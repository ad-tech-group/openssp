package channel.ssp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.ResponseContainer;
import com.atg.openssp.common.provider.AdProviderReader;

/**
 * @author André Schmer
 *
 */
public class SSPService implements Callable<AdProviderReader> {

	private static final Logger log = LoggerFactory.getLogger(SSPService.class);

	private final SessionAgent agent;

	/**
	 * 
	 * @param agent
	 *            {@link SessionAgent}
	 */
	public SSPService(final SessionAgent agent) {
		this.agent = agent;
	}

	/**
	 * Calls the SSP via custom Adapter.
	 * 
	 * @return {@link AdProviderReader}
	 */
	@Override
	public AdProviderReader call() throws Exception {
		final List<SSPBroker> adapterList = SSPAdapterCache.INSTANCE.getAll();

		final List<ResponseContainer> responseList = new ArrayList<>();

		adapterList.parallelStream().forEach(broker -> {
			broker.setSessionAgent(agent);
			try {
				final ResponseContainer resp = broker.call();
				responseList.add(resp);
			} catch (final Exception e) {
				log.error(e.getMessage(), e);
			}
		});

		for (final ResponseContainer responseContainer : responseList) {
			responseContainer.getBidResponse();
		}

		// TODO: check if necessary to laad some specials in here or move this
		// adapterList.forEach(broker -> {
		// // final Impression imp = bidRequest.getImp().get(0);
		// // if (agent.getParamValues().getVideoad().getBidfloorPrice() > 0) {
		// // // floorprice in EUR -> multiply with rate to get target
		// // // currency cause floorprice currency is always the same
		// // // as supplier currency
		// //
		// imp.setBidfloor(agent.getParamValues().getVideoad().getBidfloorPrice()
		// // * CurrencyCache.INSTANCE.get(broker.getAdapter().getCurrency()));
		// // imp.setBidfloorcur(broker.getAdapter().getCurrency());
		// // }
		// broker.setSessionAgent(agent);
		// final ResponseContainer resp = broker.call();
		//
		// // log.info("eval bidrequest: " + new
		// // Gson().toJson(bidRequest));
		// // TODO: were to store???
		// // this.agent.getBidExchange().setBidRequest(broker.getSupplier(),
		// // bidRequest.getBuilder());
		// });

		return null;
	}

}
