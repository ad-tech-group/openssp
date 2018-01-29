package com.atg.openssp.common.configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache for System Properties.
 * 
 * @author André Schmer
 * 
 */
public final class ContextCache {

	public static final ContextCache instance = new ContextCache();

	private final Map<ContextProperties, String> cache;

	private ContextCache() {
		cache = new ConcurrentHashMap<>();
	}

	public String get(final ContextProperties key) {
		return cache.get(key);
	}

	public void put(final ContextProperties key, final String value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		if (value != null) {
			cache.put(key, value);
		}
	}

	public String remove(final ContextProperties key) {
		try {
			return cache.remove(key);
		} catch (final NullPointerException ignore) {}
		return null;
	}

	public boolean isEmpty() {
		return cache.isEmpty();
	}

	public int size() {
		return cache.size();
	}

	public Map<ContextProperties, String> getAll() {
		return new ConcurrentHashMap<>(cache);
	}

	public void clear() {
		cache.clear();
	}

}
