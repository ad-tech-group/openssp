package com.atg.openssp.common.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import openrtb.bidrequest.model.App;

/**
 * @author André Schmer
 *
 */
public final class AppDataCache extends MapCache<String, App> {

	public static final AppDataCache instance = new AppDataCache();

	private AppDataCache() {
		super();
	}

}
