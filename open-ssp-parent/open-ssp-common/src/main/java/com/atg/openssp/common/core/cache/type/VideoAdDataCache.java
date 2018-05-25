package com.atg.openssp.common.core.cache.type;

import com.atg.openssp.common.cache.MapCache;
import com.atg.openssp.common.cache.dto.VideoAd;

/**
 * @author André Schmer
 *
 */
public final class VideoAdDataCache extends MapCache<String, VideoAd> {

	public static final VideoAdDataCache instance = new VideoAdDataCache();

	private VideoAdDataCache() {
		super();
	}

}
