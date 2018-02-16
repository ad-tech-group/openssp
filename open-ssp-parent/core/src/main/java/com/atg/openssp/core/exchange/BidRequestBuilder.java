package com.atg.openssp.core.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;
import openrtb.bidrequest.model.*;
import openrtb.tables.VideoBidResponseProtocol;

/**
 * RequestBuilder builds the BidRequest Object for RTB Exchange.
 *
 * @author André Schmer
 */
public class BidRequestBuilder {

    /**
     * Build a request object regarding to the OpenRTB Specification.
     *
     * @return {@see BidRequest}
     */
    public static BidRequest build(final SessionAgent agent) {

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

}
