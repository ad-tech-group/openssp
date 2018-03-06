package io.freestar.ssp.exchange.geo;

import com.atg.openssp.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.core.exchange.geo.UnavailableHandlerException;
import com.google.gson.Gson;
import openrtb.bidrequest.model.GeoIpInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeoIp2InfoHandler extends GeoIpInfoHandler {
    private final String USER_AGENT = "Mozilla/5.0";
    //https://maxmind.github.io/GeoIP2-java/

    @Override
    public GeoIpInfo lookupGeoInfo(String ipAddress) throws IOException, UnavailableHandlerException {
        URL url = new URL("http://freegeoip.net/json/"+ipAddress);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            throw new UnavailableHandlerException("failed with response code 200");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        Gson gson = new Gson();
        GeoIpInfo fdii = gson.fromJson(in, GeoIpInfo.class);
        in.close();
        return fdii;
    }

}
