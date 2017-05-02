package com.atg.openssp.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import com.atg.openssp.common.dto.Website;

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
