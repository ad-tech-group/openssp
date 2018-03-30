package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum VideoLinearity {

	LINEAR(1), NON_LINEAR(2);

	private int value;

	VideoLinearity(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static VideoLinearity convertValue(final int value) {
		for (final VideoLinearity v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
