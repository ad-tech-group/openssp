package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ad.AdModel;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.atg.openssp.dspSim.channel.adserving.AdservingCampaignProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class VideoAdServerHandler implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(VideoAdServerHandler.class);
    private final AdModel model;

    public VideoAdServerHandler(AdModel model) {
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
            log.error(ex.getMessage(), ex);
        }
        log.info("AD-->"+new Gson().toJson(input));

        AdservingCampaignProvider p = new AdservingCampaignProvider();
        p.setIsValid(true);
        p.setPrice(40f);
        p.setAdjustedCurrencyPrice(30f);

        String result = new Gson().toJson(p);
        log.info("<--"+result);
        httpExchange.sendResponseHeaders(200, result.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(result.getBytes());
        os.close();

    }
}
