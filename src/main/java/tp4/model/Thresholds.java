package tp4.model;

public class Thresholds {
	public final Double minTemp;
	public final Double maxTemp;
	public final Double maxWind;
	public final Double maxRain;

	public Thresholds(Double minTemp, Double maxTemp, Double maxWind, Double maxRain) {
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.maxWind = maxWind;
		this.maxRain = maxRain;
	}
}
