package io.freestar.ssp.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.exception.RequestException;
import com.atg.openssp.core.exchange.BidRequestBuilderHandler;
import io.freestar.ssp.common.demand.FreestarParamValue;
import openrtb.bidrequest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreestarBidRequestBuilderHandler extends BidRequestBuilderHandler {
    private final Logger log = LoggerFactory.getLogger(FreestarBidRequestBuilderHandler.class);

    @Override
    public BidRequest constructRequest(SessionAgent agent) {
        FreestarParamValue pValues = null;
        try {
            pValues = (FreestarParamValue) agent.getParamValues();
        } catch (RequestException e) {
            log.warn(e.getMessage(), e);
            pValues = new FreestarParamValue();
        }


        Device dd = new Device.Builder().build();
        Geo geo = new Geo.Builder().build();
        /*
        geo.setCity(pValues.getCity());
        geo.setCountry(pValues.getCountry());
        geo.setLat(pValues.getLat());
        geo.setLon(pValues.getLon());
        geo.setZip(pValues.getZip());
        */
        dd.setGeo(geo);

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
                .setId(agent.getRequestid())
                .setSite(pValues.getSite())
                .setDevice(dd)

                .addImp(i)
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

    private Banner createBanner(FreestarParamValue pValues) {
        Banner b = new Banner.Builder().setId(pValues.getId()).build();
        String size = pValues.getSize().toLowerCase();
        int index = size.indexOf('x');
        b.setW(Integer.parseInt(size.substring(0, index)));
        b.setH(Integer.parseInt(size.substring(index+1)));


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
        return b;
    }
}
