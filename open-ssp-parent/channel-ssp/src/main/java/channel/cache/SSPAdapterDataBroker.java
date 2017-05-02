package channel.cache;

import java.util.function.Consumer;

import com.atg.openssp.common.cache.broker.AbstractDataBroker;
import com.atg.service.LogFacade;

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

	public SSPAdapterDataBroker() {}

	@Override
	public boolean doCaching() {
		try {
			final SSPAdapterDto dto = super.connect(SSPAdapterDto.class);
			if (dto != null) {
				LogFacade.logDebug("sizeof sspadapter data=" + dto.getData().size());

				dto.getData().forEach(new Consumer<BasicAdapter>() {
					@Override
					public void accept(final BasicAdapter adapter) {
						final SSPBroker broker = new SSPBroker(adapter);
						SSPAdapterCache.INSTANCE.add(broker);
					}
				});
				return true;
			}
			LogFacade.logException(this.getClass(), " dto is null");
		} catch (final RestException e) {
			LogFacade.logException(this.getClass(), "RestException: " + e.getMessage());
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
