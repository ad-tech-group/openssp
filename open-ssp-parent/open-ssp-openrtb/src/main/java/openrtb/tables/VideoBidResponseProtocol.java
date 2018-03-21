package openrtb.tables;

/**
 * @author André Schmer
 *
 */
public enum VideoBidResponseProtocol {

	/**
	 * 1 (VAST 1.0)
	 */
	VAST_1_0(1),

	/**
	 * 2 (VAST 2.0)
	 */
	VAST_2_0(2),

	/**
	 * 3 (VAST 3.0)
	 */
	VAST_3_0(3),

	/**
	 * 4 (VAST 1.0 WRAPPER)
	 */
	VAST_1_0_WRAPPER(4),

	/**
	 * 5 (VAST 2.0 WRAPPER)
	 */
	VAST_2_0_WRAPPER(5),

	/**
	 * 6 (VAST 3.0 WRAPPER)
	 */
	VAST_3_0_WRAPPER(6),

	/**
	 * 7 (VAST 4.0)
	 */
	VAST_4_0(7),

	/**
	 * 8 (VAST 4.0 WRAPPER)
	 */
	VAST_4_0_WRAPPER(8),

	/**
	 * 9 (DAAST 1.0)
	 */
	DAAST_1_0(9),

	/**
	 * 10 (DAAST 1.0 WRAPPER)
	 */
	DAAST_1_0_WRAPPER(10);

	private int value;

	private VideoBidResponseProtocol(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static VideoBidResponseProtocol convert(final int value) {
		for (final VideoBidResponseProtocol protocolValue : values()) {
			if (protocolValue.value == value) {
				return protocolValue;
			}
		}

		return VAST_3_0;
	}

	public static VideoBidResponseProtocol convertFromString(final String value) {
		for (final VideoBidResponseProtocol protocolValue : values()) {
			if (protocolValue.value == Integer.valueOf(value).intValue()) {
				return protocolValue;
			}
		}

		return VAST_3_0;
	}

}
