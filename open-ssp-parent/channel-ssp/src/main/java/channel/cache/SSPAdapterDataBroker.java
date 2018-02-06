package channel.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.cache.broker.AbstractDataBroker;
import com.atg.openssp.common.exception.EmptyHostException;

import channel.ssp.SSPAdapterCache;
import channel.ssp.SSPBroker;
import restful.context.Path;
import restful.context.PathBuilder;
import restful.exception.RestException;

/**
 * Act as broker between connector which loads the data from the webservice into a data transfer object and the cache.
 * 
 * @author André Schmer
 *
 */
public final class SSPAdapterDataBroker extends AbstractDataBroker<SSPAdapterDto> {

	private static final Logger log = LoggerFactory.getLogger(SSPAdapterDataBroker.class);

	public SSPAdapterDataBroker() {}

	@Override
	public boolean doCaching() {
		try {
			final SSPAdapterDto dto = super.connect(SSPAdapterDto.class);
			if (dto != null) {
				log.debug("sizeof sspadapter data= {}", dto.getData().size());
				dto.getData().forEach(adapter -> {
					final SSPBroker broker = new SSPBroker(adapter);
					SSPAdapterCache.INSTANCE.add(broker);
				});

				return true;
			}
			log.error("dto is null");
		} catch (final RestException | EmptyHostException e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public PathBuilder getRestfulContext() {
		return getDefaulPathBuilder().addPath(Path.CORE).addPath(Path.SSP_ADAPTER);
	}

	@Override
	protected void finalWork() {
		SSPAdapterCache.INSTANCE.switchCache();
	}

}
