package com.atg.openssp.dspSim;

import com.atg.openssp.common.logadapter.RtbRequestLogProcessor;
import com.atg.openssp.common.logadapter.RtbResponseLogProcessor;
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
        BidRequest brq = new Gson().fromJson(new InputStreamReader(httpExchange.getRequestBody()), BidRequest.class);
        RtbRequestLogProcessor.instance.setLogData(new Gson().toJson(brq), "bidrequest", httpExchange.getRemoteAddress().getHostName());
        BidResponse brsp = model.createBidResponse(httpExchange.getLocalAddress().getHostName(), httpExchange.getLocalAddress().getPort(), brq);

        if (brsp.getSeatbid().size() > 0 || true) {


            String result = model.filterResult(brsp);
            RtbResponseLogProcessor.instance.setLogData(result, "bidresponse", httpExchange.getRemoteAddress().getHostName());
            httpExchange.sendResponseHeaders(200, result.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            httpExchange.sendResponseHeaders(201, 0);
        }


    }

}
