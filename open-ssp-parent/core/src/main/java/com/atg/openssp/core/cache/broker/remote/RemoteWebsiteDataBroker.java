package com.atg.openssp.core.cache.broker.remote;

import java.util.Arrays;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.dto.Website;
import com.atg.openssp.common.dto.Zone;
import com.atg.openssp.common.exception.EmptyHostException;
import com.atg.openssp.core.cache.type.WebsiteDataCache;
import com.atg.openssp.core.cache.type.ZoneDataCache;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import restful.context.PathBuilder;
import restful.exception.RestException;

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
		try {
			final String jsonString = super.connect();
			final Website[] data = gson.fromJson(jsonString, Website[].class);
			if (data != null && data.length > 0) {
				log.info(this.getClass().getSimpleName() + " sizeof Website data=" + data.length);
				Arrays.stream(data).forEach(c -> WebsiteDataCache.instance.put(c.getWebsiteId(), c));

				Arrays.stream(data).forEach(c -> Arrays.stream(c.getZones()).forEach(new Consumer<Zone>() {
					@Override
					public void accept(final Zone zone) {
						zone.setWebsiteId(c.getWebsiteId());
						ZoneDataCache.instance.put(zone.getZoneId(), zone);
					}
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
