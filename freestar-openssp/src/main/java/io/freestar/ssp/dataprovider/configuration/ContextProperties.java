package io.freestar.ssp.dataprovider.configuration;

import com.atg.openssp.common.annotation.RuntimeMeta;
import com.atg.openssp.common.annotation.Scope;

/**
 * @author André Schmer
 * 
 */
public enum ContextProperties {

	/**
	 * debug
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	DEBUG("debug"),

	/**
	 * verbose
	 */
	@RuntimeMeta(type = Scope.LOCAL)
	VERBOSE("verbose"),

	@RuntimeMeta(type = Scope.LOCAL)
	APP_DATA_SERVICE("app-data-service"),

	@RuntimeMeta(type = Scope.LOCAL)
	CURRENCY_DATA_SERVICE("currency-data-service"),

	@RuntimeMeta(type = Scope.LOCAL)
	LOGIN_SERVICE("login-service"),

	@RuntimeMeta(type = Scope.LOCAL)
	PRICELAYER_DATA_SERVICE("pricelayer-data-service"),

	@RuntimeMeta(type = Scope.LOCAL)
	SITE_DATA_SERVICE("site-data-service"),

	@RuntimeMeta(type = Scope.LOCAL)
	SUPPLIER_DATA_SERVICE("supplier-data-service");

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
