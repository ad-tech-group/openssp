package com.atg.openssp.core.exchange;

import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.TestParamValue;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.exchange.geo.FreeGeoIpInfoHandler;
import com.atg.openssp.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.core.exchange.geo.UnavailableHandlerException;
import openrtb.bidrequest.model.*;
import openrtb.tables.VideoBidResponseProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TestBidRequestBuilderHandler extends BidRequestBuilderHandler {
    private final Logger log = LoggerFactory.getLogger(TestBidRequestBuilderHandler.class);
    private GeoIpInfoHandler geoIpInfoHandler;

    public TestBidRequestBuilderHandler() {

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
        TestParamValue pValues = null;
        try {
            pValues = (TestParamValue) agent.getParamValues();
        } catch (RequestException e) {
            log.warn(e.getMessage(), e);
            pValues = new TestParamValue();
        }

        return new BidRequest.Builder()
                .setId(agent.getRequestid())
                .setSite(pValues.getSite())
                .setDevice(
                        new Device.Builder()
                                .setGeo(createSiteGeo(pValues))
                                .build()
                ).addImp(
                        new Impression.Builder()
                                .setId("1")
                                .setVideo(new Video.Builder()
                                        .addMime("application/x-shockwave-flash")
                                        .setH(400)
                                        .setW(600)
                                        .setMaxduration(100)
                                        .setMinduration(30)
                                        .addProtocol(VideoBidResponseProtocol.VAST_2_0.getValue())
                                        .setStartdelay(1)
                                        .build()
                                ).build())
                .setUser(
                        new User.Builder()
                                .setBuyeruid("HHcFrt-76Gh4aPl")
                                .setGender(Gender.MALE)
                                .setId("99")
                                .setYob(1981)
//                                        .setGeo()
                                .build()
                ).build();

    }

    private Geo createSiteGeo(TestParamValue pValues) {
        Geo geo = new Geo.Builder().build();
        //geo.setCity(pValues.getCity());

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
}
