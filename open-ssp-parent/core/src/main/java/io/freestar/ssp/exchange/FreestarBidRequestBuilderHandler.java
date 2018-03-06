package io.freestar.ssp.exchange;

import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.exchange.BidRequestBuilderHandler;
import com.atg.openssp.core.exchange.geo.FreeGeoIpInfoHandler;
import com.atg.openssp.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.core.exchange.geo.UnavailableHandlerException;
import openrtb.bidrequest.model.GeoIpInfo;
import io.freestar.ssp.common.demand.FreestarParamValue;
import openrtb.bidrequest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public class FreestarBidRequestBuilderHandler extends BidRequestBuilderHandler {
    private final Logger log = LoggerFactory.getLogger(FreestarBidRequestBuilderHandler.class);
    private final Base64.Decoder decoder;
    private GeoIpInfoHandler geoIpInfoHandler;


    public FreestarBidRequestBuilderHandler() {
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
    public BidRequest constructRequest(SessionAgent agent) {
        FreestarParamValue pValues = null;
        try {
            pValues = (FreestarParamValue) agent.getParamValues();
        } catch (RequestException e) {
            log.warn(e.getMessage(), e);
            pValues = new FreestarParamValue();
        }

        Site site = pValues.getSite().clone();
        String requestId = pValues.getRequestId();

        Device dd = new Device.Builder().build();
        dd.setGeo(createSiteGeo(pValues));

        Impression i = new Impression.Builder().build();
        i.setId(pValues.getId());
                /*
                i..setVideo(new Video.Builder()
                        .addMime("application/x-shockwave-flash")
                        .setH(400)
                        .setW(600)
                        .setMaxduration(100)
                        .setMinduration(30)
                        .addProtocol(VideoBidResponseProtocol.VAST_2_0.getValue())
                        .setStartdelay(1)
                        .build()
                )
                */

        i.setBanner(createBanner(pValues));

        return new BidRequest.Builder()
                .setId(selectAppropriateId(requestId, agent.getRequestid()))
                .setSite(site)
                .setDevice(dd)

                .addImp(i)
                .setUser(
                        new User.Builder()
                                //.setBuyeruid()
                                //.setGender(Gender.MALE)
                                .setId(pValues.getFsUid())
                                //.setYob(1981)
                                //.setGeo()
                                .build()
                ).build();
    }

    private Geo createSiteGeo(FreestarParamValue pValues) {
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
                geo.setType(Geo.TYPE_IP);
            } catch (IOException e) {
                log.warn("could not obtain geo code: "+e.getMessage(), e);
            } catch (UnavailableHandlerException e) {
                log.warn("could not obtain geo code: "+e.getMessage());
            }
        }
        return geo;
    }

    private Banner createBanner(FreestarParamValue pValues) {
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

//        b.setBattr();
//        b.setApi();
//        b.setBtype();
//        b.setExpdir();
//        b.setMimes();
//        b.setPos();
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
