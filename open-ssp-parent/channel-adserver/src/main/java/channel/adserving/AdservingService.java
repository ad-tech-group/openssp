package channel.adserving;

import java.util.Optional;
import java.util.concurrent.Callable;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.provider.AdProviderReader;
import util.math.FloatComparator;

/**
 * @author André Schmer
 *
 */
public class AdservingService implements Callable<AdProviderReader> {

	private final AdserverBroker broker;

	private final SessionAgent agent;

	/**
	 * 
	 * @param agent
	 *            {@link SessionAgent}
	 */
	public AdservingService(final SessionAgent agent) {
		this.agent = agent;
		broker = new AdserverBroker();
		broker.setSessionAgent(agent);
	}

	/**
	 * Calls the Broker for Adserver.
	 * 
	 * @return {@link AdProviderReader}
	 */
	@Override
	public AdProviderReader call() throws Exception {
		final Optional<AdProviderReader> adProvider = broker.call();

		if (adProvider.isPresent()) {
			final AdProviderReader provider = adProvider.get();
			// check if the ad response price is greator or equal the floorprice
//			if (FloatComparator.greaterOrEqual(provider.getExchangedCurrencyPrice(), agent.getParamValues().getVideoad().getBidfloorPrice())) {
//			 return provider;
//			 }
		}

		return null;
	}

}
