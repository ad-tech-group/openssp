package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum PrivacyPolicy {
	NO(0),
	YES(1);

	private int value;

	PrivacyPolicy(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static PrivacyPolicy convertValue(final int value) {
		for (final PrivacyPolicy v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
