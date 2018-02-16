package io.freestar.ssp.dataprovider.provider;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.freestar.ssp.dataprovider.provider.dto.CurrencyDto;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class CurrencyDataProvider extends DataProvider {

    private CurrencyDto data;

    public CurrencyDataProvider(HttpServer server) {
        super(server);
    }

    public void start() throws IOException {
        initData();
        HttpServer server = getServer();
        server.createContext("/ssp-data-provider/lookup/eurref", this);
        server.setExecutor(null); // creates a default executor
    }

    private void initData() throws IOException {
        Gson gson = new Gson();
        String content = new String(Files.readAllBytes(Paths.get("currency_db.json")), StandardCharsets.UTF_8);
        data = gson.fromJson(content, CurrencyDto.class);
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        Map <String,String>parms = queryToMap(httpExchange.getRequestURI().getQuery());

        String t = parms.get("t");
        if (LoginService.TOKEN.equals(t)) {
            String result = new Gson().toJson(data);
            System.out.println("<--"+result);
            httpExchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF8");
            httpExchange.sendResponseHeaders(200, result.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(result.getBytes());
            os.close();
        } else {
            httpExchange.sendResponseHeaders(401, 0);
        }
    }

}