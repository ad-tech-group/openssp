package com.atg.openssp.core.entry.video;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.CoreSupplyServlet;
import com.atg.openssp.common.core.entry.SessionAgentType;
import com.atg.openssp.common.core.exchange.Exchange;
import com.atg.openssp.common.core.exchange.RequestSessionAgent;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.exchange.ExchangeServer;
import openrtb.tables.AuctionType;

/**
 * Servlet implementation class SupplyVideoService
 * 
 * @author André Schmer
 */
@WebServlet(value = "/SupplyVideoService", asyncSupported = false, name = "SupplyVideo-Service")
public class SupplyVideoService extends CoreSupplyServlet<RequestSessionAgent> {

	private static final long serialVersionUID = 1L;

	@Override
	protected RequestSessionAgent getAgent(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		BiddingServiceInfo info = new BiddingServiceInfo();
		info.setType(SessionAgentType.VIDEO);
		info.setContentType("application/javascript");
		info.setCharacterEncoding("UTF-8");
		info.setAuctionType(AuctionType.SECOND_PRICE);
		RequestSessionAgent agent =  new RequestSessionAgent(request, response, info);
		info.setLoggingId(agent.getRequestid());
		return agent;
	}

	@Override
	protected Exchange<RequestSessionAgent> getServer() {
		return new ExchangeServer();
	}

}
