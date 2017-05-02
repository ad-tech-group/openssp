package com.atg.openssp.common.core.entry;

import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.ParamValue;

/**
 * Holding meta data.
 * 
 * @author André Schmer
 *
 */
public abstract class SessionAgent {

	protected final String requestid;

	protected final HttpServletRequest httpRequest;

	protected final HttpServletResponse httpResponse;

	protected ParamValue paramValue;

	protected BidExchange bidExchange;

	public SessionAgent(final HttpServletRequest request, final HttpServletResponse response) {
		httpRequest = request;
		httpResponse = response;
		final Random r = new Random();
		requestid = new UUID(r.nextLong(), r.nextLong()).toString();
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public HttpServletResponse getHttpResponse() {
		return httpResponse;
	}

	public String getRequestid() {
		return requestid;
	}

	public void cleanUp() {
		bidExchange = null;
		paramValue = null;
	}

	public ParamValue getParamValues() {
		return paramValue;
	}

	public BidExchange getBidExchange() {
		return bidExchange;
	}

	public String getRemoteIP() {
		return httpRequest.getRemoteAddr();
	}
}
