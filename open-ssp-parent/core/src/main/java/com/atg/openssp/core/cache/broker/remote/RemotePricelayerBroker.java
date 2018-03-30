package com.atg.openssp.core.cache.broker.remote;

import com.atg.openssp.common.cache.broker.AbstractDataBroker;
import com.atg.openssp.common.exception.EmptyHostException;
import com.atg.openssp.core.cache.broker.dto.PricelayerDto;
import com.atg.openssp.core.cache.type.PricelayerCache;
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
public final class RemotePricelayerBroker extends AbstractDataBroker<PricelayerDto> {

	private static final Logger log = LoggerFactory.getLogger(RemotePricelayerBroker.class);

	public RemotePricelayerBroker() {}

	@Override
	public boolean doCaching() {
		try {
			final PricelayerDto dto = super.connect(PricelayerDto.class);
			if (dto != null) {
				log.info("sizeof pricelayer data=" + dto.getPricelayer().size());
				dto.getPricelayer().forEach(pricelayer -> {
					PricelayerCache.instance.put(pricelayer.getSiteid(), pricelayer);
				});
				return true;
			}

			log.error("no price data");
		} catch (final RestException | EmptyHostException e) {
			log.error(getClass() + ", " + e.getMessage());
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() throws EmptyHostException {
		return getDefaulPathBuilder().addPath(Path.CORE).addPath(Path.PRICELAYER);
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		PricelayerCache.instance.switchCache();
	}

}
