package com.atg.openssp.dspSim;

import com.atg.openssp.common.logadapter.RtbRequestLogProcessor;
import com.atg.openssp.common.logadapter.RtbResponseLogProcessor;
import com.atg.openssp.dspSim.model.client.ClientCommandType;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.BidResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

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
        BufferedReader br = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
        String line;
        StringBuilder rawRequest = new StringBuilder();
        while((line = br.readLine()) != null) {
            rawRequest.append(line+"\n");
        }
        RtbRequestLogProcessor.instance.setLogData(rawRequest.toString(), "bidrequest", httpExchange.getRemoteAddress().getHostName());
        log.info("-->"+rawRequest);
        if (model.getMode() == ClientCommandType.RETURN_NONE) {
            httpExchange.sendResponseHeaders(200, 0);
        } else if (model.getMode() == ClientCommandType.ONLY_400) {
            httpExchange.sendResponseHeaders(400, 0);
        } else if (model.getMode() == ClientCommandType.ONLY_500) {
            httpExchange.sendResponseHeaders(500, 0);
        }
        try {
            BidRequest brq = new Gson().fromJson(rawRequest.toString(), BidRequest.class);
            BidResponse brsp = model.createBidResponse(httpExchange.getLocalAddress().getHostName(), httpExchange.getLocalAddress().getPort(), brq);
            if (brsp.getSeatbid().size() > 0 || true) {
                String result = model.filterResult(brsp);
                RtbResponseLogProcessor.instance.setLogData(result, "bidresponse", httpExchange.getRemoteAddress().getHostName());
                log.info("<--"+result);
                httpExchange.sendResponseHeaders(200, result.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(result.getBytes());
                os.close();
            } else {
                httpExchange.sendResponseHeaders(201, 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            httpExchange.sendResponseHeaders(405, 0);
        }



    }

}
