package tp4;

import tp4.model.AddressConfig;
import tp4.model.UserConfig;
import tp4.services.LocationService;
import tp4.services.RainService;
import tp4.services.TemperatureService;
import tp4.services.WeatherService;
import tp4.services.WindService;

public class AlertEngine {

    public LocationService geocodingService;
    public WeatherService rainService;
    public WeatherService windService;
    public WeatherService temperatureService;

    public AlertEngine(LocationService geocodingService) {
        this.geocodingService = geocodingService;
        this.rainService = new RainService();
        this.windService = new WindService();
        this.temperatureService = new TemperatureService();
    }
    
    public void run(UserConfig user) {
        if (user.allAlertsDisabled) {
            return;
        }

        for (AddressConfig address : user.addresses) {
            if (address.alertsDisabled) {
                continue;
            }

            try {
                double[] coords = geocodingService.translateAdresse(address.address);
            } catch (LocationException | InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
