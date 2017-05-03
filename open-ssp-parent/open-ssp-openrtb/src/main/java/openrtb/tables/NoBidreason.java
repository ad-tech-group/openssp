package openrtb.tables;

/**
 * @author André Schmer
 *
 */
public enum NoBidreason {

	UNKNOWN_ERROR(0),

	TECHNICAL_ERROR(1),

	INVALID_REQUEST(2),

	KNOWN_WEB_SPIDER(3),

	SUSPECTED_NON_HUMAN_TRAFFIC(4),

	CLOUD_DATACENTER_OR_PROXY_IP(5),

	UNSUPPORTED_DEVICE(6),

	BLOCKED_PUBLISHER_OR_SITE(7),

	UNMATCHED_USER(8);

	private int value;

	private NoBidreason(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static NoBidreason convertValue(final int value) {
		for (final NoBidreason reason : values()) {
			if (reason.getValue() == value) {
				return reason;
			}
		}
		return null;
	}

}
