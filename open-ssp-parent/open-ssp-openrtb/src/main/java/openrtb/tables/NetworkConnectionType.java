package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum NetworkConnectionType {
	UNKNOWN(0),
	ETHERNET(1),
	WIFI(2),
	CELLULAR_NETWORK_UNKNWON_GENERATION(3),
	CELLULAR_NETWORK_2G(4),
	CELLULAR_NETWORK_3G(5),
	CELLULAR_NETWORK_4G(6);

	private int value;

	NetworkConnectionType(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static NetworkConnectionType convertValue(final int value) {
		for (final NetworkConnectionType v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
