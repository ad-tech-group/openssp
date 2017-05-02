package com.atg.openssp.core.cache.broker.context;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.cache.broker.AbstractDataBroker;
import com.atg.service.LogFacade;

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
public final class CurrencyDataBroker extends AbstractDataBroker<CurrencyDto> {

	public CurrencyDataBroker() {}

	@Override
	public boolean doCaching() {
		try {
			final CurrencyDto dto = super.connect(CurrencyDto.class);
			if (dto != null) {
				LogFacade.logSystemInfo("sizeof Currency data=" + dto.getData().size());
				dto.getData().forEach(c -> CurrencyCache.instance.put(c.getCurrency(), c.getRate()));
				return true;
			}
			LogFacade.logException(getClass(), " no data");
		} catch (final RestException e) {
			LogFacade.logException(this.getClass(), e.getMessage());
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() {
		return getDefaulPathBuilder().addPath(Path.CORE).addPath(Path.EUR_REF);
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		CurrencyCache.instance.switchCache();
	}

}
