package com.atg.openssp.common.core.exchange.channel.rtb;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.core.cache.type.ConnectorCache;
import com.atg.openssp.common.core.exchange.Auction;
import com.atg.openssp.common.core.exchange.BidRequestBuilder;
import com.atg.openssp.common.core.exchange.RequestSessionAgent;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.ResponseContainer;
import com.atg.openssp.common.demand.SupplierAdPlatform;
import com.atg.openssp.common.exception.InvalidBidException;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.common.provider.AdProviderReader;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.Impression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author André Schmer
 *
 */
public class DemandService implements Callable<AdProviderReader> {

	private static final Logger log = LoggerFactory.getLogger(DemandService.class);

	private final RequestSessionAgent agent;

	/**
	 * 
	 * @param {@link
	 *            SessionAgent}
	 */
	public DemandService(final RequestSessionAgent agent) {
		this.agent = agent;
	}

	/**
	 * Calls the DSP. Collects the results of bidrequest, storing the results after validating into a {@link BidExchange} object.
	 * 
	 * <p>
	 * Principle of work is the following:
	 * <ul>
	 * <li>Loads the connectors as callables from the cache {@link DemandBroker}</li>
	 * <li>Invoke the callables due to the {@link DemandExecutorServiceFacade}</li>
	 * <li>For every result in the list of futures, the response will be validated and stored in a {@link BidExchange} object</li>
	 * <li>From the set of reponses in the {@link BidExchange} a bidding winner will be calculated in the Auction service {@link Auction}</li>
	 * </ul>
	 * <p>
	 * 
	 * @return {@link AdProviderReader}
	 * @throws Exception
	 */
	@Override
	public AdProviderReader call() throws Exception {
		AdProviderReader adProvider = null;
		try {
			final List<DemandBroker> connectors = loadSupplierConnectors();
			final List<Future<ResponseContainer>> futures = DemandExecutorServiceFacade.instance.invokeAll(connectors);

			futures.parallelStream().filter(Objects::nonNull).forEach(future -> {
				try {
					final ResponseContainer responseContainer = future.get();

					// final boolean valid = OpenRtbVideoValidator.instance.validate(agent.getBidExchange().getBidRequest(responseContainer.getSupplier()),
					// responseContainer
					// .getBidResponse());
					//
					// if (false == valid) {
					// LogFacade.logException(this.getClass(), ExceptionCode.E003, agent.getRequestid(), responseContainer.getBidResponse().toString());
					// return;// important!
					// }
					if (responseContainer != null) {
						agent.getBidExchange().setBidResponse(responseContainer.getSupplier(), responseContainer.getBidResponse());
					}
				} catch (final ExecutionException e) {
				    e.printStackTrace();
					log.error("ExecutionException {} {}", agent.getRequestid(), e);
				} catch (final InterruptedException e) {
					log.error("InterruptedException {} {}", agent.getRequestid(), e.getMessage());
				} catch (final CancellationException e) {
					log.error("CancellationException {} {}", agent.getRequestid(), e.getMessage());
				}
			});

			try {
				adProvider = Auction.auctioneer(agent.getBiddingServiceInfo(), agent.getBidExchange());
			} catch (final ArrayIndexOutOfBoundsException e) {
				log.info("No DSP points available.", agent.getRequestid(), e);
			} catch (final InvalidBidException e) {
				log.info("{} {}", agent.getRequestid(), e.getMessage());
			}
		} catch (final InterruptedException e) {
			log.error(" InterruptedException (outer) {} {}", agent.getRequestid(), e.getMessage());
		}

		return adProvider;
	}

	/**
	 * Loads the connectors for supplier from the cache.
	 * <p>
	 * Therefore it prepares the {@link BidRequest} for every connector, which is a representant to a demand connection.
	 * 
	 * @return a {@code List} with {@link DemandBroker}
	 * 
	 * @link SessionAgent
	 */
	private List<DemandBroker> loadSupplierConnectors() throws RequestException {
		final List<OpenRtbConnector> connectorList = ConnectorCache.instance.getAll();
		final List<DemandBroker> connectors = new ArrayList<>();

		final BidRequest bidRequest = BidRequestBuilder.getInstance().build(agent);
		boolean isMobile = bidRequest.getDevice().getUa().contains("Mobi");
        log.info(bidRequest.getDevice().getUa());
		log.info("is Mobile: "+isMobile);

		connectorList.stream().filter(b -> b.getSupplier().getActive().getValue() == 1).forEach(connector -> {

			boolean processingIsOK = false;
            if (connector.getSupplier().getAllowedAdPlatforms().size() == 0) {
                processingIsOK = true;
            } else if (
                    isMobile && connector.getSupplier().getAllowedAdPlatforms().contains(SupplierAdPlatform.MOBILE) ||
                    !isMobile && connector.getSupplier().getAllowedAdPlatforms().contains(SupplierAdPlatform.DESKTOP)
                    ) {
                processingIsOK = true;
            }

			if (processingIsOK) {
                log.info("keeping: "+connector.getSupplier().getShortName());
				if (connector.getSupplier().getTmax() != null) {
					bidRequest.setTmax(connector.getSupplier().getTmax());
				}
				final DemandBroker demandBroker = new DemandBroker(agent.getBiddingServiceInfo(), connector.getSupplier(), connector, agent);
				if (bidRequest.getImp().get(0).getBidfloor() > 0) {
					final Impression imp = bidRequest.getImp().get(0);
					// floorprice in EUR -> multiply with rate to get target
					// currency therfore floorprice currency is always the same
					// as supplier currency
					imp.setBidfloor(bidRequest.getImp().get(0).getBidfloor() * CurrencyCache.instance.get(connector.getSupplier().getCurrency()));
					imp.setBidfloorcur(connector.getSupplier().getCurrency());
				}

				bidRequest.setTest(connector.getSupplier().getUnderTest());
				demandBroker.setBidRequest(bidRequest);
				agent.getBidExchange().setBidRequest(connector.getSupplier(), bidRequest);
				connectors.add(demandBroker);
			} else {
                log.info("skipping: "+connector.getSupplier().getShortName());
            }
		});

		return connectors;
	}

}
