package com.atg.openssp.common.configuration;

import com.atg.openssp.common.annotation.RuntimeMeta;
import com.atg.openssp.common.annotation.Scope;

/**
 * @author André Schmer
 * 
 */
public enum ContextProperties {

	/**
	 * rtb
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	RTB("rtb"),

	/**
	 * debug
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	DEBUG("debug"),

	/**
	 * cdl-timeout
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	EXECUTION_TIMEOUT("cdl-timeout"),

	/**
	 * draw-moderation
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	DRAW_MODERATION("draw-moderation"),

	/**
	 * cache-trigger-expression
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	TRIGGER_EXPESSION("cache-trigger-expression"),

	/**
	 * data-privder-scheme
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	DATA_PROVIDER_SCHEME("data-provider-scheme"),

	/**
	 * data-privder-host
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	DATA_PROVIDER_HOST("data-provider-host"),

	/**
	 * data-privder-port
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	DATA_PROVIDER_PORT("data-provider-port"),

	/**
	 * master_pw
	 */
	@RuntimeMeta(type = Scope.GLOBAL, printable = false)
	MASTER_PW("master_pw"),

	/**
	 * master_user
	 */
	@RuntimeMeta(type = Scope.GLOBAL, printable = false)
	MASTER_USER("master_user"),

	/**
	 * data-privder-scheme
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_PROVIDER_SCHEME("adserver-provider-scheme"),

	/**
	 * data-privder-host
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_PROVIDER_HOST("adserver-provider-host"),

	/**
	 * data-privder-port
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_PROVIDER_PORT("adserver-provider-port"),

	/**
	 * data-privder-paths
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_PROVIDER_BANNER_PATH("adserver-provider-banner-path"),
	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_PROVIDER_VIDEO_PATH("adserver-provider-video-path"),
	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_PROVIDER_WEBSITE_PATH("adserver-provider-website-path"),
	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_PROVIDER_NATIVE_PATH("adserver-provider-native-path"),


	/**
	 * adserving
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	ADSERVING("adserving"),

	/**
	 * verbose
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	VERBOSE("verbose"),

	/**
	 * metrics
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	METRICS("metrics"),

	/**
	 * ssp
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	SSPCHANNEL("ssp"),

	@RuntimeMeta(type = Scope.GLOBAL)
	VALIDATOR_HANDLER_FOR_VIDEO_CLASS("validator-handler-for-video-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	VALIDATOR_HANDLER_FOR_BANNER_CLASS("validator-handler-for-banner-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	VALIDATOR_HANDLER_FOR_HEADER_CLASS("validator-handler-for-header-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_BROKER_HANDLER_CLASS("adserver-broker-handler-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	BUILD_REQUEST_BUILDER_HANDLER_FOR_VIDEO_CLASS("bid-request-builder-handler-for-video-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	BUILD_REQUEST_BUILDER_HANDLER_FOR_BANNER_CLASS("bid-request-builder-handler-for-banner-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	BUILD_REQUEST_BUILDER_HANDLER_FOR_HEADER_CLASS("bid-request-builder-handler-for-header-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	COOKIE_SYNC_HANDLER_CLASS("cookie-sync-handler-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	GEO_IP_INFO_HANDLER_CLASS("geo-ip-info-handler-class"),

	//###########################################################
    /**
     * app data service
     */
    @RuntimeMeta(type = Scope.LOCAL)
    APP_DATA_SERVICE_ENABLED("app-data-service-enabled"),

	@RuntimeMeta(type = Scope.LOCAL)
	APP_DATA_HANDLER_CLASS("app-data-handler-class"),

    /**
     * currency data service
     */
    @RuntimeMeta(type = Scope.LOCAL)
    CURRENCY_DATA_SERVICE_ENABLED("currency-data-service-enabled"),

	@RuntimeMeta(type = Scope.LOCAL)
	CURRENCY_DATA_HANDLER_CLASS("currency-data-handler-class"),

	@RuntimeMeta(type = Scope.LOCAL)
	CURRENCY_DATA_MAINTENANCE_HANDLER_CLASS("currency-data-maintenance-handler-class"),

	/**
     * login service
     */
    @RuntimeMeta(type = Scope.LOCAL)
    LOGIN_SERVICE_ENABLED("login-service-enabled"),

	@RuntimeMeta(type = Scope.LOCAL)
	LOGIN_HANDLER_CLASS("login-handler-class"),

	/**
     * pricelayer data service
     */
    @RuntimeMeta(type = Scope.LOCAL)
    PRICELAYER_DATA_SERVICE_ENABLED("pricelayer-data-service-enabled"),

	@RuntimeMeta(type = Scope.LOCAL)
	PRICELAYER_DATA_HANDLER_CLASS("pricelayer-data-handler-class"),

	@RuntimeMeta(type = Scope.LOCAL)
	PRICELAYER_DATA_MAINTENANCE_HANDLER_CLASS("pricelayer-data-maintenance-handler-class"),

	/**
     * site data service
     */
    @RuntimeMeta(type = Scope.LOCAL)
    SITE_DATA_SERVICE_ENABLED("site-data-service-enabled"),

	@RuntimeMeta(type = Scope.LOCAL)
	SITE_DATA_HANDLER_CLASS("site-data-handler-class"),

	@RuntimeMeta(type = Scope.LOCAL)
	SITE_DATA_MAINTENANCE_HANDLER_CLASS("site-data-maintenance-handler-class"),

    /**
     * app data service
     */
    @RuntimeMeta(type = Scope.LOCAL)
    SUPPLIER_DATA_SERVICE_ENABLED("supplier-data-service-enabled"),

	@RuntimeMeta(type = Scope.LOCAL)
	SUPPLIER_DATA_HANDLER_CLASS("supplier-data-handler-class"),

	@RuntimeMeta(type = Scope.LOCAL)
	SUPPLIER_DATA_MAINTENANCE_HANDLER_CLASS("supplier-data-maintenance-handler-class"),

	/**
	 * ad data service
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	BANNER_AD_DATA_SERVICE_ENABLED("banner-ad-data-service-enabled"),
	@RuntimeMeta(type = Scope.LOCAL)
	BANNER_AD_DATA_HANDLER_CLASS("banner-ad-data-handler-class"),
	@RuntimeMeta(type = Scope.LOCAL)
	BANNER_AD_DATA_MAINTENANCE_HANDLER_CLASS("banner-ad-data-maintenance-handler-class"),
	@RuntimeMeta(type = Scope.LOCAL)
	VIDEO_AD_DATA_SERVICE_ENABLED("video-ad-data-service-enabled"),
	@RuntimeMeta(type = Scope.LOCAL)
	VIDEO_AD_DATA_HANDLER_CLASS("video-ad-data-handler-class"),
	@RuntimeMeta(type = Scope.LOCAL)
	VIDEO_AD_DATA_MAINTENANCE_HANDLER_CLASS("video-ad-data-maintenance-handler-class");

	//###########################################################

	private String value;

	private ContextProperties(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static ContextProperties get(final String value) {
		for (final ContextProperties prop : values()) {
			if (prop.getValue().equals(value)) {
				return prop;
			}
		}
		return null;
	}

}
