package com.atg.openssp.core.cache.broker.remote;

import java.util.Arrays;
import java.util.function.Consumer;

import com.atg.openssp.common.dto.Website;
import com.atg.openssp.common.dto.Zone;
import com.atg.openssp.core.cache.type.WebsiteDataCache;
import com.atg.openssp.core.cache.type.ZoneDataCache;
import com.atg.service.LogFacade;
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
				LogFacade.logSystemInfo(this.getClass().getSimpleName() + " [REMOTE] sizeof Website data=" + data.length);
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
			LogFacade.logException(this.getClass(), " no data");
		} catch (final JsonSyntaxException e) {
			LogFacade.logException(this.getClass(), "JsonSyntaxException: " + e.getMessage());
		} catch (final RestException e) {
			LogFacade.logException(this.getClass(), "RestException: " + e.getMessage());
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() {
		return getDefaulPathBuilder().addParam("websites", "1");
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		WebsiteDataCache.instance.switchCache();
		ZoneDataCache.instance.switchCache();
	}

}
