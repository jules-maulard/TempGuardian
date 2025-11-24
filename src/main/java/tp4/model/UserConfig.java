package tp4.model;

import java.util.List;

public class UserConfig {
	public final boolean allAlertsDisabled;
	public final List<AddressConfig> addresses;
	public final int userId;

	public UserConfig(int userId, boolean allAlertsDisabled, List<AddressConfig> addresses) {
		this.userId = userId;
		this.allAlertsDisabled = allAlertsDisabled;
		this.addresses = addresses;
	}
}
