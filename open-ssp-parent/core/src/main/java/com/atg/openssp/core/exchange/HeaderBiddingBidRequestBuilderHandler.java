package com.atg.openssp.core.exchange;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.cache.dto.VideoAd;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.cache.type.PricelayerCache;
import com.atg.openssp.common.core.exchange.BidRequestBuilderHandler;
import com.atg.openssp.common.core.exchange.RequestSessionAgent;
import com.atg.openssp.common.core.exchange.geo.AddressNotFoundException;
import com.atg.openssp.common.core.exchange.geo.FreeGeoIpInfoHandler;
import com.atg.openssp.common.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.common.core.exchange.geo.UnavailableHandlerException;
import com.atg.openssp.common.demand.HeaderBiddingParamValue;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.ERROR_CODE;
import com.atg.openssp.common.exception.EmptyCacheException;
import com.atg.openssp.common.exception.RequestException;
import openrtb.bidrequest.model.*;
import openrtb.tables.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author bsorensen
 */
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

        BidRequest bidRequest = new BidRequest.Builder()
                .setId(selectAppropriateId(requestId, agent.getRequestid()))
                .setAt(agent.getBiddingServiceInfo().getAuctionType())
                .setSite(site)
                .setApp(app)
                .setDevice(dd)
                .setUser(createUser(masterValues))
                .addCur(CurrencyCache.instance.getBaseCurrency())
                //TODO: BKS
                //.setBadv()
                //.setBcat()
                // set tmax temporarily - set in DemandService (supplier info)
                .setTmax((int) GlobalContext.getExecutionTimeout())
                // set test temporarily - set in DemandService (supplier info)
                .setTest(BooleanInt.FALSE)
