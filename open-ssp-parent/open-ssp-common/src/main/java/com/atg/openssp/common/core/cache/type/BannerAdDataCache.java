package com.atg.openssp.common.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import com.atg.openssp.common.cache.dto.BannerAd;

/**
 * @author André Schmer
 *
 */
public final class BannerAdDataCache extends MapCache<String, BannerAd> {

	public static final BannerAdDataCache instance = new BannerAdDataCache();

	private BannerAdDataCache() {
		super();
	}

}
