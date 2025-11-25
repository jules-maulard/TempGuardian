package tp4.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlertCSVTest {

    @TempDir
    Path tempDir;
    
    private String testCsvFile;

    @BeforeEach
    public void setUp() {
        // Utiliser un fichier unique pour chaque test
        testCsvFile = tempDir.resolve("test_alerts_" + System.nanoTime() + ".csv").toString();
    }

    @Test
    public void test_constructor_creeFichierSiInexistant() {
        // Act
        AlertCSV alertCSV = new AlertCSV(testCsvFile);

        // Assert
        File file = new File(testCsvFile);
        assertTrue(file.exists(), "Le fichier CSV doit être créé");
    }

    @Test
    public void test_constructor_ajouteEntete() throws IOException {
        // Act
        AlertCSV alertCSV = new AlertCSV(testCsvFile);

        // Assert
        List<String> lines = Files.readAllLines(Paths.get(testCsvFile));
        assertTrue(lines.size() >= 1, "Le fichier doit contenir au moins l'entête");
        Arrays.asList(lines.get(0).split(",")).equals(
            List.of("UserId", "Type", "Releve", "Threshold", "Timestamp", "Address")
        );
    }

    @Test
    public void test_saveAlert_ajouteLigneAuFichier() throws IOException {
        // Arrange
        AlertCSV alertCSV = new AlertCSV(testCsvFile);
        Integer userId = 1;
        String type = "MinTemp";
        double releve = 5.0;
        double threshold = 10.0;
        Date timestamp = new Date();
        String address = "Paris";

        // Act
        alertCSV.saveAlert(userId, type, releve, threshold, timestamp, address);

        // Assert
        List<String> lines = Files.readAllLines(Paths.get(testCsvFile));
        assertTrue(lines.size() >= 2, "Le fichier doit contenir l'entête et au moins une alerte");
        
        // Chercher la ligne avec MinTemp
        boolean found = lines.stream().anyMatch(line -> line.contains("MinTemp"));
        assertTrue(found, "La ligne avec MinTemp doit exister");
    }

    @Test
    public void test_saveAlert_formatteCorrectementLesDonnees2Lignes() throws IOException {
        // Arrange
        AlertCSV alertCSV = new AlertCSV(testCsvFile);
        Integer userId = 1;
        String type = "MaxTemp";
        double releve = 35.5;
        double threshold = 30.0;
        Date timestamp = new Date();
        String address = "Lyon";

        // Act
        alertCSV.saveAlert(userId, type, releve, threshold, timestamp, address);

        // Assert
        List<String> lines = Files.readAllLines(Paths.get(testCsvFile));
        
        // Chercher la dernière ligne ajoutée
        String lastLine = lines.stream()
            .filter(line -> line.contains("MaxTemp") && line.contains("Lyon"))
            .findFirst()
            .orElse("");
        
        assertTrue(lastLine.contains("1"), "Doit contenir l'userId");
        assertTrue(lastLine.contains("MaxTemp"), "Doit contenir le type");
        assertTrue(lastLine.contains("Lyon"), "Doit contenir l'adresse");
    }

    @Test
    public void test_saveAlert_avecValeursDecimales() throws IOException {
        // Arrange
        AlertCSV alertCSV = new AlertCSV(testCsvFile);
        Integer userId = 1;
        String type = "MinTemp";
        double releve = 5.123456;
        double threshold = 10.987654;
        Date timestamp = new Date();
        String address = "Paris";

        // Act
        alertCSV.saveAlert(userId, type, releve, threshold, timestamp, address);

        // Assert
        List<String> lines = Files.readAllLines(Paths.get(testCsvFile));
        String content = String.join("\n", lines);
        
        // Vérifier qu'il y a bien des décimales formatées
        assertTrue(content.contains("5,") || content.contains("5."), 
            "Le relevé doit être formatté");
    }
}