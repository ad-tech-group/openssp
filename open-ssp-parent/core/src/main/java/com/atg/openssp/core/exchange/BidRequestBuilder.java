package com.atg.openssp.core.exchange;

import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.core.system.loader.LocalContextLoader;
import openrtb.bidrequest.model.*;
import openrtb.tables.VideoBidResponseProtocol;

/**
 * RequestBuilder builds the BidRequest Object for RTB Exchange.
 *
 * @author André Schmer
 */
public final class BidRequestBuilder {
    private static BidRequestBuilder instance;
    private BidRequestBuilderHandler handler;

    private BidRequestBuilder() {
        String handlerClassName = GlobalContext.getBidRequestBuilderHandlerClass();
        if (handlerClassName != null && !"".equals(handlerClassName)) {
            try {
                Class c = Class.forName(handlerClassName);
                handler = (BidRequestBuilderHandler) c.getConstructor(null).newInstance(null);
            } catch (Exception e) {
                handler = new TestBidRequestBuilderHandler();
                e.printStackTrace();
            }
        } else {
            handler = new TestBidRequestBuilderHandler();
        }

    }

    /**
     * Build a request object regarding to the OpenRTB Specification.
     *
     * @return {@see BidRequest}
     */
    public BidRequest build(final SessionAgent agent) {

        final BidRequest bidRequest =
                new BidRequest.Builder()
                        .setId(agent.getRequestid())
                        .setSite(agent.getParamValues().getSite())
                        .setDevice(
                                new Device.Builder()
                                        .setGeo(
                                                new Geo.Builder()
//                                                        .setCity(agent.getParamValues().getCity())
//                                                        .setCountry(agent.getParamValues().getCountry())
//                                                        .setLat(agent.getParamValues().getLat())
//                                                        .setLon(agent.getParamValues().getLon())
//                                                        .setZip(agent.getParamValues().getZip())
                                                        .build())
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

        return bidRequest;
    }

    public synchronized static BidRequestBuilder getInstance() {
        if (instance == null) {
            instance = new BidRequestBuilder();
        }
        return instance;
    }
}
