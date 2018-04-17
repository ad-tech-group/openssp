package com.atg.openssp.common.core.exchange.channel.rtb;

import com.atg.openssp.common.core.broker.AbstractBroker;
import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.ResponseContainer;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.exception.BidProcessingException;
import com.atg.openssp.common.logadapter.RtbRequestLogProcessor;
import com.atg.openssp.common.logadapter.RtbResponseLogProcessor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.BidResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * This class acts as Broker to a connector used in demand (OpenRTB) context. It represents one Demand side (DSP).
 * 
 * @author André Schmer
 *
 */
public final class DemandBroker extends AbstractBroker implements Callable<ResponseContainer> {

	private static final Logger log = LoggerFactory.getLogger(DemandBroker.class);

	private final BiddingServiceInfo info;

	private final Supplier supplier;

	private final OpenRtbConnector connector;

	private final Header[] headers;

	private Gson gson;

	private BidRequest bidrequest;

	public DemandBroker(BiddingServiceInfo info, final Supplier supplier, final OpenRtbConnector connector, final SessionAgent agent) {
		super(agent);
		this.info = info;
		this.supplier = supplier;
		this.connector = connector;

		headers = new Header[2];
		headers[0] = new BasicHeader("x-openrtb-version", supplier.getOpenRtbVersion());
		headers[1] = new BasicHeader("ContentType", supplier.getContentType());
		// headers[2] = new BasicHeader("Accept-Encoding", supplier.getAcceptEncoding());
		// headers[3] = new BasicHeader("Content-Encoding", supplier.getContentEncoding());

		try {
			gson = new GsonBuilder().setVersion(Double.valueOf(supplier.getOpenRtbVersion())).create();
		} catch (Throwable t) {
			log.error(t.getMessage(), t);
		}
	}

	@Override
	public ResponseContainer call() throws Exception {
		if (bidrequest == null) {
			return null;
		}

		try {
			final String jsonBidrequest = info.getDemandBrokerFilter(supplier, gson, bidrequest).filterRequest(gson, bidrequest);

			log.debug("biderquest: " + jsonBidrequest);
			RtbRequestLogProcessor.instance.setLogData(jsonBidrequest, "bidrequest", supplier.getShortName());

			final String result = connector.connect(jsonBidrequest, headers);
			log.debug("bidresponse: " + result);
			RtbResponseLogProcessor.instance.setLogData(result, "bidresponse", supplier.getShortName());

			if (!StringUtils.isEmpty(result)) {
				final BidResponse bidResponse = info.getDemandBrokerFilter(supplier, gson, bidrequest).filterResponse(gson, result);
				return new ResponseContainer(supplier, bidResponse);
			}
		} catch (final BidProcessingException e) {
			log.error(getClass().getSimpleName() + " " + e.getMessage());
			throw e;
		} catch (final Exception e) {
			log.error(getClass().getSimpleName() + " " + e.getMessage());
			//throw e;
		}
		return null;
	}

	public void setBidRequest(final BidRequest bidrequest) {
		this.bidrequest = bidrequest;
	}

}
