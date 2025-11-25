package tp4.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TemperatureServiceTest {

    private TemperatureService temperatureService;

    @BeforeEach
    public void setUp() {
        temperatureService = new TemperatureService();
    }

    @Test
    public void test_getTemperature_avecCoordonneesValides_retourneTemperature() throws WeatherException {
        // Arrange
        double latitude = 48.8566;  // Paris
        double longitude = 2.3522;

        // Act
        double temperature = temperatureService.getData(latitude, longitude);

        // Assert
        assertTrue(temperature >= -50 && temperature <= 70, 
            "La température doit être dans une plage");
    }

    @Test
    public void test_getTemperature_avecLatitudeInvalide_lanceException() {
        // Arrange
        double latitude = 100.0;  // Invalide (> 90)
        double longitude = 2.3522;

        // Act & Assert
        assertThrows(WeatherException.class, () -> {
            temperatureService.getData(latitude, longitude);
        });
    }

    @Test
    public void test_getTemperature_avecLongitudeInvalide_lanceException() {
        // Arrange
        double latitude = 48.8566;
        double longitude = 200.0;  // Invalide (> 180)

        // Act & Assert
        assertThrows(WeatherException.class, () -> {
            temperatureService.getData(latitude, longitude);
        });
    }

    @Test
    public void test_getTemperature_avecCoordonneesPolaires_retourneTemperatureFroide() throws WeatherException {
        // Arrange
        double latitude = 80.0;  // Proche du pôle Nord
        double longitude = 0.0;

        // Act
        double temperature = temperatureService.getData(latitude, longitude);

        // Assert
        assertTrue(temperature <= 30, "La température polaire devrait être basse");
    }

    @Test
    public void test_getTemperature_avecCoordonneesEquatoriales_retourneTemperatureChaude() throws WeatherException {
        // Arrange
        double latitude = 0.0;  // Équateur
        double longitude = 30.0;

        // Act
        double temperature = temperatureService.getData(latitude, longitude);

        // Assert
        assertTrue(temperature >= -10, "La température équatoriale devrait être relativement élevée");
    }
}