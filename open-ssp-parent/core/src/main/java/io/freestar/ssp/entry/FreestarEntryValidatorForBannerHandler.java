package io.freestar.ssp.entry;

import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.cache.type.AppDataCache;
import com.atg.openssp.core.cache.type.SiteDataCache;
import com.atg.openssp.core.entry.EntryValidatorHandler;
import io.freestar.ssp.common.demand.FreestarParamValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class FreestarEntryValidatorForBannerHandler extends EntryValidatorHandler {
    private final Logger log = LoggerFactory.getLogger(FreestarEntryValidatorForBannerHandler.class);

    public FreestarEntryValidatorForBannerHandler()
    {

    }

    @Override
    public ParamValue validateEntryParams(HttpServletRequest request) throws RequestException {
        final FreestarParamValue pm = new FreestarParamValue();

        Cookie[] cList = request.getCookies();
        if (cList != null) {
            for (Cookie c : cList) {
                log.info("cookie: "+c.getName());
            }
        } else {
            log.info("no cookies");
        }

        // Note:
        // You may define your individual parameter or payloadto work with.
        // Neither the "ParamValue" - object nor the list of params may fit to your requirements out of the box.

        // geo data could be solved by a geo lookup service and ipaddress

        if (request.getContentLength() > 0) {
            byte[] buffer = new byte[request.getContentLength()];
            try {
                ServletInputStream is = request.getInputStream();
                is.read(buffer);
                String json = new String(buffer);
                log.info("I got content!!! : "+json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HashMap<String, String> params = new LinkedHashMap();
        Enumeration<String> penum = request.getParameterNames();
        while(penum.hasMoreElements()) {
            String key = penum.nextElement();
            List<String> values = Arrays.asList(request.getParameterValues(key));
            if (values.size() > 0) {
                params.put(key, values.get(0));
            }
            log.info("param: "+key+" : "+values);
        }

        /*
        {
INFO: FreestarEntryValidatorForBannerHandler param: callback : [pbjs.handleAnCB]
INFO: FreestarEntryValidatorForBannerHandler param: callback_uid : [61a63db4087493]
INFO: FreestarEntryValidatorForBannerHandler param: psa : [0]
INFO: FreestarEntryValidatorForBannerHandler param: id : [10433394]
INFO: FreestarEntryValidatorForBannerHandler param: _fshash : [38b2680f7b]
INFO: FreestarEntryValidatorForBannerHandler param: _fssid : [8e289c68-c27f-408d-8fa8-e04577c606ad]
INFO: FreestarEntryValidatorForBannerHandler param: _fsloc : [?i=US&c=TG9zIEFuZ2VsZXM=]
INFO: FreestarEntryValidatorForBannerHandler param: _fsuid : [41624435-5e3e-47c6-b382-a938abb79283]
INFO: FreestarEntryValidatorForBannerHandler param: size : [468x60]
INFO: FreestarEntryValidatorForBannerHandler param: promo_sizes : [728x90]
INFO: FreestarEntryValidatorForBannerHandler param: referrer : [http://testsite.com/the-best-website-and-page-building-tools-you-should-rely-on-in-2018/#38b2680f7b]

         */
        final String siteId = request.getParameter("site");
        final String appid = request.getParameter("app");

        final String callback = request.getParameter("callback");
        final String callbackUid = request.getParameter("callback_uid");
        final String psa = request.getParameter("psa");
        final String id = request.getParameter("id");
        final String fsHash = request.getParameter("_fshash");
        final String fssid = request.getParameter("_fssid");
        final String fsLoc = request.getParameter("_fsloc");
        final String fsUid = request.getParameter("_fsuid");
        final String size = request.getParameter("size");
        final String promoSizes = request.getParameter("promo_sizes");
        final String referrer = request.getParameter("referrer");


        try {
            pm.setSite(SiteDataCache.instance.get(siteId));
        } catch (final EmptyCacheException e) {
            try {
                pm.setApp(AppDataCache.instance.get(appid));
            } catch (final EmptyCacheException e1) {
                throw new RequestException(e1.getMessage());
            }
        }
        pm.setCallback(callback);
        pm.setCallbackUid(callbackUid);
        pm.setPsa(psa);
        pm.setId(id);
        pm.setFsHash(fsHash);
        pm.setFsSid(fssid);
        pm.setFsLoc(fsLoc);
        pm.setFsUid(fsUid);
        pm.setSize(size);
        pm.setPromoSizes(promoSizes);
        pm.setReferrer(referrer);

//        System.out.println(request.getParameterMap());

        // pm.setDomain(checkValue(request.getParameter("domain"), ERROR_CODE.E906, "Domain"));
        // pm.setH(checkValue(request.getParameter("h"), ERROR_CODE.E906, "Height"));
        // pm.setW(checkValue(request.getParameter("w"), ERROR_CODE.E906, "Width"));
        // pm.setMimes(convertMimes(request.getParameter("mimes")));
        // pm.setPage(checkValue(request.getParameter("page"), pm.getDomain()));
        // pm.setStartdelay(Integer.valueOf(checkValue(request.getParameter("sd"), "0")));
        // pm.setProtocols(convertProtocolValues(request.getParameter("prot")));

        return pm;
    }
}
