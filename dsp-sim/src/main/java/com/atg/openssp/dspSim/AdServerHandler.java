package com.atg.openssp.dspSim;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.atg.openssp.dspSim.channel.adserving.AdservingCampaignProvider;
import openrtb.bidrequest.model.BidRequest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class AdServerHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        BidRequest brq = new Gson().fromJson(new InputStreamReader(httpExchange.getRequestBody()), BidRequest.class);
        System.out.println("AD-->"+new Gson().toJson(brq));

        AdservingCampaignProvider p = new AdservingCampaignProvider();
        p.setIsValid(true);
        p.setPrice(40f);
        p.setNormalizedPrice(30f);
        /*
        BidResponse brsp = new BidResponse();
        brsp.setId(UUID.randomUUID().toString());
        brsp.setBidid(brq.getId());

        SeatBid sb1 = new SeatBid();
        Bid b1 = new Bid();
        b1.setId(UUID.randomUUID().toString());
        b1.setImpid(brq.getImp().get(0).getId());
        b1.setPrice(1.00f);
        sb1.getBid().add(b1);
        brsp.addSeatBid(sb1);

        SeatBid sb2 = new SeatBid();
        Bid b2 = new Bid();
        b2.setId(UUID.randomUUID().toString());
        b2.setImpid(brq.getImp().get(0).getId());
        b2.setPrice(5.00f);
        sb2.getBid().add(b2);
        brsp.addSeatBid(sb2);

//        brsp.setNbr(4);
*/

        String result = new Gson().toJson(p);
        System.out.println("<--"+result);
        httpExchange.sendResponseHeaders(200, result.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(result.getBytes());
        os.close();

    }
}
