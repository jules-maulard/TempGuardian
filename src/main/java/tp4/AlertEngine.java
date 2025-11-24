package tp4;

import tp4.model.AddressConfig;
import tp4.model.UserConfig;
import tp4.services.LocationService;

public class AlertEngine {

    public LocationService geocodingService;

    public AlertEngine(LocationService geocodingService) {
        this.geocodingService = geocodingService;
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
