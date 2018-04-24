package com.atg.openssp.common.core.broker.remote;

import com.atg.openssp.common.cache.dto.Website;
import com.atg.openssp.common.core.cache.type.WebsiteDataCache;
import com.atg.openssp.common.core.cache.type.ZoneDataCache;
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
 * This special data broker loads the {@see Website} data from a backend which holds the data for the websites. It uses a {@see PathBuilder} object to store
 * information about the endpoint which is used by the {@see AbstractRemoteDataProvider} to connect to the remote.
 * 
 * @author André Schmer
 *
 */
public final class RemoteWebsiteDataBroker extends AbstractRemoteDataProvider {

	private static final Logger log = LoggerFactory.getLogger(RemoteWebsiteDataBroker.class);

	final private Gson gson;

	public RemoteWebsiteDataBroker() {
		gson = new Gson();
	}

	@Override
	public boolean doCaching() {
		long startTS = System.currentTimeMillis();
		try {
			final String jsonString = super.connect();
			final Website[] data = gson.fromJson(jsonString, Website[].class);
			if (data != null && data.length > 0) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("WebsiteData", data.length, startTS, endTS, endTS-startTS);
				log.debug(this.getClass().getSimpleName() + " sizeof Website data=" + data.length);
				Arrays.stream(data).forEach(c -> WebsiteDataCache.instance.put(c.getWebsiteId(), c));

				Arrays.stream(data).forEach(c -> Arrays.stream(c.getZones()).forEach(zone -> {
					zone.setWebsiteId(c.getWebsiteId());
					ZoneDataCache.instance.put(zone.getZoneId(), zone);
				}));
				return true;
			}
			log.error("no Website data");
		} catch (final JsonSyntaxException | RestException | EmptyHostException e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() throws EmptyHostException {
		return getDefaulPathBuilder().addParam("websites", "1");
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		WebsiteDataCache.instance.switchCache();
		ZoneDataCache.instance.switchCache();
	}

}
