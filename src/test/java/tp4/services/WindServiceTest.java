package tp4.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WindServiceTest {

    private WindService windService;

    @BeforeEach
    public void setUp() {
        windService = new WindService();
    }

    @Test
    public void test_getWind_avecCoordonneesValides_retourneVitesseVent() throws WeatherException {
        // Arrange
        double latitude = 48.8566;  // Paris
        double longitude = 2.3522;

        // Act
        double wind = windService.getData(latitude, longitude);

        // Assert
        assertTrue(wind >= 0, "La vitesse du vent ne peut pas être négative");
        assertTrue(wind <= 200, "La vitesse du vent doit être réaliste");
    }

    @Test
    public void test_getWind_avecZoneVentee_retourneVitessePositive() throws WeatherException {
        // Arrange
        double latitude = 50.0;  // Zone généralement venteuse
        double longitude = -5.0;

        // Act
        double wind = windService.getData(latitude, longitude);

        // Assert
        assertTrue(wind >= 0, "La vitesse du vent ne peut pas être négative");
    }

    @Test
    public void test_getWind_avecCoordonneesInvalide_lanceException() {
        // Arrange
        double latitude = -100.0;  // Invalide (< -90)
        double longitude = 50.0;

        // Act & Assert
        assertThrows(WeatherException.class, () -> {
            windService.getData(latitude, longitude);
        });
    }
}