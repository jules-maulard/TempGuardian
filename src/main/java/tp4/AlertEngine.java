package tp4;

import java.util.Date;

import tp4.model.AddressConfig;
import tp4.model.AlertCSV;
import tp4.model.Thresholds;
import tp4.model.UserConfig;
import tp4.services.LocationService;
import tp4.services.RainService;
import tp4.services.TemperatureService;
import tp4.services.WeatherException;
import tp4.services.WeatherService;
import tp4.services.WindService;

public class AlertEngine {

    public LocationService geocodingService;
    public AlertCSV alertCSV;

    public WeatherService rainService;
    public WeatherService windService;
    public WeatherService temperatureService;

    public AlertEngine(LocationService geocodingService, AlertCSV alertCSV) {
        this.geocodingService = geocodingService;
        this.rainService = new RainService();
        this.windService = new WindService();
        this.temperatureService = new TemperatureService();
        this.alertCSV = alertCSV;
    }
    
    public void run(UserConfig user) {
        if (user.allAlertsDisabled) {
            return;
        }

        for (AddressConfig address : user.addresses) {
            if (address.alertsDisabled) {
                continue;
            }

            double[] coords = new double[2];
            try {
                coords = geocodingService.translateAdresse(address.address);
            } catch (LocationException | InterruptedException e) {
                e.printStackTrace();
            }

            double rain = 0;
            double wind = 0;
            double temperature = 0;
            try {
                temperature = temperatureService.getData(coords[0], coords[1]);
                wind = windService.getData(coords[0], coords[1]);
                rain = rainService.getData(coords[0], coords[1]);
            } catch (WeatherException e) {
                e.printStackTrace();
            }

            checkThresholds(address, temperature, wind, rain);
        }
    }

    private void checkThresholds(AddressConfig addr, double t, double w, double r) {
        Thresholds th = addr.thresholds;

        if (th.minTemp != null && t < th.minTemp)
            alertCSV.saveAlert("MinTemp", t, th.minTemp, new Date(), addr.address);

        if (th.maxTemp != null && t > th.maxTemp)

        if (th.maxWind != null && w > th.maxWind)

        if (th.maxRain != null && r > th.maxRain)
    }
}
