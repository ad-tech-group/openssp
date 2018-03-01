package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.atg.openssp.dspSim.view.dsp.DspView;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.BidResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class DspHandler implements HttpHandler {
    private final boolean headless = false;
    private final DspModel model;
    private final DspView view;

    public DspHandler(DspModel model) {
        this.model = model;
        if (!headless) {
            view = new DspView(model);
        }
    }

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

        BidResponse brsp = model.createBidResponse(brq);

        if (brsp.getSeatbid().size() > 0) {
            String result = new Gson().toJson(brsp);
            System.out.println("<--"+result);
            httpExchange.sendResponseHeaders(200, result.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            httpExchange.sendResponseHeaders(201, 0);
        }


    }

}
