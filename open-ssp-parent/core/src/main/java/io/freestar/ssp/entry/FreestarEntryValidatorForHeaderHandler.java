package io.freestar.ssp.entry;

import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.ERROR_CODE;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.cache.type.AppDataCache;
import com.atg.openssp.core.cache.type.SiteDataCache;
import com.atg.openssp.core.entry.EntryValidatorHandler;
import com.google.gson.Gson;
import io.freestar.ssp.common.demand.FreestarParamValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @author bsorensen
 */
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
                log.debug("cookie: "+c.getName());
            }
        } else {
            log.debug("no cookies");
        }

        HeaderBiddingRequest biddingRequest=null;
        if (request.getMethod().equalsIgnoreCase("post") && request.getContentLength() > 0) {
            byte[] buffer = new byte[request.getContentLength()];
            try {
                ServletInputStream is = request.getInputStream();
                is.read(buffer);
                String json = new String(buffer);
                StringReader bais = new StringReader(json);
                biddingRequest = gson.fromJson(bais, HeaderBiddingRequest.class);
                bais.close();

            } catch (IOException e) {
                log.warn("returned 906: "+e.getMessage(), e);
                throw new RequestException(ERROR_CODE.E906, "could not read json input");
            }
        }

        if (biddingRequest != null) {

            try {
                pm.setSite(SiteDataCache.instance.get(biddingRequest.getSite()));
            } catch (final EmptyCacheException e) {
                e.printStackTrace();
                try {
                    pm.setApp(AppDataCache.instance.get(biddingRequest.getApp()));
                } catch (final EmptyCacheException e1) {
                    throw new RequestException(ERROR_CODE.E906, "missing site or app");
                }
            }

            pm.setRequestId(biddingRequest.getId());
            pm.setFsSid(biddingRequest.getFsSid());
            pm.setFsLoc(biddingRequest.getFsLoc());
            pm.setFsUid(biddingRequest.getFsUid());
            pm.setFsHash(biddingRequest.getFsHash());
            pm.setPsa("0");

            List<AdUnit> adList = biddingRequest.getAdUnitsToBidUpon();
            for (AdUnit a : adList) {
                pm.setId(a.getId());
                pm.setSize(a.getSize());
                pm.setPromoSizes(a.getPromoSizes());
                pm.setReferrer(biddingRequest.getSite()+biddingRequest.getPage());
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
                log.debug("param: "+key+" : "+values);
            }

            try {
                pm.setSite(SiteDataCache.instance.get(params.get("site")));
            } catch (final EmptyCacheException e) {
                try {
                    pm.setApp(AppDataCache.instance.get(params.get("app")));
                } catch (final EmptyCacheException e1) {
                    throw new RequestException(ERROR_CODE.E906, "missing site or app");
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


        // pm.setDomain(checkValue(request.getParameter("domain"), ERROR_CODE.E906, "Domain"));
        // pm.setH(checkValue(request.getParameter("h"), ERROR_CODE.E906, "Height"));
        // pm.setW(checkValue(request.getParameter("w"), ERROR_CODE.E906, "Width"));
        // pm.setMimes(convertMimes(request.getParameter("mimes")));
        // pm.setPage(checkValue(request.getParameter("page"), pm.getDomain()));
        // pm.setStartdelay(Integer.valueOf(checkValue(request.getParameter("sd"), "0")));
        // pm.setProtocols(convertProtocolValues(request.getParameter("prot")));

        pm.setIpAddress(request.getRemoteAddr());

        return pm;
    }
}
