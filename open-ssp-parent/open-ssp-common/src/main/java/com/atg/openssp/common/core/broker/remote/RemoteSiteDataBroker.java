package com.atg.openssp.common.core.broker.remote;

import com.atg.openssp.common.cache.broker.AbstractDataBroker;
import com.atg.openssp.common.core.broker.dto.SiteDto;
import com.atg.openssp.common.core.cache.type.SiteDataCache;
import com.atg.openssp.common.exception.EmptyHostException;
import com.atg.openssp.common.logadapter.DataBrokerLogProcessor;
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
public final class RemoteSiteDataBroker extends AbstractDataBroker<SiteDto> {

	private static final Logger log = LoggerFactory.getLogger(RemoteSiteDataBroker.class);

	public RemoteSiteDataBroker() {}

	@Override
	public boolean doCaching() {
		long startTS = System.currentTimeMillis();
		try {
			final SiteDto dto = super.connect(SiteDto.class);
			if (dto != null) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("SiteData", dto.getSites().size(), startTS, endTS, endTS-startTS);
				log.debug("sizeof Site data=" + dto.getSites().size());
				dto.getSites().forEach(site -> {
					SiteDataCache.instance.put(site.getId(), site);
				});
				return true;
			}

			log.error("no Site data");
		} catch (final RestException | EmptyHostException e) {
			log.error(getClass() + ", " + e.getMessage(), e);
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() {
		return getDefaulPathBuilder().addPath(Path.CORE).addPath(Path.SITE);
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		SiteDataCache.instance.switchCache();
	}

}
