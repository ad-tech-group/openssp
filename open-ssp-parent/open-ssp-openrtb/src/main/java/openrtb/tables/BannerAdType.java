package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum BannerAdType {
	XHTML_TEXT_AD(1),
	XHTML_BANNER_AD(2),
	JAVASCRIPT_AD(3),
	IFRAME(4);

	private int value;

	BannerAdType(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static BannerAdType convertValue(final int value) {
		for (final BannerAdType v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
