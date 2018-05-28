package restful.context;

/**
 * @author André Schmer
 * 
 */
public enum Path {

	/**
	 * ssp-services
	 */
	CORE("ssp-services/lookup"),

	/**
	 * supplier
	 */
	SUPPLIER("supplier"),

	/**
	 * sspAdapter
	 */
	SSP_ADAPTER("sspAdapter"),

	/**
	 * eurref
	 */
	EUR_REF("eurref"),

	/**
	 * ?website=1
	 */
	WEBSITE("?website=1"),

	/**
	 * pricelayer
	 */
	PRICELAYER("pricelayer"),

	/**
	 * site
	 */
	SITE("site"),

	/**
	 * app
	 */
	APP("app"),

	/**
	 * ssp-services
	 */
	ADS_CORE("ssp-services/ads"),

	/**
	 * banner
	 */
	BANNER_ADS("banner"),

	/**
	 * video
	 */
	VIDEO_ADS("video");

	private String value;

	private Path(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
