package com.atg.openssp.core.entry.banner;

import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.CoreSupplyServlet;
import com.atg.openssp.common.core.entry.SessionAgentType;
import com.atg.openssp.common.core.exchange.Exchange;
import com.atg.openssp.common.core.exchange.RequestSessionAgent;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.exchange.ExchangeServer;
import openrtb.tables.AuctionType;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SupplyBannerService
 * 
 * @author André Schmer
 */
@WebServlet(value = "/SupplyBannerService", asyncSupported = false, name = "SupplyBanner-Service")
public class SupplyBannerService extends CoreSupplyServlet<RequestSessionAgent> {

	private static final long serialVersionUID = 1L;

	@Override
	protected RequestSessionAgent getAgent(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		BiddingServiceInfo info = new BiddingServiceInfo();
		info.setType(SessionAgentType.BANNER);
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
