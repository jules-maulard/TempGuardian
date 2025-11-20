package tp4;

public class LocationException extends Exception {
	public LocationException(String message) {
		super(message);
	}

	public LocationException(String message, Exception cause) {
		super(message, cause);
	}
}
