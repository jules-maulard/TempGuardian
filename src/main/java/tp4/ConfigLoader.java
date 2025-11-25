package tp4;

import tp4.model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigLoader {

    public List<UserConfig> load(String path) {
        List<UserConfig> users = new ArrayList<>();

        Map<Integer, List<AddressConfig>> addressesByClient = new HashMap<>();
        Map<Integer, Boolean> settingsByClient = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String header = br.readLine(); // ignore l'entête

            String line;
            while ((line = br.readLine()) != null) {
                String[] t = line.split(",");

                Integer clientId = Integer.parseInt(t[0].trim()); 
                boolean allAlertsDisabled = Boolean.parseBoolean(t[1].trim());
                String address = t[2].trim();

                Double minTemp = parseDoubleOrNull(t[3]);
                Double maxTemp = parseDoubleOrNull(t[4]);
                Double maxWind = parseDoubleOrNull(t[5]);
                Double maxRain = parseDoubleOrNull(t[6]);

                System.out.println("Loaded config for clientId " + clientId + ": address=" + address +
                        ", minTemp=" + minTemp + ", maxTemp=" + maxTemp +
                        ", maxWind=" + maxWind + ", maxRain=" + maxRain +
                        ", allAlertsDisabled=" + allAlertsDisabled);

                Thresholds th = new Thresholds(minTemp, maxTemp, maxWind, maxRain);
                AddressConfig addr = new AddressConfig(address, allAlertsDisabled, th);

                if (!addressesByClient.containsKey(clientId)) {
                    addressesByClient.put(clientId, new ArrayList<>());
                }
                addressesByClient.get(clientId).add(addr);

                settingsByClient.put(clientId, allAlertsDisabled);
            }

            for (Integer id : addressesByClient.keySet()) {
                List<AddressConfig> clientAddresses = addressesByClient.get(id);
                Boolean allDisabled = settingsByClient.get(id);
                
                users.add(new UserConfig(id, allDisabled, clientAddresses));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public static Double parseDoubleOrNull(String s) {
        return (s == null || s.isBlank()) ? null : Double.parseDouble(s);
    }
}