package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.channel.adserving.AdservingCampaignProvider;
import com.atg.openssp.dspSim.model.ad.AdModel;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class AdServerHandler implements HttpHandler {
    private final AdModel model;

    public AdServerHandler(AdModel model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        StringBuilder input = new StringBuilder();
        try {
            BufferedReader is = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
            String line;
            while((line = is.readLine()) != null) {
                input.append(line+"\n");
            }
        } catch (Exception ex) {

        }
        System.out.println("AD-->"+new Gson().toJson(input));

        //BidRequest brq = new Gson().fromJson(new InputStreamReader(httpExchange.getRequestBody()), BidRequest.class);
        //System.out.println("AD-->"+new Gson().toJson(brq));

        AdservingCampaignProvider p = new AdservingCampaignProvider();
        p.setIsValid(true);
        p.setPrice(40f);
        p.setPriceEur(30f);
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
