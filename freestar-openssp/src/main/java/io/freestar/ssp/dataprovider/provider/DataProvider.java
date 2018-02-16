package io.freestar.ssp.dataprovider.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public abstract class DataProvider implements HttpHandler {
    private final HttpServer server;

    public DataProvider(HttpServer server) {
        this.server = server;
    }

    protected HttpServer getServer() {
        return server;
    }

    protected static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        if (query != null) {
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                if (pair.length>1) {
                    result.put(pair[0], pair[1]);
                }else{
                    result.put(pair[0], "");
                }
            }
        }
        return result;
    }

    protected static Map<String, String> attributesToMap(HttpExchange exchange){
        Map<String, String> result =  (Map<String, String>) exchange.getAttribute("attributes");
        if (result == null) {
            result = new HashMap<String, String>();
        }
        return result;
    }

    protected static String queryFromBodyString(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String query;
        try {
            query = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            query="";
        }
        return query;
    }

    protected void populateFromBody(Map<String, String> parameters, String query) {
        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    try {
                        key = URLDecoder.decode(param[0], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        key = param[0];
                    }
                }

                if (param.length > 1) {
                    try {
                        value = URLDecoder.decode(param[1], "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        value = param[1];
                    }
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if(obj instanceof List<?>) {
                        List<String> values = (List<String>)obj;
                        values.add(value);
                    } else if(obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String)obj);
                        values.add(value);
//                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

    public abstract void start() throws IOException;
}
