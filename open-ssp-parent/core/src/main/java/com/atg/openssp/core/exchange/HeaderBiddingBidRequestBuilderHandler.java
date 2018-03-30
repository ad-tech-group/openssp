package com.atg.openssp.core.exchange;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.HeaderBiddingParamValue;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.ERROR_CODE;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.exchange.geo.AddressNotFoundException;
import com.atg.openssp.core.exchange.geo.FreeGeoIpInfoHandler;
import com.atg.openssp.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.core.exchange.geo.UnavailableHandlerException;
import openrtb.bidrequest.model.*;
import openrtb.tables.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public class HeaderBiddingBidRequestBuilderHandler extends BidRequestBuilderHandler {
    private final Logger log = LoggerFactory.getLogger(HeaderBiddingBidRequestBuilderHandler.class);
    private final Base64.Decoder decoder;
    private GeoIpInfoHandler geoIpInfoHandler;


    public HeaderBiddingBidRequestBuilderHandler() {
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
            pValueList.add(new HeaderBiddingParamValue());
        }
        HeaderBiddingParamValue masterValues = (HeaderBiddingParamValue) pValueList.get(0);

        Site site;
        if (masterValues.getSite() != null) {
            site = masterValues.getSite().clone();
        } else {
            site = null;
        }
        App app;
        if (masterValues.getApp() != null) {
            app = masterValues.getApp().clone();
        } else {
            app = null;
        }
        String requestId = masterValues.getRequestId();

        Device dd = new Device.Builder().build();
        dd.setGeo(createSiteGeo(masterValues));
        dd.setUa(masterValues.getBrowserUserAgentString());
        dd.setIp(masterValues.getIpAddress());

        BidRequest bidRequest =  new BidRequest.Builder()
                .setId(selectAppropriateId(requestId, agent.getRequestid()))
                .setAt(agent.getBiddingServiceInfo().getAuctionType())
                .setSite(site)
                .setApp(app)
                .setDevice(dd)
                .setUser(createUser(masterValues))
                .addCur(CurrencyCache.instance.getBaseCurrency())
                .build();

        int idCount = 1;
        for (ParamValue pOrigin : pValueList) {
            HeaderBiddingParamValue pValues = (HeaderBiddingParamValue) pOrigin;

            Impression i = new Impression.Builder().build();
            i.setId(Integer.toString(idCount++));
            i.setVideo(createVideo(pValues));
            i.setBanner(createBanner(pValues));
            //i.setNative(createNative(pValues));
            //TODO: BKS
            i.setBidfloor(0f);
            i.setSecure(ImpressionSecurity.NON_SECURE);
            bidRequest.addImp(i);

        }

        return bidRequest;
    }

    private User createUser(HeaderBiddingParamValue pValues) {
        return new User.Builder()
                //.setBuyeruid()
                //.setGender(pValues.getGender())
                .setId(pValues.getFsUid())
                //.setYob(pValues.getYearOfBirth())
                //.setGeo(createUserGeo(pValues))
                .build();

    }

    private Native createNative(HeaderBiddingParamValue pValues) {
        return null;
        // Native not implemeneted yet.  We need a way to specify type
    }

    private Video createVideo(HeaderBiddingParamValue pValues) {
        return null;
        // Video not implemeneted yet.  We need a way to specify type
        /*
        return new Video.Builder()
                .addMimeX("application/x-shockwave-flash")
                .setHX(400)
                .setWX(600)
                .setMaxdurationX(100)
                .setMindurationX(30)
                .addProtocolX(VideoBidResponseProtocol.VAST_2_0)
                .setStartdelayX(1)
                .build();
                */
    }

    private Geo createSiteGeo(HeaderBiddingParamValue pValues) {
        Geo geo = new Geo.Builder().build();
        StringTokenizer st = new StringTokenizer(pValues.getFsLoc(), "?&");
        while(st.hasMoreTokens()) {
            String t = st.nextToken();
            if (t.startsWith("i=")) {
                geo.setCountry(t.substring(2));
            } else if (t.startsWith("c=")) {
                geo.setCity(new String(decoder.decode(t.substring(2).getBytes())));
            }
        }
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
                // no new address offerings to help
                // keep what we have and move on
            } catch (UnavailableHandlerException e) {
                log.warn("could not obtain geo code: "+e.getMessage());
                // no new address offerings to help
                // keep what we have and move on
            } catch (AddressNotFoundException e) {
                // no new address offerings to help
                // keep what we have and move on
            }
        }
        return geo;
    }

    private Banner createBanner(HeaderBiddingParamValue pValues) {
        Banner b = new Banner.Builder().setId(pValues.getId()).build();
        ArrayList<Banner.BannerSize> sizes = new ArrayList();
        Banner.BannerSize size = new Banner.BannerSize(pValues.getSize().toLowerCase());
        b.setW(size.getW());
        b.setH(size.getH());
        sizes.add(size);

        StringTokenizer st = new StringTokenizer(pValues.getPromoSizes(), ",");
        while(st.hasMoreTokens()) {
            String token = st.nextToken();
            Banner.BannerSize ts = new Banner.BannerSize(token);
            sizes.add(ts);
        }
        Collections.sort(sizes);
        b.setWmin(sizes.get(0).getW());
        b.setHmin(sizes.get(0).getH());
        b.setWmax(sizes.get(sizes.size()-1).getW());
        b.setHmax(sizes.get(sizes.size()-1).getH());
        b.setFormat(sizes.toArray());

        //TODO: BKS
        b.setAllBattr(new CreativeAttribute[]{CreativeAttribute.PROVOCATIVE_OR_SUGGESTIVE_IMAGERY});
//        b.setApi();
        b.setAllBtype(new BannerAdType[]{BannerAdType.IFRAME});
//        b.setExpdir();
        b.setMimes(new String[]{});
        b.setPos(AddPosition.FOOTER);
//        b.setTopframe();
//        b.setExt();
        return b;
    }

    private String selectAppropriateId(String requestId, String agentRequestid) {
        if (requestId !=null) {
            return requestId;
        } else {
            return agentRequestid;
        }
    }

}
