package com.atg.openssp.core.exchange;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atg.openssp.common.core.entry.RequestMonitor;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.logadapter.RequestLogProcessor;
import com.atg.openssp.core.entry.EntryValidator;
import com.atg.service.LogFacade;

import common.RequestException;

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

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws RequestException
	 */
	public RequestSessionAgent(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		super(request, response);
		paramValue = new EntryValidator().validateEntryParams(request);

		LogFacade.logDebug(paramValue.toString());

		bidExchange = new BidExchange();

		// TODO: Messaging entfernen
		// MessagingAdapter.instance.message(Topic.REQUEST_LOG, "receiving
		// request " + requestid);
		RequestLogProcessor.instance.setLogData(this);
		RequestMonitor.monitorRequests();
	}

}
