package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum AddPosition {
	UNKNOWN(0),
	ABOVE_THE_FOLD(1),
	DEPRICATED(2),
	BELOW_THE_FOLD(3),
	HEADER(4),
	FOOTER(5),
	SIDEBAR(6),
	FULL_SCREEN(7);

	private int value;

	AddPosition(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static AddPosition convertValue(final int value) {
		for (final AddPosition v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
