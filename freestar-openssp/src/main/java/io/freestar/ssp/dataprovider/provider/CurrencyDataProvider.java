package io.freestar.ssp.dataprovider.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.freestar.ssp.dataprovider.provider.handler.CurrencyDataHandler;

import java.io.IOException;

public class CurrencyDataProvider extends DataProvider {

    public CurrencyDataProvider(HttpServer server) {
        super(server);
    }

    public void start() throws IOException {
        HttpServer server = getServer();
        server.createContext("/open-ssp-services"+ CurrencyDataHandler.CONTEXT, this);
        server.setExecutor(null); // creates a default executor
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        CurrencyDataHandler handler = new CurrencyDataHandler(httpExchange);
    }

}