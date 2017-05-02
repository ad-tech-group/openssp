package channel.ssp;

import com.atg.openssp.common.cache.ListCache;

/**
 * @author André Schmer
 *
 */
public final class SSPAdapterCache extends ListCache<SSPBroker> {

	public static final SSPAdapterCache INSTANCE = new SSPAdapterCache();

	private SSPAdapterCache() {
		super();
	}

}
