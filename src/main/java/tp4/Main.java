package tp4;

import java.util.List;

import tp4.model.AlertCSV;
import tp4.model.UserConfig;
import tp4.services.LocationService;

public class Main {

    // nom fichier CSV
    public static final String csv_file  = "alerts.csv";

    public static void main(String[] args) {
        String config_path = "config.csv";
        ConfigLoader configLoader = new ConfigLoader();
        List<UserConfig> userConfigs = configLoader.load(config_path);

        LocationService locationService = new LocationService();
        AlertCSV alertCSV = new AlertCSV(csv_file);
        AlertEngine alertEngine = new AlertEngine(locationService, alertCSV);

        for (UserConfig userConfig : userConfigs) {
            alertEngine.run(userConfig);
        }
    }
}
