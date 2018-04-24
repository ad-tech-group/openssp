package com.atg.openssp.common.core.exchange;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.cache.type.PricelayerCache;
import com.atg.openssp.common.core.exchange.geo.AddressNotFoundException;
import com.atg.openssp.common.core.exchange.geo.FreeGeoIpInfoHandler;
import com.atg.openssp.common.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.common.core.exchange.geo.UnavailableHandlerException;
import com.atg.openssp.common.demand.TestParamValue;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import openrtb.bidrequest.model.*;
import openrtb.tables.GeoType;
import openrtb.tables.VideoBidResponseProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;

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
    public BidRequest constructRequest(RequestSessionAgent agent) {
        TestParamValue pValues = null;
        try {
            pValues = (TestParamValue) agent.getParamValues();
        } catch (RequestException e) {
            log.warn(e.getMessage(), e);
            pValues = new TestParamValue();
        }

        Site site = pValues.getSite();
        float workingBidfloor;
        String workingBidfloorcur;
        try {
            workingBidfloor = PricelayerCache.instance.get(site.getId()).getBidfloor();
            workingBidfloorcur = PricelayerCache.instance.get(site.getId()).getCurrency();
        } catch (EmptyCacheException e) {
            log.info("price floor does not exist for site: "+site.getId());
            workingBidfloor = 0f;
            workingBidfloorcur = CurrencyCache.instance.getBaseCurrency();
        }

        return new BidRequest.Builder()
                .setId(agent.getRequestid())
                .setAt(agent.getBiddingServiceInfo().getAuctionType())
                .setSite(site)
                .setApp(pValues.getApp())
                .setDevice(
                        new Device.Builder()
                                .setGeo(createSiteGeo(pValues))
                                .setUa(pValues.getBrowserUserAgentString())
                                .setIp(pValues.getIpAddress())
                                .build()
                ).addImp(
                        new Impression.Builder()
                                .setId("1")
                                .setBidfloor(workingBidfloor)
                                .setBidfloorcurrency(workingBidfloorcur)
                                .setVideo(new Video.Builder()
                                        .addMime("application/x-shockwave-flash")
                                        .setH(400)
                                        .setW(600)
                                        .setMaxduration(100)
                                        .setMinduration(30)
                                        .addToProtocols(VideoBidResponseProtocol.VAST_2_0)
                                        .setStartdelay(1)
                                        .build()
                                ).build())
                .setUser(
                        new User.Builder()
                                .setBuyeruid("HHcFrt-76Gh4aPl")
                                .setGender(Gender.MALE)
                                .setId("99")
                                .setYob(1981)
//                                .setCur()
//                                .setTmax()
//                                        .setGeo()
                                .build()
                )
                .addCur(CurrencyCache.instance.getBaseCurrency())
                .setTmax((int)GlobalContext.getExecutionTimeout())
                .build();

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
                geo.setType(GeoType.IP);
                //geo.setUtcOffset(?);
                geo.setIpServiceType(geoInfo.getIpServiceType());
                //geo.setExt(?)
            } catch (IOException e) {
                log.warn("could not obtain geo code: "+e.getMessage(), e);
            } catch (UnavailableHandlerException e) {
                log.warn("could not obtain geo code: "+e.getMessage());
            } catch (AddressNotFoundException e) {
                // no new address offerings to help
                // keep what we have and move on
            }
        }
        return geo;
    }
}
