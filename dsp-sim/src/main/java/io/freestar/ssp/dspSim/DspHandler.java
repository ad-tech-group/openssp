package io.freestar.ssp.dspSim;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class DspHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // method: post
        // writeFinished: false
        // uri: /dsp-sim/DemandService
        // reqContentLen: 724

        // _REQ_HEADERS_
        // Accept-encoding: gzip,deflate
        // Connection: Keep-Alive
        // Host: localhost:8082
        // User-agent: Apache-HttpClient/4.5.2 (Java/1.8.0_161)
        // Content-type: application/json; charset=UTF-8
        // Contenttype: application/json
        // X-openrtb-version: 2.2
        // Content-length: 724

        BidRequest brq = new Gson().fromJson(new InputStreamReader(httpExchange.getRequestBody()), BidRequest.class);
        System.out.println("-->"+new Gson().toJson(brq));

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

        String result = new Gson().toJson(brsp);
        System.out.println("<--"+result);
        httpExchange.sendResponseHeaders(200, result.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(result.getBytes());
        os.close();


    }
}
