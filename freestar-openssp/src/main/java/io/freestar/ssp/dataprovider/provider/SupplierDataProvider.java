package io.freestar.ssp.dataprovider.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.freestar.ssp.dataprovider.provider.handler.SupplierDataHandler;

import java.io.IOException;

public class SupplierDataProvider extends DataProvider {

    public SupplierDataProvider(HttpServer server) {
        super(server);
    }

    public void start() throws IOException {
        HttpServer server = getServer();
        server.createContext("/open-ssp-services"+ SupplierDataHandler.CONTEXT, this);
        server.setExecutor(null); // creates a default executor
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        SupplierDataHandler handler = new SupplierDataHandler(httpExchange);
    }

}