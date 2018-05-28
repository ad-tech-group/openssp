package com.atg.openssp.common.core.broker.remote;

import com.atg.openssp.common.cache.broker.AbstractAdDataBroker;
import com.atg.openssp.common.configuration.ContextCache;
import com.atg.openssp.common.configuration.ContextProperties;
import com.atg.openssp.common.core.broker.dto.VideoAdDto;
import com.atg.openssp.common.core.cache.type.VideoAdDataCache;
import com.atg.openssp.common.exception.EmptyHostException;
import com.atg.openssp.common.logadapter.DataBrokerLogProcessor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restful.context.Path;
import restful.context.PathBuilder;
import restful.exception.RestException;

import java.util.Arrays;

/**
 * Act as broker between connector which loads the data from the webservice into a data transfer object and the cache.
 * 
 * This special data broker loads the {@see VideoAd} data from a backend which holds the data for the websites. It uses a {@see PathBuilder} object to store
 * information about the endpoint which is used by the {@see AbstractRemoteDataProvider} to connect to the remote.
 * 
 * @author André Schmer
 *
 */
public final class RemoteVideoadDataBroker extends AbstractAdDataBroker<VideoAdDto> {

	private static final Logger log = LoggerFactory.getLogger(RemoteVideoadDataBroker.class);

	public RemoteVideoadDataBroker() {}

    @Override
    protected PathBuilder getDefaulPathBuilder() {
        final PathBuilder pathBuilder = super.getDefaulPathBuilder();
        pathBuilder.addPath(Path.ADS_CORE);
        pathBuilder.addPath(Path.VIDEO_ADS);
        return pathBuilder;
    }

	@Override
	public boolean doCaching() {
		long startTS = System.currentTimeMillis();
		try {
			final VideoAdDto dto = super.connect(VideoAdDto.class);
            if (dto != null) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("VideoAdData", dto.getVideoAds().size(), startTS, endTS, endTS-startTS);
				log.debug("sizeof VideoAd data=" + dto.getVideoAds().size());
				dto.getVideoAds().forEach(ad -> {
					VideoAdDataCache.instance.put(ad.getId(), ad);
				});
				return true;
			}
			log.error("no VideoAd data");
		} catch (final JsonSyntaxException | RestException | EmptyHostException e) {
            log.error(getClass() + ", " + e.getMessage(), e);
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() throws EmptyHostException {
		return getDefaulPathBuilder();
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		VideoAdDataCache.instance.switchCache();
	}

}
