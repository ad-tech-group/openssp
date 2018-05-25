package com.atg.openssp.core.entry.header;

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
 * Servlet implementation class HeaderBiddingService
 * Header bidding allows for submission of a bid request to a set of DSPs, and derives a winner, but because the
 * result may be party to additional autions, the normal notification of the winner is not made.  We also use
 * first price bidding to ensure we give our best and final offer.
 * 
 * @author Brian Sorensen
 */
@WebServlet(value = "/HeaderBiddingService", asyncSupported = false, name = "HeaderBidding-Service")
public class HeaderBiddingService extends CoreSupplyServlet<RequestSessionAgent> {

	private static final long serialVersionUID = 1L;

	@Override
	protected RequestSessionAgent getAgent(final HttpServletRequest request, final HttpServletResponse response) throws RequestException {
		BiddingServiceInfo info = new BiddingServiceInfo();
		info.setType(SessionAgentType.HEADER);
		info.setContentType("application/json");
		info.setCharacterEncoding("UTF-8");
		info.activateAccessAllowOrigin();
		info.setAuctionType(AuctionType.FIRST_PRICE);
		info.disableSendNurlNotifications();
		RequestSessionAgent agent =  new RequestSessionAgent(request, response, info);
		info.setLoggingId(agent.getRequestid());
		return agent;
	}

	@Override
	protected Exchange<RequestSessionAgent> getServer() {
		return new ExchangeServer();
	}

}