//                .setExtension()
                .build();

        /*
	private List<String> badv;
	private List<String> bcat;
	private Object ext;
         */

        int idCount = 1;
        for (ParamValue pOrigin : pValueList) {
            HeaderBiddingParamValue pValues = (HeaderBiddingParamValue) pOrigin;

            Impression i = new Impression.Builder().build();
            i.setId(pValues.getId());
            //i.setVideo(createVideo(pValues));
            i.setBanner(createBanner(pValues));
            //i.setNative(createNative(pValues));

            Float overrideBidFloor = pValues.getOverrideBidFloor();
            if (overrideBidFloor != null) {
                i.setBidfloor(overrideBidFloor.floatValue());
                try {
                    i.setBidfloorcur(PricelayerCache.instance.get(site.getId()).getCurrency());
                } catch (EmptyCacheException e) {
                    log.info("price floor does not exist for site: " + site.getId());
                    i.setBidfloorcur(CurrencyCache.instance.getBaseCurrency());
                }
            } else {
                try {
                    i.setBidfloor(PricelayerCache.instance.get(site.getId()).getBidfloor());
                    i.setBidfloorcur(PricelayerCache.instance.get(site.getId()).getCurrency());
                } catch (EmptyCacheException e) {
                    log.info("price floor does not exist for site: " + site.getId());
                    i.setBidfloor(0f);
                    i.setBidfloorcur(CurrencyCache.instance.getBaseCurrency());
                }
            }
            i.setSecure(ImpressionSecurity.NON_SECURE);
            bidRequest.addImp(i);

        }

        return bidRequest;
    }

    private User createUser(HeaderBiddingParamValue pValues) {
        String userId = pValues.getUid();

        return new User.Builder()
                //               .setBuyeruid()
                //.setGender(pValues.getGender())
                .setId(userId)
                //.setYob(pValues.getYearOfBirth())
                //.setGeo(createUserGeo(pValues))
                .build();

    }

    private Native createNative(HeaderBiddingParamValue pValues) {
        return null;
        // Native not implemeneted yet.  We need a way to specify type
    }

    private Video createVideo(HeaderBiddingParamValue hValues) {
        VideoAd ad = hValues.getVideoad();
        Video v = new Video();
        List<String> mimes = ad.getMimes();
        if (mimes != null) {
            v.setMimes(mimes);
        }
        Integer minduration = ad.getMinDuration();
        if (minduration != null) {
            v.setMinduration(minduration);
        }
        Integer maxduration = ad.getMaxDuration();
        if (maxduration != null) {
            v.setMaxduration(maxduration);
        }
        Integer w = ad.getW();
        if (w != null) {
            v.setW(w);
        }
        Integer h = ad.getH();
        if (h != null) {
            v.setH(h);
        }
        Integer startdelay = ad.getStartDelay();
        if (startdelay != null) {
            v.setStartdelay(startdelay);
        }
        List<VideoBidResponseProtocol> protocols = ad.getProtocols();
        if (protocols != null) {
            for (VideoBidResponseProtocol i : protocols) {
                v.addToProtocols(i);
            }
        }
        List<CreativeAttribute> battr = ad.getBattr();
        if (battr != null) {
            for (CreativeAttribute i : battr) {
                v.addBattr(i);
            }
        }
        VideoLinearity linearity = ad.getLinearity();
        if (linearity != null) {
            v.setLinearity(linearity);
        }
        List<Banner> companionad = ad.getCompanionad();
        if (companionad != null) {
            for (Banner i : companionad) {
                v.addCompanionad(i);
            }
        }
        List<ApiFramework> api = ad.getApi();
        if (api != null) {
            for (ApiFramework i : api) {
                v.addApi(i);
            }
        }
        Object ext = ad.getExt();
        if (ext != null) {
            v.setExt(ext);
        }
        return v;
    }

    private Banner createBanner(HeaderBiddingParamValue hValues) {
        BannerAd ad = hValues.getBannerad();
        Banner b = new Banner.Builder().setId(ad.getPlacementId()).build();
        ArrayList<Banner.BannerSize> sizes = new ArrayList();
        Banner.BannerSize size = new Banner.BannerSize(ad.getSize().toLowerCase());
        b.setW(size.getW());
        b.setH(size.getH());
        sizes.add(size);

        String promoSizesString = ad.getPromoSizes();
        if (promoSizesString != null) {
            StringTokenizer st = new StringTokenizer(promoSizesString, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                Banner.BannerSize ts = new Banner.BannerSize(token);
                if (!sizes.contains(ts)) {
                    sizes.add(ts);
                }
            }
        }
        Collections.sort(sizes);
        b.setWmin(sizes.get(0).getW());
        b.setHmin(sizes.get(0).getH());
        b.setWmax(sizes.get(sizes.size() - 1).getW());
        b.setHmax(sizes.get(sizes.size() - 1).getH());
        b.setFormat(sizes.toArray());

        b.setAllBtype(ad.getBtypes());
        b.setAllBattr(ad.getBattrs());
        b.setApis(ad.getApi());
        b.setAllExpdir(ad.getExpdir());
        b.setMimes(ad.getMimes());
        //BKS TODO:
//        b.setPos(AddPosition.FOOTER);
        b.setTopframe(ad.getTopframe());
        b.setExt(ad.getExt());

        return b;
    }

    private Geo createSiteGeo(HeaderBiddingParamValue pValues) {
        Geo geo = new Geo.Builder().build();
        try {
            StringTokenizer st = new StringTokenizer(pValues.getLoc(), "?&");
            while (st.hasMoreTokens()) {
                String t = st.nextToken();
                if (t.startsWith("i=")) {
                    geo.setCountry(t.substring(2));
                } else if (t.startsWith("c=")) {
                    geo.setCity(new String(decoder.decode(t.substring(2).getBytes())));
                }
            }
        } catch (Exception ex) {
            log.error("missing geo code properties: " + pValues.getLoc());
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
                log.warn("could not obtain geo code: " + e.getMessage(), e);
                // no new address offerings to help
                // keep what we have and move on
            } catch (UnavailableHandlerException e) {
                log.warn("could not obtain geo code: " + e.getMessage());
                // no new address offerings to help
                // keep what we have and move on
            } catch (AddressNotFoundException e) {
                // no new address offerings to help
                // keep what we have and move on
            }
        }
        return geo;
    }

    private String selectAppropriateId(String requestId, String agentRequestid) {
        if (requestId != null) {
            return requestId;
        } else {
            return agentRequestid;
        }
    }

}
