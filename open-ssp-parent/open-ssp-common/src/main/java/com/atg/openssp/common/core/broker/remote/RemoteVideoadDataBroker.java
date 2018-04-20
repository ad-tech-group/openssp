package com.atg.openssp.common.core.broker.remote;

import com.atg.openssp.common.cache.dto.VideoAd;
import com.atg.openssp.common.core.cache.type.VideoAdDataCache;
import com.atg.openssp.common.exception.EmptyHostException;
import com.atg.openssp.common.logadapter.DataBrokerLogProcessor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public final class RemoteVideoadDataBroker extends AbstractRemoteDataProvider {

	private static final Logger log = LoggerFactory.getLogger(RemoteVideoadDataBroker.class);

	final private Gson gson;

	public RemoteVideoadDataBroker() {
		gson = new Gson();
	}

	@Override
	public boolean doCaching() {
		long startTS = System.currentTimeMillis();
		try {
			final String jsonString = super.connect();
			final VideoAd[] data = gson.fromJson(jsonString, VideoAd[].class);
			if (data != null && data.length > 0) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("VideoAdData", data.length, startTS, endTS, endTS-startTS);
				log.debug(this.getClass().getSimpleName() + " sizeof VideoAd data=" + data.length);
				Arrays.stream(data).forEach(c -> VideoAdDataCache.instance.put(c.getVideoadId(), c));
				return true;
			}
			log.error("no VideoAd data");
		} catch (final JsonSyntaxException | RestException | EmptyHostException e) {
			log.error(e.getMessage());
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
