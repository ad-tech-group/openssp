package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum AuctionType {
	FIRST_PRICE(1),
	SECOND_PRICE(2);

	private int value;

	AuctionType(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static AuctionType convertValue(final int value) {
		for (final AuctionType v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
