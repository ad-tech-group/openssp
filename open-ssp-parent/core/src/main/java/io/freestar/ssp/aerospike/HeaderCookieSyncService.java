package io.freestar.ssp.aerospike;

import com.atg.openssp.core.entry.header.AerospikeService;
import io.freestar.ssp.aerospike.data.CookieSyncDTO;
import io.freestar.ssp.aerospike.data.DspCookieDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    private final Properties properties = new Properties();
    private final String set;
    private final String bin;

    private AerospikeService aerospikeService;

    public HeaderCookieSyncService()
    {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resolveEnvironment()+"aerospike.properties");
        if (is != null) {
            try {
                properties.load(is);
                is.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        String host=properties.getProperty("host");
        int port = Integer.parseInt(properties.getProperty("port"));
        String user=properties.getProperty("user");
        String pw=properties.getProperty("password");
        String namespace=properties.getProperty("namespace");
        int expiration=Integer.parseInt(properties.getProperty("expiration"));
        set = properties.getProperty("set");
        bin = properties.getProperty("bin");
        aerospikeService = new AerospikeService(host, port, user, pw, namespace, expiration);
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

        CookieSyncDTO result = aerospikeService.get(set, fsuid, bin);
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
            aerospikeService.set(set, fsuid, bin, result);
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

    private String resolveEnvironment() {
        String environment = System.getProperty("SSP_ENVIRONMENT");
        log.info("Environment: "+environment);
        if (environment != null) {
            return environment+"_";
        } else {
            return "";
        }
    }

}
