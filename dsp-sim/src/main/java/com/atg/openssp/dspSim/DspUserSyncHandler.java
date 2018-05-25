package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Brian Sorensen
 */
public class DspUserSyncHandler implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(DspUserSyncHandler.class);
    private final DspModel model;

    public DspUserSyncHandler(DspModel model) {
        this.model = model;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> parms = queryToMap(httpExchange.getRequestURI().getQuery());

        String t = parms.get("t");
        String r = parms.get("r");

        log.info("User Sync: T: " + t + " :: R: " + r);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(r);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

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
