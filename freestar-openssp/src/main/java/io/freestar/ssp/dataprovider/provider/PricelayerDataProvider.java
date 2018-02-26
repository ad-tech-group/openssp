package io.freestar.ssp.dataprovider.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.freestar.ssp.dataprovider.provider.handler.PricelayerDataHandler;

import java.io.IOException;

public class PricelayerDataProvider extends DataProvider {

    public PricelayerDataProvider(HttpServer server) {
        super(server);
    }

    public void start() throws IOException {
        HttpServer server = getServer();
        server.createContext("/open-ssp-services"+ PricelayerDataHandler.CONTEXT, this);
        server.setExecutor(null); // creates a default executor
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        PricelayerDataHandler handler = new PricelayerDataHandler(httpExchange);
    }

}