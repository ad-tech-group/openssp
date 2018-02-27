package io.freestar.ssp.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.core.exchange.BidRequestBuilderHandler;
import io.freestar.ssp.common.demand.FreestarParamValue;
import openrtb.bidrequest.model.*;
import openrtb.tables.VideoBidResponseProtocol;

public class FreestarBidRequestBuilderHandler extends BidRequestBuilderHandler {
    @Override
    public BidRequest constructRequest(SessionAgent agent) {
        FreestarParamValue pValues = (FreestarParamValue) agent.getParamValues();

        Banner b = new Banner.Builder().setId(pValues.getId()).build();
        System.out.println("szie: "+pValues.getSize());
//        b.setH(pValues.set);
//        b.setW();
//        b.setBattr();
//        b.setApi();
//        b.setBtype();
//        b.setExpdir();
//        b.setHmax();
//        b.setHmin();
//        b.setMimes();
//        b.setPos();
//        b.setTopframe();
//        b.setWmax();
//        b.setWmin();
//        b.setExt();

        return new BidRequest.Builder()
                .setId(agent.getRequestid())
                .setSite(pValues.getSite())
                .setDevice(
                        new Device.Builder()
                                .setGeo(
                                        new Geo.Builder()
//                                                        .setCity(pValues.getCity())
//                                                        .setCountry(pValues.getCountry())
//                                                        .setLat(pValues.getLat())
//                                                        .setLon(pValues.getLon())
//                                                        .setZip(pValues.getZip())
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
                        )
                        .setBanner(b).build())
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
