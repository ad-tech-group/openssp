package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum ImpressionSecurity {

	NON_SECURE(0), SECURE(1);

	private int value;

	ImpressionSecurity(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static ImpressionSecurity convertValue(final int value) {
		for (final ImpressionSecurity v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
