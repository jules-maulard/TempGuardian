package tp4.services;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getGeocodeApiKey() {
        return dotenv.get("GEOCODE_API_KEY");
    }
}
