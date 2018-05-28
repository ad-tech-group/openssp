package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum ApiFramework {

	VPAID_1_0(1),
	VPAID_2_0(2),
	MRAID_1(3),
	ORMMA(4),
	MRAID_2(5);

	private int value;

	ApiFramework(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static ApiFramework convertValue(final int value) {
		for (final ApiFramework v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
