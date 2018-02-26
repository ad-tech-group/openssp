package io.freestar.ssp.dataprovider.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.freestar.ssp.dataprovider.provider.handler.LoginHandler;

import java.io.IOException;

public class LoginService extends DataProvider {
    public static final String TOKEN = "liverworst-5";

    public LoginService(HttpServer server) {
        super(server);
    }

    public void start() throws IOException {
        HttpServer server = getServer();
        server.createContext("/open-ssp-services"+ LoginHandler.CONTEXT, this);
        server.setExecutor(null); // creates a default executor
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        LoginHandler handler = new LoginHandler(httpExchange);
    }

}