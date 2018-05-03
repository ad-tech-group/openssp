package io.freestar.ssp.aerospike;

import com.atg.openssp.common.logadapter.DspCookieSyncLogProcessor;
import io.freestar.openssp.common.exchange.aerospike.AerospikeService;
import io.freestar.openssp.common.exchange.aerospike.data.CookieSyncDTO;
import io.freestar.openssp.common.exchange.aerospike.data.DspCookieDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Servlet implementation class HeaderCookieSyncService
 * 
 * @author Brian Sorensen
 */
@WebServlet(value = "/cookiesync", asyncSupported = false, name = "HeaderBidding-Cookie-Sync-Service")
public class HeaderCookieSyncService extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(HeaderCookieSyncService.class);

    public HeaderCookieSyncService()
    {
        DspCookieSyncLogProcessor.instance.setLogData("cookie-sync", "update", "bks was here!");
    }

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

        CookieSyncDTO result = AerospikeService.getInstance().get(fsuid);
        if (result == null) {
            result = new CookieSyncDTO();
            result.setFsuid(fsuid);
        }
        DspCookieDto dspResult = result.lookup(dspShortName);
        if (dspResult == null) {
            dspResult = new DspCookieDto();
            dspResult.setShortName(dspShortName);
            result.add(dspResult);
        }
        String checkUid = dspResult.getUid();
        if (checkUid == null) {
            dspResult.setUid(dspUid);
        } else {
            if (!checkUid.equals(dspUid)) {
                dspResult.setUid(dspUid);
            }
        }

        if (result.isDirty()) {
            AerospikeService.getInstance().set(fsuid, result);
            DspCookieSyncLogProcessor.instance.setLogData("cookie-sync", "update", fsuid, dspShortName, dspUid);
        }


        response.addHeader("Content-Type", "application/json");
//        response.addHeader("Access-Control-Allow-Origin", "https://webdesignledger.com");
//        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
//        response.addHeader("Access-Control-Allow-Credentials", "true");

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
