package com.atg.openssp.core.entry;

import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.CoreSupplyServlet;
import com.atg.openssp.common.core.entry.SessionAgentType;
import com.atg.openssp.common.core.exchange.Exchange;
import com.atg.openssp.common.core.exchange.RequestSessionAgent;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.exchange.ExchangeServer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SupplyBannerService
 * 
 * @author André Schmer
 */
@WebServlet(value = "/BidRequestService", asyncSupported = false, name = "BidRequest-Service")
public class BidRequestService extends CoreSupplyServlet<RequestSessionAgent> {

	private static final long serialVersionUID = 1L;

	@Override
	protected RequestSessionAgent getAgent(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		BiddingServiceInfo info = new BiddingServiceInfo();
		info.setType(SessionAgentType.BANNER);
		info.setContentType("application/javascript");
		info.setCharacterEncoding("UTF-8");
		RequestSessionAgent agent =  new RequestSessionAgent(request, response, info);
		info.setLoggingId(agent.getRequestid());
		return agent;
	}

	@Override
	protected Exchange<RequestSessionAgent> getServer() {
		return new ExchangeServer();
	}

}
