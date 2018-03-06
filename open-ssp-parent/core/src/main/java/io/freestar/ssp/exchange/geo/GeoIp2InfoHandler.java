package io.freestar.ssp.exchange.geo;

import com.atg.openssp.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.core.exchange.geo.UnavailableHandlerException;
import com.google.gson.Gson;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import openrtb.bidrequest.model.GeoIpInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class GeoIp2InfoHandler extends GeoIpInfoHandler {
    private final String USER_AGENT = "Mozilla/5.0";
    //https://maxmind.github.io/GeoIP2-java/

    public GeoIp2InfoHandler() {

    }

    @Override
    public GeoIpInfo lookupGeoInfo(String ipAddressString) throws IOException, UnavailableHandlerException {
// A File object pointing to your GeoIP2 or GeoLite2 database
        File database = new File("/geo/db/GeoIP2-City.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName(ipAddressString);

// Replace "city" with the appropriate method for your database, e.g.,
// "country".
        GeoIpInfo fdii = new GeoIpInfo();
        try {
            CityResponse response = reader.city(ipAddress);
            Country country = response.getCountry();
            fdii.setCountryCode(country.getIsoCode());
            fdii.setCountryName(country.getName());

//            Subdivision subdivision = response.getMostSpecificSubdivision();
//            System.out.println(subdivision.getName());    // 'Minnesota'
//            System.out.println(subdivision.getIsoCode()); // 'MN'

            City city = response.getCity();
            fdii.setCity(city.getName());

            Postal postal = response.getPostal();
            fdii.setZip(postal.getCode());

            Location location = response.getLocation();
            fdii.setLat(location.getLatitude().floatValue());
            fdii.setLon(location.getLongitude().floatValue());
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }


        return fdii;
    }

}
