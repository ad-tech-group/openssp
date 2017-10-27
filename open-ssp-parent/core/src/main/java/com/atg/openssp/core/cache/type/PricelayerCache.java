package com.atg.openssp.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import com.atg.openssp.core.cache.broker.context.Pricelayer;

/**
 * @author André Schmer
 *
 */
public final class PricelayerCache extends MapCache<String, Pricelayer> {

	public static final PricelayerCache instance = new PricelayerCache();

	private PricelayerCache() {
		super();
	}

}
