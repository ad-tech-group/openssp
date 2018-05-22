package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum ExpandableDirectionType {
	Left(1),
	Right(2),
	Up(3),
	Down(4),
	FullScreen(5);

	private int value;

	ExpandableDirectionType(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static ExpandableDirectionType convertValue(final int value) {
		for (final ExpandableDirectionType v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
