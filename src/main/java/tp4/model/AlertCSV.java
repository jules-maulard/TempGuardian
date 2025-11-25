package tp4.model;

import java.io.IOException;
import java.nio.file.*;
import java.util.Date;

public class AlertCSV {

    private String csv_file;

    public AlertCSV(String csv_file) {
        // creer le fichier s'il n'existe pas
        Path path = Paths.get(csv_file);
        this.csv_file = csv_file;
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
                // ecrire l'entete
                Files.write(path,
                        "UserId,Type,Releve,Threshold,Timestamp,Address\n".getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveAlert(Integer userId, String type, double releve, double threshold, Date timestamp, String address) {
        String line = String.format("%d,%s,%.2f,%.2f,%s,%s\n",
                userId, type, releve, threshold, timestamp.toString(), address);
        try {
            Files.write(Paths.get(csv_file),
                    line.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
