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
	 * data-privder-path
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_PROVIDER_PATH("adserver-provider-path"),

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
	VALIDATOR_HANDLER_CLASS("validator-handler-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	ADSERVER_BROKER_HANDLER_CLASS("adserver-broker-handler-class"),

	@RuntimeMeta(type = Scope.GLOBAL)
	BUILD_REQUEST_BUILDER_HANDLER_CLASS("bid-request-builder-handler-class");


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
