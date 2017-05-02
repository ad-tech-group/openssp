package com.atg.openssp.common.cache;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.atg.openssp.common.configuration.Context;
import com.google.common.base.Preconditions;

import common.EmptyCacheException;

/**
 * @author André Schmer
 *
 */
public class MapCache<K, V> implements DynamicCache {

	protected Map<K, V> wcache;
	private final Map<K, V> tcache;
	private final Map<K, V> b;
	private final Map<K, V> a;

	private boolean ab = true;

	private String updateTime;

	public MapCache() {
		this.tcache = new ConcurrentHashMap<>();
		this.wcache = new ConcurrentHashMap<>();
		this.b = new ConcurrentHashMap<>();
		this.a = new ConcurrentHashMap<>();
	}

	public V get(final K key) throws EmptyCacheException {
		try {
			Preconditions.checkNotNull(key, this.getClass().getSimpleName() + ": invalid argument [key is null]");
			Preconditions.checkState(this.wcache.containsKey(key), this.getClass().getName() + " cache empty for " + key);
		} catch (final Exception e) {
			throw new EmptyCacheException(e.getMessage());
		}
		return this.wcache.get(key);
	}

	public final void put(final K key, final V value) {
		this.tcache.put(key, value);
	}

	public final Map<K, V> getAll() {
		return new ConcurrentHashMap<>(wcache);
	}

	public final boolean contains(final K key) {
		return this.wcache.containsKey(key);
	}

	@Override
	public final void switchCache() {
		if (!this.tcache.isEmpty()) {
			if (ab) {
				this.a.putAll(this.tcache);
				this.wcache = this.a;
				this.b.clear();
			} else {
				this.b.putAll(this.tcache);
				this.wcache = this.b;
				this.a.clear();
			}
			ab = !ab;
			this.tcache.clear();
			this.updateTime = LocalDateTime.now(Context.zoneId).format(Context.dateTimeFormatter);
		}
	}

	public final void clear() {
		wcache.clear();
	}

	public String getUpdateTime() {
		return updateTime;
	}

}
