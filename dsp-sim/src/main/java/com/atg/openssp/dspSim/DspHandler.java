package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.BidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Brian Sorensen
 */
public class DspHandler implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(DspHandler.class);
    private final DspModel model;

    public DspHandler(DspModel model) {
        this.model = model;
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
        log.info("-->"+new Gson().toJson(brq));

        BidResponse brsp = model.createBidResponse(brq);

        if (brsp.getSeatbid().size() > 0 || true) {
            String result = new Gson().toJson(brsp);
            log.info("<--"+result);
            httpExchange.sendResponseHeaders(200, result.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            httpExchange.sendResponseHeaders(201, 0);
        }


    }

}
