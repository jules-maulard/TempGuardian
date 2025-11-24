package tp4.services;

public interface WeatherService {
    double getData(double latitude, double longitude) throws WeatherException;
}
