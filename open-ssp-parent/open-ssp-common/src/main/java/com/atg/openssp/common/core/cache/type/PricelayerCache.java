package com.atg.openssp.common.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import openrtb.bidrequest.model.Pricelayer;

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
