package tp4.model;

public class AddressConfig {
	public final String address;
	public final boolean alertsDisabled;
	public final Thresholds thresholds;

	public AddressConfig(String address, boolean alertsDisabled, Thresholds thresholds) {
		this.address = address;
		this.alertsDisabled = alertsDisabled;
		this.thresholds = thresholds;
	}
}
