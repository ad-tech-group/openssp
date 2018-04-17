package com.atg.openssp.common.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import openrtb.bidrequest.model.Site;

/**
 * @author André Schmer
 *
 */
public final class SiteDataCache extends MapCache<String, Site> {

	public static final SiteDataCache instance = new SiteDataCache();

	private SiteDataCache() {
		super();
	}

}
