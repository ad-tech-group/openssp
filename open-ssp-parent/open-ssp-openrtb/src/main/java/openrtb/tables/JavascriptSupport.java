package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum JavascriptSupport {
	NO(0),
	YES(1);

	private int value;

	JavascriptSupport(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static JavascriptSupport convertValue(final int value) {
		for (final JavascriptSupport v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
