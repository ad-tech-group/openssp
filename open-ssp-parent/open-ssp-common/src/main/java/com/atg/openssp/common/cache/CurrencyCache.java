package com.atg.openssp.common.cache;

/**
 * Cache implementation for storing currency rates for EUR as base.
 * 
 * @author André Schmer
 *
 */
public final class CurrencyCache extends MapCache<String, Float> {

	public static final CurrencyCache instance = new CurrencyCache();
	private String currency;

	private CurrencyCache() {
		super();
	}

	@Override
	public Float get(final String key) {
		if (key != null && wcache.containsKey(key)) {
			return wcache.get(key) != 0.0f ? wcache.get(key) : 1.0f;
		}
		return 1.0f;
	}

	public String getBaseCurrency() {
		return currency;
	}

	public void setBaseCurrency(String currency) {
		this.currency = currency;
	}
}
