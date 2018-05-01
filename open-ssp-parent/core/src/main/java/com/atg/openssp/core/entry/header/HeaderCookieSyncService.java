package com.atg.openssp.core.entry.header;

import com.atg.openssp.common.buffer.SSPLatencyBuffer;
import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.CoreSupplyServlet;
import com.atg.openssp.common.core.entry.SessionAgentType;
import com.atg.openssp.common.core.exchange.Auction;
import com.atg.openssp.common.core.exchange.Exchange;
import com.atg.openssp.common.core.exchange.RequestSessionAgent;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.common.logadapter.TimeInfoLogProcessor;
import com.atg.openssp.core.exchange.ExchangeServer;
import com.google.common.base.Stopwatch;
import openrtb.tables.AuctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;

/**
 * Servlet implementation class SupplyBannerService
 * 
 * @author Brian Sorensen
 */
@WebServlet(value = "/cookiesync", asyncSupported = false, name = "HeaderBidding-Cookie-Sync-Service")
public class HeaderCookieSyncService extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(HeaderCookieSyncService.class);

	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        HashMap<String, String> params = new LinkedHashMap();
        Enumeration<String> penum = request.getParameterNames();
        while (penum.hasMoreElements()) {
            String key = penum.nextElement();
            List<String> values = Arrays.asList(request.getParameterValues(key));
            if (values.size() > 0) {
                params.put(key, values.get(0));
            }
            log.debug("param: " + key + " : " + values);
        }

        String fsuid = params.get("uid");
        String dspShortName = params.get("dsp");
        String dspUid = params.get("dsp_uid");

        response.addHeader("Content-Type", "application/json");
        response.addHeader("Access-Control-Allow-Origin", "https://webdesignledger.com");
//        response.addHeader("Access-Control-Allow-Methods", "POST");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Allow-Credentials", "true");


//        info.setCharacterEncoding("UTF-8");
//        info.activateAccessAllowOrigin();
//        info.setAuctionType(AuctionType.FIRST_PRICE);
//        info.disableSendNurlNotifications();


        response.sendError(200, "{success}");
	}

	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
