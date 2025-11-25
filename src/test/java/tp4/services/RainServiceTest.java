package tp4.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RainServiceTest {

    private RainService rainService;

    @BeforeEach
    public void setUp() {
        rainService = new RainService();
    }

    @Test
    public void test_getRain_avecCoordonneesValides_retournePluie() throws WeatherException {
        // Arrange
        double latitude = 48.8566;  // Paris
        double longitude = 2.3522;

        // Act
        double rain = rainService.getData(latitude, longitude);

        // Assert
        assertTrue(rain >= 0, "La quantité de pluie ne peut pas être négative");
    }

    @Test
    public void test_getRain_avecCoordonneesOcean_retournePluieValide() throws WeatherException {
        // Arrange
        double latitude = 0.0;  // Océan Atlantique
        double longitude = -30.0;

        // Act
        double rain = rainService.getData(latitude, longitude);

        // Assert
        assertTrue(rain >= 0, "La quantité de pluie ne peut pas être négative");
    }

    @Test
    public void test_getRain_avecCoordonneesInvalide_lanceException() {
        // Arrange
        double latitude = -100.0;  // Invalide (< -90)
        double longitude = 50.0;

        // Act & Assert
        assertThrows(WeatherException.class, () -> {
            rainService.getData(latitude, longitude);
        });
    }
}