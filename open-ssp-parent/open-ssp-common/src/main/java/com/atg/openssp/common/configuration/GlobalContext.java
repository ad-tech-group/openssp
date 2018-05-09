package com.atg.openssp.common.configuration;

/**
 * @author André Schmer
 *
 */
public class GlobalContext extends Context {

	private static long executionTimeout;
	private static float drawModeration;
	private static String entryValidatorHandlerForBannerClass;
	private static String entryValidatorHandlerForVideoClass;
	private static String entryValidatorHandlerForHeaderClass;
	private static String adserverBrokerHandlerClass;
    private static String bidRequestBuilderHandlerForVideoClass;
	private static String bidRequestBuilderHandlerForBannerClass;
	private static String bidRequestBuilderHandlerForHeaderClass;
	private static String cookieSyncHandlerClass;
	private static String geoIpInfoHandlerClass;

	public static void refreshContext() {
		// default value is 500 milliseconds
		executionTimeout = Long.parseLong(ContextCache.instance.get(ContextProperties.EXECUTION_TIMEOUT) != null ? ContextCache.instance.get(
		        ContextProperties.EXECUTION_TIMEOUT) : "500");

		// default value is 0.3
		drawModeration = Float.parseFloat(ContextCache.instance.get(ContextProperties.DRAW_MODERATION) != null ? ContextCache.instance.get(
		        ContextProperties.DRAW_MODERATION) : "0.3");

		entryValidatorHandlerForBannerClass = ContextCache.instance.get(ContextProperties.VALIDATOR_HANDLER_FOR_BANNER_CLASS);
		entryValidatorHandlerForVideoClass = ContextCache.instance.get(ContextProperties.VALIDATOR_HANDLER_FOR_VIDEO_CLASS);
		entryValidatorHandlerForHeaderClass = ContextCache.instance.get(ContextProperties.VALIDATOR_HANDLER_FOR_HEADER_CLASS);
		adserverBrokerHandlerClass = ContextCache.instance.get(ContextProperties.ADSERVER_BROKER_HANDLER_CLASS);
        bidRequestBuilderHandlerForVideoClass = ContextCache.instance.get(ContextProperties.BUILD_REQUEST_BUILDER_HANDLER_FOR_VIDEO_CLASS);
		bidRequestBuilderHandlerForBannerClass = ContextCache.instance.get(ContextProperties.BUILD_REQUEST_BUILDER_HANDLER_FOR_BANNER_CLASS);
		bidRequestBuilderHandlerForHeaderClass = ContextCache.instance.get(ContextProperties.BUILD_REQUEST_BUILDER_HANDLER_FOR_HEADER_CLASS);
		cookieSyncHandlerClass = ContextCache.instance.get(ContextProperties.COOKIE_SYNC_HANDLER_CLASS);
		geoIpInfoHandlerClass = ContextCache.instance.get(ContextProperties.GEO_IP_INFO_HANDLER_CLASS);
	}

	/**
	 * The timeout in milliseconds.
	 * <p>
	 * The value can be changed outside in {@code local.runtime.xml} properties file.
	 * 
	 * @return timeout in milliseconds
	 */
	public static long getExecutionTimeout() {
		return executionTimeout;
	}

	/**
	 * The draw moderation.
	 * <p>
	 * The value can be changed outside in {@code local.runtime.xml} properties file.
	 * 
	 * @return draw moderation
	 */
	public static float getDrawModeration() {
		return drawModeration;
	}

    public static String getEntryValidatorHandlerForVideoClass() {
        return entryValidatorHandlerForVideoClass;
    }

	public static String getEntryValidatorHandlerForBannerClass() {
		return entryValidatorHandlerForBannerClass;
	}

	public static String getEntryValidatorHandlerForHeaderClass() {
		return entryValidatorHandlerForHeaderClass;
	}

	public static String getAdserverBrokerHandlerClass() {
        return adserverBrokerHandlerClass;
    }

    public static String getBidRequestBuilderHandlerForVideoObjectsClass() {
        return bidRequestBuilderHandlerForVideoClass;
    }

	public static String getBidRequestBuilderHandlerForBannerObjectsClass() {
		return bidRequestBuilderHandlerForBannerClass;
	}

	public static String getBidRequestBuilderHandlerForHeaderBiddingClass() {
		return bidRequestBuilderHandlerForHeaderClass;
	}

	public static String getCookieSyncHandlerClass() {
		return cookieSyncHandlerClass;
	}

	public static String getGeoIpInfoHandlerClass() {
		return geoIpInfoHandlerClass;
	}
}
