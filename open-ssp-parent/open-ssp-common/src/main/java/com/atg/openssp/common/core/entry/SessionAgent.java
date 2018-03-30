package com.atg.openssp.common.core.entry;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.RequestException;

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

	private List<ParamValue> paramValueList;

	private BidExchange bidExchange;
	private boolean paramValueInitialized;

	public SessionAgent(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		httpRequest = request;
		httpResponse = response;

		final Random r = new Random();
		requestid = new UUID(r.nextLong(), r.nextLong()).toString();
		bidExchange = createBidExchange();
	}

	protected abstract List<ParamValue> createParamValue(HttpServletRequest request) throws RequestException;

    protected abstract BidExchange createBidExchange();

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
		paramValueList = null;
		paramValueInitialized = false;
	}

	public List<ParamValue> getParamValues() throws RequestException {
    	if (!paramValueInitialized) {
			paramValueList = createParamValue(httpRequest);
			paramValueInitialized = true;
		}
		return paramValueList;
	}

	public BidExchange getBidExchange() {
		return bidExchange;
	}

	public String getRemoteIP() {
		return httpRequest.getRemoteAddr();
	}
}
