package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Sorensen
 */
public class DspWinHandler implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(DspWinHandler.class);
    private final DspModel model;

    public DspWinHandler(DspModel model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> parms = queryToMap(httpExchange.getRequestURI().getQuery());

        String i = parms.get("i");
        String price = parms.get("price");

        log.info("ad win notification: ID: " + i + " :: PRICE: " + price);

        String result = "";
        httpExchange.sendResponseHeaders(200, result.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(result.getBytes());
        os.close();
    }

    protected static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                } else {
                    result.put(pair[0], "");
                }
            }
        }
        return result;
    }
}
