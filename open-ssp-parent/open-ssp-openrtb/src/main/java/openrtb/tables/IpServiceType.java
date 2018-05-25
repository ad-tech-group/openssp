package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum IpServiceType {
	IP2LOCATION(1),
	NEUSTAR(2), // Neustar (Quova)
	MAXMIND(3),
	NETAQUITY(4); // 4 NetAquity (Digital Element)

	private int value;

	IpServiceType(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static IpServiceType convertValue(final int value) {
		for (final IpServiceType v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
