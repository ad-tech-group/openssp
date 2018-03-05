package io.freestar.ssp.entry;

import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.cache.type.AppDataCache;
import com.atg.openssp.core.cache.type.SiteDataCache;
import com.atg.openssp.core.entry.EntryValidatorHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.freestar.ssp.common.demand.FreestarParamValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class FreestarEntryValidatorForHeaderHandler extends EntryValidatorHandler {
    private final Logger log = LoggerFactory.getLogger(FreestarEntryValidatorForHeaderHandler.class);
    private final Gson gson;

    public FreestarEntryValidatorForHeaderHandler()
    {
        gson = new Gson();
    }

    @Override
    public ParamValue validateEntryParams(HttpServletRequest request) throws RequestException {
        final FreestarParamValue pm = new FreestarParamValue();

        Cookie[] cList = request.getCookies();
        if (cList != null) {
            for (Cookie c : cList) {
                //log.info("cookie: "+c.getName());
            }
        } else {
            //log.info("no cookies");
        }

        // Note:
        // You may define your individual parameter or payloadto work with.
        // Neither the "ParamValue" - object nor the list of params may fit to your requirements out of the box.

        // geo data could be solved by a geo lookup service and ipaddress

        HeaderBiddingRequest r=null;
        if (request.getContentLength() > 0) {
            byte[] buffer = new byte[request.getContentLength()];
            try {
                ServletInputStream is = request.getInputStream();
                is.read(buffer);
                String json = new String(buffer);
//                log.info("I got content!!! : "+json);
                System.out.println("I got content!!!bks : "+json);
                StringReader bais = new StringReader(json);
                r = gson.fromJson(bais, HeaderBiddingRequest.class);
                bais.close();

                /*
                      "id": "46109ce8b3ad6d41",
      "adUnitCode": "freestar-slot-footer-ad",
      "size": "970x90",
      "promo_sizes": "970x250,728x90",
      "placementId": "10433394"
                 */

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        System.out.println(r);
        /*
          "site": "testsite.com",
  "page": "/the-best-website-and-page-building-tools-you-should-rely-on-in-2018/",
  "_fshash": "38b2680f7b",
  "_fsloc": "?i=US&c=TG9zIEFuZ2VsZXM=",
  "_fsuid": "41624435-5e3e-47c6-b382-a938abb79283",
  "_fssid": "747aed65-74b4-4706-9962-61bc08fb66cd"
         */

        if (r != null) {

            try {
                pm.setSite(SiteDataCache.instance.get(r.getSite()));
            } catch (final EmptyCacheException e) {
                e.printStackTrace();
//                try {
//                    pm.setApp(AppDataCache.instance.get(r.getApp()));
//                } catch (final EmptyCacheException e1) {
//                    throw new RequestException(e1.getMessage());
//                }
            }

            pm.setRequestId(r.getId());
            pm.setCallback(r.getPage());
//            pm.setCallbackUid("pbjs.handleAnCB");
            pm.setFsSid(r.getFsSid());
            pm.setFsLoc(r.getFsLoc());
            pm.setFsUid(r.getFsUid());
            pm.setFsHash(r.getFsHash());
            pm.setPsa("0");

            List<AdUnit> adList = r.getAdUnitsToBidUpon();
            for (AdUnit a : adList) {
                pm.setId(a.getId());
                pm.setSize(a.getSize());
                pm.setPromoSizes(a.getPromoSizes());
                pm.setReferrer("http%3A%2F%2Ftestsite.com%2Fthe-best-website-and-page-building-tools-you-should-rely-on-in-2018%2F%2338b2680f7b");
                break; // TODO:
            }

        } else {
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

            try {
                pm.setSite(SiteDataCache.instance.get(params.get("site")));
            } catch (final EmptyCacheException e) {
                try {
                    pm.setApp(AppDataCache.instance.get(params.get("app")));
                } catch (final EmptyCacheException e1) {
                    throw new RequestException(e1.getMessage());
                }
            }
            pm.setRequestId(params.get("id"));
            pm.setCallback(params.get("callback"));
            pm.setCallbackUid(params.get("callback_uid"));
            pm.setPsa(params.get("psa"));
            pm.setId(params.get("id"));
            pm.setFsHash(params.get("_fshash"));
            pm.setFsSid(params.get("_fssid"));
            pm.setFsLoc(params.get("_fsloc"));
            pm.setFsUid(params.get("_fsuid"));
            pm.setSize(params.get("size"));
            pm.setPromoSizes(params.get("promo_sizes"));
            pm.setReferrer(params.get("referrer"));
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
