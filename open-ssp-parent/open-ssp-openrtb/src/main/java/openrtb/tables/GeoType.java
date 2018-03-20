package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum GeoType {
	GPS(1),
	IP(2),
	USER(3);

	private int value;

	GeoType(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static GeoType convertValue(final int value) {
		for (final GeoType v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
