package com.atg.openssp.core.entry.banner;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.core.cache.type.AppDataCache;
import com.atg.openssp.common.core.cache.type.BannerAdDataCache;
import com.atg.openssp.common.core.cache.type.SiteDataCache;
import com.atg.openssp.common.core.entry.EntryValidatorHandler;
import com.atg.openssp.common.demand.BannerObjectParamValue;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.ERROR_CODE;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import openrtb.bidrequest.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;

/**
 * @author bsorensen
 */
public class BannerObjectEntryValidatorHandler extends EntryValidatorHandler {
    public static final String ID_TAG = "placement_id";
    private final Logger log = LoggerFactory.getLogger(BannerObjectEntryValidatorHandler.class);
    private final Gson gson;

    public BannerObjectEntryValidatorHandler() {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    @Override
    public List<ParamValue> validateEntryParams(HttpServletRequest request) throws RequestException {
        final ArrayList<ParamValue> pmList = new ArrayList<ParamValue>();
        Cookie[] cList = request.getCookies();
        if (cList != null) {
            for (Cookie c : cList) {
                log.debug("cookie: " + c.getName());
            }
        } else {
            log.debug("no cookies");
        }
        String method = request.getMethod();
        int contentLength = request.getContentLength();
        String referrer = request.getHeader("referer");
        String ipAddress = request.getRemoteAddr();
        String userAgent = null;
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if ("user-agent".equalsIgnoreCase(name)) {
                userAgent = request.getHeader(name);
            }
        }

        if (method.equalsIgnoreCase("post") && contentLength > 0) {
            try {
                ServletInputStream is = request.getInputStream();
                processValidationFromPost(is, pmList, contentLength, referrer, ipAddress, userAgent);
            } catch (IOException e) {
                // ?? 400
                log.warn("returned E906 " + e.getMessage(), e);
                throw new RequestException(ERROR_CODE.E906, "could not read json input");
            }
        } else if (request.getMethod().equalsIgnoreCase("get")) {
            HashMap<String, String> params = new LinkedHashMap<>();
            Enumeration<String> penum = request.getParameterNames();
            while (penum.hasMoreElements()) {
                String key = penum.nextElement();
                List<String> values = Arrays.asList(request.getParameterValues(key));
                if (values.size() > 0) {
                    params.put(key, values.get(0));
                }
                log.debug("param: " + key + " : " + values);
            }
            processValidationFromGet(params, pmList, referrer, ipAddress, userAgent);
        } else {
            log.warn("No Content");
            log.warn(request.getHeader("User-Agent"));
        }

        return pmList;
    }

    void processValidationFromPost(InputStream is,
                                   final List<ParamValue> pmList,
                                   final int contentLength,
                                   final String referrer,
                                   final String ipAddress,
                                   final String userAgent) throws RequestException {

        byte[] buffer = new byte[contentLength];
        BannerBiddingRequest biddingRequest;
        try {
            is.read(buffer);
            String json = new String(buffer);
            System.out.println("bks-->"+json);
            StringReader bais = new StringReader(json);
            biddingRequest = gson.fromJson(bais, BannerBiddingRequest.class);
            bais.close();
            log.debug("bannerBiddingRequest: " + json.replaceAll("\n", "").replaceAll("  ", ""));

        } catch (IOException e) {
            // ?? 400
            log.warn("returned E906 " + e.getMessage(), e);
            throw new RequestException(ERROR_CODE.E906, "could not read json input");
        } catch (JsonSyntaxException e) {
            log.warn("returned E906 " + e.getMessage());
            throw new RequestException(ERROR_CODE.E906, "could not read json input");
        } catch (Exception e) {
            log.warn("returned E906 " + e.getMessage(), e);
            throw new RequestException(ERROR_CODE.E906, "could not read json input");
        }
        processValidation(pmList, biddingRequest, referrer, ipAddress, userAgent);
    }

    void processValidationFromGet(Map<String, String> params,
                                  final List<ParamValue> pmList,
                                  final String referrer,
                                  final String ipAddress,
                                  final String userAgent) throws RequestException {
        BannerBiddingRequest biddingRequest = new BannerBiddingRequest();

        biddingRequest.setSite(params.get("site"));
        biddingRequest.setApp(params.get("app"));
        biddingRequest.setPage(params.get("page"));
        biddingRequest.setId(params.get("id"));
        biddingRequest.setSid(params.get("sid"));
        biddingRequest.setHash(params.get("hash"));
        biddingRequest.setLoc(params.get("loc"));
        biddingRequest.setUid(params.get("uid"));

        biddingRequest.setHash(biddingRequest.getHash());
        final String adId = params.get(ID_TAG);
        try {
            biddingRequest.addBannerAdToBidUpon(BannerAdDataCache.instance.get(adId));
        } catch (final EmptyCacheException e) {
            throw new RequestException(e.getMessage());
        }

        processValidation(pmList, biddingRequest, referrer, ipAddress, userAgent);
    }

    private void processValidation(final List<ParamValue> pmList,
                                   final BannerBiddingRequest biddingRequest,
                                   final String referrer,
                                   final String ipAddress,
                                   final String userAgent) throws RequestException {

        List<BannerAd> adList = biddingRequest.getBannerAdsToBidUpon();
        for (BannerAd a : adList) {
            final BannerObjectParamValue pm = new BannerObjectParamValue();
            pm.setBannerad(a);

            try {
                Site s = SiteDataCache.instance.get(biddingRequest.getSite());
                // We need to clone the site to support the cookie sync - buyerid addition in the user object
                Site site = s.clone();
                site.setRef(referrer);
                pm.setSite(site);
            } catch (final EmptyCacheException e) {
                try {
                    String requestedApp = biddingRequest.getApp();
                    log.info("requested app: " + requestedApp);
                    pm.setApp(AppDataCache.instance.get(requestedApp));
                } catch (final EmptyCacheException e1) {
                    throw new RequestException(ERROR_CODE.E906, "missing site or app (1)");
                }
            }

            pm.setRequestId(biddingRequest.getId());

            pm.setSid(biddingRequest.getSid());
            pm.setLoc(biddingRequest.getLoc());
            pm.setUid(biddingRequest.getUid());
            pm.setHash(biddingRequest.getHash());
            pm.setPsa("0");

            pm.setIpAddress(ipAddress);
            pm.setBrowserUserAgentString(userAgent);
            pmList.add(pm);
        }

    }
}
