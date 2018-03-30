package openrtb.tables;

/**
 * @author Brian Sorensen
 *
 */
public enum CreativeAttribute {
	 AUDIO_AD_AUTO_PLAY(1),
	 AUDIO_AD_USER_INITIATED(2),
	 EXPANDABLE_AUTOMATIC(3),
	 EXPANDABLE_USER_INITIATED_CLICK(4),
	 EXPANDABLE_USER_INITIATED_ROLLOVER(5),
	 IN_BANNER_VIDEO_AD_AUTO_PLAY(6),
	 IN_BANNER_VIDEO_AD_USER_INITIATED(7),
	 POP(8),
	 PROVOCATIVE_OR_SUGGESTIVE_IMAGERY(9),
	 SHAKY_FLASHING_FLICKERING_EXTREME_ANIMATION_SMILEYS(10),
	 SURVEYS(11),
	 TEXT_ONLY(12),
	 USER_INTERACTIVE(13),
	 WINDOWS_DIALOG_OR_ALERT_STYLE(14),
	 HAS_AUDIO_ON_OFF_BUTTON(15),
	 AD_PROVICES_SKIP_BUTTON(16),
	 ADOBE_FLASH(17);


	private int value;

	CreativeAttribute(int value) {
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static CreativeAttribute convertValue(final int value) {
		for (final CreativeAttribute v : values()) {
			if (v.getValue() == value) {
				return v;
			}
		}
		return null;
	}
}
