package tp4.services;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationService {
    private long lastRequestTime = 0;
    
    public double[] translateAdresse(String adress) throws LocationException, InterruptedException {
        long now = System.currentTimeMillis();
        long interval = now - lastRequestTime;
        if (interval < 1000) {
            Thread.sleep(1000 - interval);
        }
        lastRequestTime = System.currentTimeMillis();

        String apiKey = Config.getGeocodeApiKey();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(String.format("https://geocode.maps.co/search?q=%s&api_key=%s", adress, apiKey))
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new LocationException("Unexpected response " + response);
            }

            String responseBody = response.body().string();
            JSONArray jArray = new JSONArray(responseBody);

            if (jArray.length() == 0) {
                throw new LocationException("Address not found: " + adress);
            }

            JSONObject jObject = jArray.getJSONObject(0);
            double lat = jObject.getDouble("lat");
            double lon = jObject.getDouble("lon");
            return new double[]{ lat, lon };
        } catch (IOException e) {
            throw new LocationException("Received an IOException", e);
        }
    }
}