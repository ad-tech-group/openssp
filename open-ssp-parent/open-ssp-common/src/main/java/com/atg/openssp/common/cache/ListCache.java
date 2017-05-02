package com.atg.openssp.common.cache;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.atg.openssp.common.configuration.Context;

/**
 * @author André Schmer
 *
 */
public class ListCache<T> implements DynamicCache {

	// the working cache. holds the actual data for working
	protected List<T> wcache;
	// the temporarily cache, to serve as for updating the cache
	private final List<T> tcache;

	// reference holder for working cache
	private final List<T> b;
	private final List<T> a;

	private boolean ab = true;

	private String updateTime;

	public ListCache() {
		this.tcache = new CopyOnWriteArrayList<>();
		this.wcache = new CopyOnWriteArrayList<>();
		this.b = new CopyOnWriteArrayList<>();
		this.a = new CopyOnWriteArrayList<>();
	}

	public final void add(final T key) {
		this.tcache.add(key);
	}

	/**
	 * Creates a new copy of this cache.
	 * 
	 * @return
	 */
	public final List<T> getAll() {
		return new CopyOnWriteArrayList<>(this.wcache);
	}

	public final boolean contains(final T key) {
		return this.wcache.contains(key);
	}

	@Override
	public final void switchCache() {
		if (!this.tcache.isEmpty()) {
			if (this.ab) {
				this.a.addAll(this.tcache);
				this.wcache = this.a;
				this.b.clear();
			} else {
				this.b.addAll(this.tcache);
				this.wcache = this.b;
				this.a.clear();
			}
			this.ab = !this.ab;
			this.tcache.clear();
			this.updateTime = LocalDateTime.now().format(Context.dateFormatter);
		}
	}

	public String getUpdateTime() {
		return this.updateTime;
	}

}
