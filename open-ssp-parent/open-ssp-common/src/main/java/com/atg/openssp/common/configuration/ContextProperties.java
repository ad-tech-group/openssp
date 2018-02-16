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
	 * data-privder-url
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	DATA_PROVIDER_SCHEME("data-provider-scheme"),

	/**
	 * data-privder-url
	 */
	@RuntimeMeta(type = Scope.GLOBAL)
	DATA_PROVIDER_URL("data-provider-url"),

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
	SSPCHANNEL("ssp");

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
