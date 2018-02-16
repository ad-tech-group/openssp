package io.freestar.ssp.dataprovider.provider;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.freestar.ssp.dataprovider.provider.dto.TokenWrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class LoginService extends DataProvider {
    public static final String TOKEN = "liverworst-5";

    //private TokenWrapper data;

    public LoginService(HttpServer server) {
        super(server);
    }

    public void start() throws IOException {
        initData();
        HttpServer server = getServer();
        server.createContext("/ssp-data-provider/login/token", this);
        server.setExecutor(null); // creates a default executor
    }

    private void initData() throws IOException {
        Gson gson = new Gson();
        String content = new String(Files.readAllBytes(Paths.get("currency_db.json")), StandardCharsets.UTF_8);
        //data = gson.fromJson(content, TokenWrapper.class);
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String,String> parms;
        if ("POST".equalsIgnoreCase(httpExchange.getRequestMethod())) {
            String body = queryFromBodyString(httpExchange.getRequestBody());
            parms = attributesToMap(httpExchange);
            populateFromBody(parms, body);
        } else {
            parms = queryToMap(httpExchange.getRequestURI().getRawQuery());
        }
        String user = parms.get("u");
        String pw = parms.get("p");
        if (!isAuthorized(user, pw)) {
            httpExchange.sendResponseHeaders(401, 0);
        } else {
            TokenWrapper token = new TokenWrapper();
            token.setToken(TOKEN);
            String result = new Gson().toJson(token);
            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF8");
            httpExchange.sendResponseHeaders(200, result.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        }
    }

    private boolean isAuthorized(String user, String pw) {
        return user != null && "izod".equals(user) && pw != null && "frogs".equals(pw);
    }

}