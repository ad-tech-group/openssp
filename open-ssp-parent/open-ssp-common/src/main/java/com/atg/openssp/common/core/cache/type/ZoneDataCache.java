package com.atg.openssp.common.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import com.atg.openssp.common.cache.dto.Zone;

/**
 * @author André Schmer
 *
 */
public final class ZoneDataCache extends MapCache<Integer, Zone> {

	public static final ZoneDataCache instance = new ZoneDataCache();

	private ZoneDataCache() {
		super();
	}

}
