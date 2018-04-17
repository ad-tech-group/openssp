package com.atg.openssp.common.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import com.atg.openssp.common.cache.dto.Website;

/**
 * @author André Schmer
 *
 */
public final class WebsiteDataCache extends MapCache<Integer, Website> {

	public static final WebsiteDataCache instance = new WebsiteDataCache();

	private WebsiteDataCache() {
		super();
	}

}
