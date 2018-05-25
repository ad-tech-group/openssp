package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum AppType {
	FREE(0),
	PAID(1);

	private int value;

	AppType(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static AppType convertValue(final int value) {
		for (final AppType v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
