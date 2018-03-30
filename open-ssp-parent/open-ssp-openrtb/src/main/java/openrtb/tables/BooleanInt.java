package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum BooleanInt {
	FALSE(0),
	TRUE(1);

	private int value;

	BooleanInt(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static BooleanInt convertValue(final int value) {
		for (final BooleanInt v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
