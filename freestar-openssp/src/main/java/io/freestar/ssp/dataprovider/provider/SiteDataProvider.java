package io.freestar.ssp.dataprovider.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.freestar.ssp.dataprovider.provider.handler.SiteDataHandler;

import java.io.IOException;

public class SiteDataProvider extends DataProvider {

    public SiteDataProvider(HttpServer server) {
        super(server);
    }

    public void start() throws IOException {
        HttpServer server = getServer();
        server.createContext("/open-ssp-services"+SiteDataHandler.CONTEXT, this);
        server.setExecutor(null); // creates a default executor
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        SiteDataHandler handler = new SiteDataHandler(httpExchange);
    }

}