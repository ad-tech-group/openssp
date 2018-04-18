package com.atg.openssp.common.core.broker.remote;

import com.atg.openssp.common.cache.broker.AbstractDataBroker;
import com.atg.openssp.common.core.broker.dto.SupplierDto;
import com.atg.openssp.common.core.cache.type.ConnectorCache;
import com.atg.openssp.common.core.exchange.channel.rtb.OpenRtbConnector;
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
 * This special data broker loads the {@see SupplierDto} data from a backend which holds the data for the supplier, also known as DSP. and put into a special
 * cache reps. key-value store {@see ConnectorCache}. It uses a {@see PathBuilder} object to store information about the endpoint which is used by the generic
 * {@see AbstractDataBroker} to connect to the remote.
 * 
 * @author André Schmer
 *
 */
public final class RemoteSupplierDataBroker extends AbstractDataBroker<SupplierDto> {

	private static final Logger log = LoggerFactory.getLogger(RemoteSupplierDataBroker.class);

	public RemoteSupplierDataBroker() {}

	/**
	 * Do this for example if you wish to load supplier data from an external service like a REST webservice.
	 */
	@Override
	public boolean doCaching() {
		long startTS = System.currentTimeMillis();
		try {
			final SupplierDto dto = super.connect(SupplierDto.class);
			if (dto != null) {
				long endTS = System.currentTimeMillis();
				DataBrokerLogProcessor.instance.setLogData("SupplierData", dto.getSupplier().size(), startTS, endTS, endTS-startTS);
				log.debug("sizeof supplier data=" + dto.getSupplier().size());
				dto.getSupplier().forEach(supplier -> {
					final OpenRtbConnector openRtbConnector = new OpenRtbConnector(supplier);
					ConnectorCache.instance.add(openRtbConnector);
				});
				return true;
			}
			log.error("no Supplier data");
		} catch (final RestException | EmptyHostException e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() {
		return getDefaulPathBuilder().addPath(Path.CORE).addPath(Path.SUPPLIER);
	}

	@Override
	protected void finalWork() {
		// need to switch the intermediate cache to make the data available
		ConnectorCache.instance.switchCache();
	}

}
