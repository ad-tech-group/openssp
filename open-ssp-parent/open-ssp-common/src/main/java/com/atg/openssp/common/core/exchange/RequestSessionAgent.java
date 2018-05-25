package com.atg.openssp.common.core.exchange;

import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.EntryValidator;
import com.atg.openssp.common.core.entry.RequestMonitor;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.common.logadapter.RequestLogProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Handling class for single request handling. Holding all those data which are relevant to a video request and performing demand requests.
 * 
 * Responsible for extracting and evaluating request parameter. Creates a requestid as request identifier, useful in logging.
 * 
 * 
 * @author André Schmer
 *
 */
public class RequestSessionAgent extends SessionAgent {

	private static final Logger log = LoggerFactory.getLogger(RequestSessionAgent.class);
	private final BiddingServiceInfo info;

	/**
	 * 
	 * @param request
	 * @param response
	 * @param info
	 * @throws RequestException
	 */
	public RequestSessionAgent(final HttpServletRequest request, final HttpServletResponse response, BiddingServiceInfo info) throws RequestException {
		super(request, response);
		this.info = info;
		RequestLogProcessor.instance.setLogData(this);
		RequestMonitor.monitorRequests();
	}

	@Override
	protected List<ParamValue> createParamValue(HttpServletRequest request) throws RequestException {
		List<ParamValue> pList = new EntryValidator(info.getType()).validateEntryParams(request);
		for (ParamValue paramValue : pList) {
			log.debug(paramValue.toString());
            info.setParameter(paramValue);
		}
		return pList;
	}

	@Override
	protected BidExchange createBidExchange() {
		return new BidExchange();
	}

	public BiddingServiceInfo getBiddingServiceInfo() {
		return info;
	}

}
