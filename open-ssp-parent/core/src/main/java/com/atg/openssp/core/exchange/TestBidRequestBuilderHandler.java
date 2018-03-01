package com.atg.openssp.core.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.RequestException;
import io.freestar.ssp.common.demand.FreestarParamValue;
import openrtb.bidrequest.model.*;
import openrtb.tables.VideoBidResponseProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBidRequestBuilderHandler extends BidRequestBuilderHandler {
    private final Logger log = LoggerFactory.getLogger(TestBidRequestBuilderHandler.class);

    @Override
    public BidRequest constructRequest(SessionAgent agent) {
        ParamValue pValues = null;
        try {
            pValues = agent.getParamValues();
        } catch (RequestException e) {
            log.warn(e.getMessage(), e);
            pValues = new FreestarParamValue();
        }

        return new BidRequest.Builder()
                .setId(agent.getRequestid())
                .setSite(pValues.getSite())
                .setDevice(
                        new Device.Builder()
                                .setGeo(
                                        new Geo.Builder()
//                                                        .setCity(agent.getParamValues().getCity())
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

    }
}
