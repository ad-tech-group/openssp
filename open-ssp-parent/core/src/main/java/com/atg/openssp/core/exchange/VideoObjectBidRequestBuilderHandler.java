package com.atg.openssp.core.exchange;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.cache.type.PricelayerCache;
import com.atg.openssp.common.core.exchange.BidRequestBuilderHandler;
import com.atg.openssp.common.core.exchange.RequestSessionAgent;
import com.atg.openssp.common.core.exchange.cookiesync.CookieSyncDTO;
import com.atg.openssp.common.core.exchange.cookiesync.CookieSyncManager;
import com.atg.openssp.common.core.exchange.geo.AddressNotFoundException;
import com.atg.openssp.common.core.exchange.geo.FreeGeoIpInfoHandler;
import com.atg.openssp.common.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.common.core.exchange.geo.UnavailableHandlerException;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.demand.VideoObjectParamValue;
import com.atg.openssp.common.exception.ERROR_CODE;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import openrtb.bidrequest.model.*;
import openrtb.tables.GeoType;
import openrtb.tables.ImpressionSecurity;
import openrtb.tables.VideoBidResponseProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public class VideoObjectBidRequestBuilderHandler extends BidRequestBuilderHandler {
    private final Logger log = LoggerFactory.getLogger(VideoObjectBidRequestBuilderHandler.class);
    private final Base64.Decoder decoder;
    private GeoIpInfoHandler geoIpInfoHandler;


    public VideoObjectBidRequestBuilderHandler() {
        decoder = Base64.getDecoder();
        String handlerClassName = GlobalContext.getGeoIpInfoHandlerClass();
        if (handlerClassName == null) {
            geoIpInfoHandler = new FreeGeoIpInfoHandler();
        } else {
            try {
                Class handlerClass = Class.forName(handlerClassName);
                Constructor cc = handlerClass.getConstructor(new Class[]{});
                geoIpInfoHandler = (GeoIpInfoHandler) cc.newInstance(new Object[]{});
            } catch (Exception e) {
                log.error("could not load GeoIpInfoHandler as specified.  Loading default handler;");
                geoIpInfoHandler = new FreeGeoIpInfoHandler();
            }
        }
    }

    @Override
    public BidRequest constructRequest(RequestSessionAgent agent) throws RequestException {
        List<ParamValue> pValueList;
        try {
            pValueList = agent.getParamValues();
        } catch (RequestException e) {
            if (e.getCode() == ERROR_CODE.E906) {
                throw e;
            }
            log.warn(e.getMessage(), e);
            pValueList = new ArrayList();
            pValueList.add(new VideoObjectParamValue());
        }
        VideoObjectParamValue masterValues = (VideoObjectParamValue) pValueList.get(0);

        Site site = masterValues.getSite().clone();
        String requestId = masterValues.getRequestId();

        Device dd = new Device.Builder().build();
        dd.setGeo(createSiteGeo(masterValues));
        dd.setUa(masterValues.getBrowserUserAgentString());

        BidRequest bidRequest =  new BidRequest.Builder()
                .setId(selectAppropriateId(requestId, agent.getRequestid()))
                .setAt(agent.getBiddingServiceInfo().getAuctionType())
                .setSite(site)
                .setDevice(dd)
                .setUser(createUser(masterValues))
                .addCur(CurrencyCache.instance.getBaseCurrency())
                .setTmax((int)GlobalContext.getExecutionTimeout())
                .build();

        int idCount = 1;
        for (ParamValue pOrigin : pValueList) {
            VideoObjectParamValue pValues = (VideoObjectParamValue) pOrigin;

            Impression i = new Impression.Builder().build();
            i.setId(Integer.toString(idCount++));
            i.setVideo(createVideo(pValues));
            try {
                i.setBidfloor(PricelayerCache.instance.get(site.getId()).getBidfloor());
                i.setBidfloorcur(PricelayerCache.instance.get(site.getId()).getCurrency());
            } catch (EmptyCacheException e) {
                log.info("price floor does not exist for site: "+site.getId());
                i.setBidfloor(0f);
                i.setBidfloorcur(CurrencyCache.instance.getBaseCurrency());
            }
            i.setSecure(ImpressionSecurity.NON_SECURE);
            bidRequest.addImp(i);

        }

        return bidRequest;
    }

    private User createUser(VideoObjectParamValue pValues) {
        String userId = pValues.getFsUid();
        if (CookieSyncManager.getInstance().supportsCookieSync()) {
            CookieSyncDTO result = CookieSyncManager.getInstance().get(userId);
            if (result != null) {
            }
        }
        return new User.Builder()
//                .setBuyeruid()
                //.setGender(pValues.getGender())
                .setId(userId)
                //.setYob(pValues.getYearOfBirth())
                //.setGeo(createUserGeo(pValues))
                .build();

    }

    private Geo createSiteGeo(ParamValue pValues) {
        Geo geo = new Geo.Builder().build();
        String ipAddress = pValues.getIpAddress();
        if (ipAddress != null && !ipAddress.equalsIgnoreCase("localhost") && !ipAddress.equalsIgnoreCase("0:0:0:0:0:0:0:1") && !ipAddress.equalsIgnoreCase("127.0.0.1")) {
            try {
                GeoIpInfo geoInfo = geoIpInfoHandler.lookupGeoInfo(ipAddress);
                geo.setLat(geoInfo.getLat());
                geo.setLon(geoInfo.getLon());
                geo.setZip(geoInfo.getZip());
                geo.setCity(geoInfo.getCity());
                geo.setCountry(geoInfo.getCountryCode());
                geo.setMetro(geoInfo.getMetroCode());
                geo.setRegion(geoInfo.getRegionCode());
                geo.setType(GeoType.IP);
                //geo.setUtcOffset(?);
                geo.setIpServiceType(geoInfo.getIpServiceType());
                //geo.setExt(?)
            } catch (IOException e) {
                log.warn("could not obtain geo code: "+e.getMessage(), e);
                return null;
            } catch (UnavailableHandlerException e) {
                log.warn("could not obtain geo code: "+e.getMessage());
                return null;
            } catch (AddressNotFoundException e) {
                log.warn("could not find ip address");
                return null;
            }
        }
        return geo;
    }

    private Video createVideo(VideoObjectParamValue pValues) {
        return new Video.Builder()
                .addMime("application/x-shockwave-flash")
                .setH(400)
                .setW(600)
                .setMaxduration(100)
                .setMinduration(30)
                .addToProtocols(VideoBidResponseProtocol.VAST_2_0)
                .setStartdelay(1)
                .build();
    }

    private String selectAppropriateId(String requestId, String agentRequestid) {
        if (requestId !=null) {
            return requestId;
        } else {
            return agentRequestid;
        }
    }

}
