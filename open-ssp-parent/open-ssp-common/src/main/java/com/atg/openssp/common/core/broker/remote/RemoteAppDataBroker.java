package com.atg.openssp.common.core.broker.remote;

import com.atg.openssp.common.cache.broker.AbstractDataBroker;
import com.atg.openssp.common.core.broker.dto.AppDto;
import com.atg.openssp.common.core.cache.type.AppDataCache;
import com.atg.openssp.common.exception.EmptyHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restful.context.Path;
import restful.context.PathBuilder;
import restful.exception.RestException;

/**
 * Act as broker between connector which loads the data from the webservice into a data transfer object and the cache.
 * 
 * This special data-broker loads the currency rate, see {@see CurrencyDto}, informations from the central webservice into a cache. It uses a {@see PathBuilder}
 * object to store information about the endpoint which is used by the generic {@see AbstractDataBroker} to connect to the remote.
 * 
 * @author André Schmer
 *
 */
public final class RemoteAppDataBroker extends AbstractDataBroker<AppDto> {

	private static final Logger log = LoggerFactory.getLogger(RemoteAppDataBroker.class);

	public RemoteAppDataBroker() {}

	@Override
	public boolean doCaching() {
		try {
			final AppDto dto = super.connect(AppDto.class);
			if (dto != null) {
				log.debug("sizeof App data=" + dto.getApps().size());
				dto.getApps().forEach(app -> {
					AppDataCache.instance.put(app.getId(), app);
				});
				return true;
			}

			log.error("no App data");
		} catch (final RestException | EmptyHostException e) {
			log.error(getClass() + ", " + e.getMessage());
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() {
		return getDefaulPathBuilder().addPath(Path.CORE).addPath(Path.APP);
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		AppDataCache.instance.switchCache();
	}

}
