package com.atg.openssp.core.entry.banner;

import com.atg.openssp.common.demand.BannerObjectParamValue;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.cache.type.AppDataCache;
import com.atg.openssp.core.cache.type.SiteDataCache;
import com.atg.openssp.core.entry.EntryValidatorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class BannerObjectEntryValidatorHandler extends EntryValidatorHandler {
    private final Logger log = LoggerFactory.getLogger(BannerObjectEntryValidatorHandler.class);

    public BannerObjectEntryValidatorHandler()
    {

    }

    @Override
    public List<ParamValue> validateEntryParams(HttpServletRequest request) throws RequestException {
        final ArrayList<ParamValue> pmList = new ArrayList<ParamValue>();
        final BannerObjectParamValue pm = new BannerObjectParamValue();

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

        HashMap<String, List<String>> params = new LinkedHashMap();
        Enumeration<String> penum = request.getParameterNames();
        while(penum.hasMoreElements()) {
            String key = penum.nextElement();
            List<String> values = Arrays.asList(request.getParameterValues(key));
            params.put(key, values);
            log.info("param: "+key+" : "+values);
        }

        /*
        {
        site=[Ljava.lang.String;@1d9a3344,
        callback=[Ljava.lang.String;@b1dbe05,
        callback_uid=[Ljava.lang.String;@4047a76c,
        psa=[Ljava.lang.String;@78ba2df4,
        id=[Ljava.lang.String;@347621b4,
        size=[Ljava.lang.String;@527d5ca9,
        promo_sizes=[Ljava.lang.String;@2ffcfd4d,
        referrer=[Ljava.lang.String;@600a0cb}

         */
        final String siteId = request.getParameter("site");
        final String appid = request.getParameter("app");

        final String callback = request.getParameter("callback");
        final String callbackUid = request.getParameter("callback_uid");
        final String psa = request.getParameter("psa");
        final String id = request.getParameter("id");
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

        pmList.add(pm);
        return pmList;
    }
}
