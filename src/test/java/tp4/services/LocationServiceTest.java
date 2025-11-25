package tp4.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationServiceTest {

    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        locationService = new LocationService();
    }

    @Test
    public void test_translateAdresse_avecAdresseValide_retourneCoordonneesValides() throws Exception {
        // Arrange
        String adresse = "Paris France";

        // Act
        double[] coords = locationService.translateAdresse(adresse);

        // Assert
        assertNotNull(coords);
        assertEquals(2, coords.length);
        assertTrue(coords[0] >= -90 && coords[0] <= 90, "Latitude doit être entre -90 et 90");
        assertTrue(coords[1] >= -180 && coords[1] <= 180, "Longitude doit être entre -180 et 180");
    }

    @Test
    public void test_translateAdresse_avecAdresseVide_lanceException() {
        // Arrange
        String adresse = "";

        // Act & Assert
        assertThrows(Exception.class, () -> {
            locationService.translateAdresse(adresse);
        });
    }
}