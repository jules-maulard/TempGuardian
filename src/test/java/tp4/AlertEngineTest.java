package tp4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tp4.model.AddressConfig;
import tp4.model.AlertCSV;
import tp4.model.Thresholds;
import tp4.model.UserConfig;
import tp4.services.LocationException;
import tp4.services.LocationService;
import tp4.services.WeatherException;
import tp4.services.WeatherService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class AlertEngineTest {

    private LocationService mockLocationService;
    private AlertCSV mockAlertCSV;
    private WeatherService mockRainService;
    private WeatherService mockWindService;
    private WeatherService mockTemperatureService;
    private AlertEngine alertEngine;

    @BeforeEach
    public void setUp() {
        // Arrange - Créer les mocks
        mockLocationService = mock(LocationService.class);
        mockAlertCSV = mock(AlertCSV.class);
        
        alertEngine = new AlertEngine(mockLocationService, mockAlertCSV);
        
        // Remplacer les services météo par des mocks
        mockRainService = mock(WeatherService.class);
        mockWindService = mock(WeatherService.class);
        mockTemperatureService = mock(WeatherService.class);
        
        alertEngine.rainService = mockRainService;
        alertEngine.windService = mockWindService;
        alertEngine.temperatureService = mockTemperatureService;
    }

    @Test
    public void test_run_avecToutesLesAlertesDesactivees_neGenerePasDAlertes() throws Exception {
        // Arrange
        UserConfig user = new UserConfig(1, true, new ArrayList<>());
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verifyNoInteractions(mockLocationService);
        verifyNoInteractions(mockAlertCSV);
    }

    @Test
    public void test_run_avecAlertesAdresseDesactivees_ignoreAdresse() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("Paris", true, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verifyNoInteractions(mockLocationService);
        verifyNoInteractions(mockAlertCSV);
    }

    @Test
    public void test_run_avecTemperatureInferieureAuMinimum_genereAlerteMinTemp() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("Paris", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Paris")).thenReturn(new double[]{48.8566, 2.3522});
        when(mockTemperatureService.getData(48.8566, 2.3522)).thenReturn(5.0);
        when(mockWindService.getData(48.8566, 2.3522)).thenReturn(30.0);
        when(mockRainService.getData(48.8566, 2.3522)).thenReturn(10.0);
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verify(mockAlertCSV, times(1)).saveAlert(
            eq(1), 
            eq("MinTemp"), 
            eq(5.0), 
            eq(10.0), 
            any(Date.class), 
            eq("Paris")
        );
    }

    @Test
    public void test_run_avecTemperatureSuperieureAuMaximum_genereAlerteMaxTemp() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("Lyon", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Lyon")).thenReturn(new double[]{45.75, 4.85});
        when(mockTemperatureService.getData(45.75, 4.85)).thenReturn(35.0);
        when(mockWindService.getData(45.75, 4.85)).thenReturn(30.0);
        when(mockRainService.getData(45.75, 4.85)).thenReturn(10.0);
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verify(mockAlertCSV, times(1)).saveAlert(
            eq(1), 
            eq("MaxTemp"), 
            eq(35.0), 
            eq(30.0), 
            any(Date.class), 
            eq("Lyon")
        );
    }

    @Test
    public void test_run_avecVentSuperieurAuMaximum_genereAlerteMaxWind() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("Marseille", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Marseille")).thenReturn(new double[]{43.3, 5.4});
        when(mockTemperatureService.getData(43.3, 5.4)).thenReturn(20.0);
        when(mockWindService.getData(43.3, 5.4)).thenReturn(60.0);
        when(mockRainService.getData(43.3, 5.4)).thenReturn(10.0);
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verify(mockAlertCSV, times(1)).saveAlert(
            eq(1), 
            eq("MaxWind"), 
            eq(60.0), 
            eq(50.0), 
            any(Date.class), 
            eq("Marseille")
        );
    }

    @Test
    public void test_run_avecPluieSuperieurAuMaximum_genereAlerteMaxRain() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("Bordeaux", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Bordeaux")).thenReturn(new double[]{44.84, -0.58});
        when(mockTemperatureService.getData(44.84, -0.58)).thenReturn(20.0);
        when(mockWindService.getData(44.84, -0.58)).thenReturn(30.0);
        when(mockRainService.getData(44.84, -0.58)).thenReturn(25.0);
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verify(mockAlertCSV, times(1)).saveAlert(
            eq(1), 
            eq("MaxRain"), 
            eq(25.0), 
            eq(20.0), 
            any(Date.class), 
            eq("Bordeaux")
        );
    }

    @Test
    public void test_run_avecPlusieursSeuilsDepasses_genereMultiplesAlertes() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("Nice", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Nice")).thenReturn(new double[]{43.7, 7.25});
        when(mockTemperatureService.getData(43.7, 7.25)).thenReturn(35.0); // Dépasse maxTemp
        when(mockWindService.getData(43.7, 7.25)).thenReturn(60.0); // Dépasse maxWind
        when(mockRainService.getData(43.7, 7.25)).thenReturn(25.0); // Dépasse maxRain
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verify(mockAlertCSV, times(3)).saveAlert(anyInt(), anyString(), anyDouble(), anyDouble(), any(Date.class), anyString());
    }

    @Test
    public void test_run_avecSeuilsNuls_neGenereAucuneAlerte() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(null, null, null, null);
        AddressConfig address = new AddressConfig("Toulouse", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Toulouse")).thenReturn(new double[]{43.6, 1.44});
        when(mockTemperatureService.getData(43.6, 1.44)).thenReturn(35.0);
        when(mockWindService.getData(43.6, 1.44)).thenReturn(60.0);
        when(mockRainService.getData(43.6, 1.44)).thenReturn(25.0);
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verifyNoInteractions(mockAlertCSV);
    }

    @Test
    public void test_run_avecExceptionGeocoding_continueExecution() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("AdresseInvalide", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("AdresseInvalide"))
            .thenThrow(new LocationException("Adresse introuvable"));
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verify(mockLocationService, times(1)).translateAdresse("AdresseInvalide");
    }

    @Test
    public void test_run_avecExceptionMeteo_continueExecution() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("Lille", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Lille")).thenReturn(new double[]{50.63, 3.06});
        when(mockTemperatureService.getData(50.63, 3.06))
            .thenThrow(new WeatherException("Service météo indisponible"));
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verify(mockLocationService, times(1)).translateAdresse("Lille");
        verify(mockTemperatureService, times(1)).getData(50.63, 3.06);
    }

    @Test
    public void test_run_avecPlusieursAdresses_traiteToutes() throws Exception {
        // Arrange
        Thresholds thresholds1 = new Thresholds(10.0, 30.0, 50.0, 20.0);
        Thresholds thresholds2 = new Thresholds(5.0, 35.0, 60.0, 25.0);
        
        AddressConfig address1 = new AddressConfig("Paris", false, thresholds1);
        AddressConfig address2 = new AddressConfig("Lyon", false, thresholds2);
        
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);
        
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Paris")).thenReturn(new double[]{48.8566, 2.3522});
        when(mockLocationService.translateAdresse("Lyon")).thenReturn(new double[]{45.75, 4.85});
        
        when(mockTemperatureService.getData(anyDouble(), anyDouble())).thenReturn(20.0);
        when(mockWindService.getData(anyDouble(), anyDouble())).thenReturn(30.0);
        when(mockRainService.getData(anyDouble(), anyDouble())).thenReturn(10.0);
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verify(mockLocationService, times(2)).translateAdresse(anyString());
    }

    @Test
    public void test_run_avecValeursSurLeSeuil_neGenereAucuneAlerte() throws Exception {
        // Arrange
        Thresholds thresholds = new Thresholds(10.0, 30.0, 50.0, 20.0);
        AddressConfig address = new AddressConfig("Nantes", false, thresholds);
        List<AddressConfig> addresses = new ArrayList<>();
        addresses.add(address);
        UserConfig user = new UserConfig(1, false, addresses);
        
        when(mockLocationService.translateAdresse("Nantes")).thenReturn(new double[]{47.22, -1.55});
        when(mockTemperatureService.getData(47.22, -1.55)).thenReturn(30.0); // Égal à maxTemp
        when(mockWindService.getData(47.22, -1.55)).thenReturn(50.0); // Égal à maxWind
        when(mockRainService.getData(47.22, -1.55)).thenReturn(20.0); // Égal à maxRain
        
        // Act
        alertEngine.run(user);
        
        // Assert
        verifyNoInteractions(mockAlertCSV);
    }
}