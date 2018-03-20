package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum DeviceType {
	MOBILE_OR_TABLET(1),
	PERSONAL_COMPUTER(2),
	CONNECTED_TV(3),
	PHONE(4),
	TABLET(5),
	CONNECTED_DEVICE(6),
	SET_TOP_BOX(7),
	OUT_OF_HOME(8);

	private int value;

	DeviceType(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static DeviceType convertValue(final int value) {
		for (final DeviceType v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
