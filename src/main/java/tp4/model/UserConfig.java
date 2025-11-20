package tp4.model;

import java.util.List;

public class UserConfig {
	public final boolean allAlertsDisabled;
	public final List<AddressConfig> addresses;
	final IAlertOutput alertOutput;

	public UserConfig(boolean allAlertsDisabled, List<AddressConfig> addresses) {
		this.allAlertsDisabled = allAlertsDisabled;
		this.addresses = addresses;
		this.alertOutput = new AlertCSV(this);
	}
}
