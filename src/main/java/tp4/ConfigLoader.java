package tp4;

import tp4.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigLoader {

    public List<UserConfig> load(String path) throws Exception {
        List<UserConfig> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String header = br.readLine(); // ignore l'entête

            String line;
            while ((line = br.readLine()) != null) {
                String[] t = line.split(",");

                // On suppose le CSV : clientId,allAlertsDisabled,address,minTemp,maxTemp,maxWind,maxRain
                boolean allAlertsDisabled = Boolean.parseBoolean(t[1]);
                String address = t[2];

                Integer clientId = parseDoubleOrNull(t[0]).intValue();
                Double minTemp = parseDoubleOrNull(t[3]);
                Double maxTemp = parseDoubleOrNull(t[4]);
                Double maxWind = parseDoubleOrNull(t[5]);
                Double maxRain = parseDoubleOrNull(t[6]);

                Thresholds th = new Thresholds(minTemp, maxTemp, maxWind, maxRain);
                AddressConfig addr = new AddressConfig(address, !allAlertsDisabled, th);

                Map<Integer, List<AddressConfig>> clientConfig = new HashMap<>();
                clientConfig.get(clientId).add(addr);

                UserConfig user = null;
                for (Integer id : clientConfig.keySet()) {
                    user = new UserConfig(clientId, allAlertsDisabled, clientConfig.get(id));
                }

                users.add(user);
            }
        }

        return users;
    }

    private Double parseDoubleOrNull(String s) {
        return (s == null || s.isBlank()) ? null : Double.parseDouble(s);
    }
}
