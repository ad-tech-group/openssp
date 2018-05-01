package io.freestar.ssp.exchange.geo;

import com.atg.openssp.common.core.exchange.geo.AddressNotFoundException;
import com.atg.openssp.common.core.exchange.geo.GeoIpInfoHandler;
import com.atg.openssp.common.core.exchange.geo.UnavailableHandlerException;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import openrtb.bidrequest.model.GeoIpInfo;
import openrtb.tables.IpServiceType;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class GeoIp2InfoHandler extends GeoIpInfoHandler {
    private final String USER_AGENT = "Mozilla/5.0";
    //https://maxmind.github.io/GeoIP2-java/

    public GeoIp2InfoHandler() {

    }

    @Override
    public GeoIpInfo lookupGeoInfo(String ipAddressString) throws IOException, AddressNotFoundException, UnavailableHandlerException {
// A File object pointing to your GeoIP2 or GeoLite2 database
        File database = new File("/opt/GeoLite2_data/GeoLite2-City.mmdb");

// This creates the DatabaseReader object. To improve performance, reuse
// the object across lookups. The object is thread-safe.
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName(ipAddressString);

// Replace "city" with the appropriate method for your database, e.g.,
// "country".
        GeoIpInfo fdii = new GeoIpInfo();
        try {
            /*
    private String country_code;
    private String country_name;

    private String region_code;
    private String region_name;
    private String time_zone;
    private String metro_code;
             */
            CityResponse response = reader.city(ipAddress);
            fdii.setIpServiceType(IpServiceType.MAXMIND);
            fdii.setIp(ipAddressString);
            Country country = response.getCountry();
            fdii.setCountryCode(country.getIsoCode());
            fdii.setCountryName(country.getName());

//            Subdivision subdivision = response.getMostSpecificSubdivision();
//            subdivision.getName());    // 'Minnesota'
//            subdivision.getIsoCode()); // 'MN'

            City city = response.getCity();
            fdii.setCity(city.getName());

            Postal postal = response.getPostal();
            fdii.setZip(postal.getCode());

            fdii.setTimeZone(response.getLocation().getTimeZone());
            Location location = response.getLocation();
            fdii.setLat(location.getLatitude().floatValue());
            fdii.setLon(location.getLongitude().floatValue());
        } catch (com.maxmind.geoip2.exception.AddressNotFoundException e) {
            throw new AddressNotFoundException();
        } catch (GeoIp2Exception e) {
            throw new UnavailableHandlerException(e.getMessage());
        }


        return fdii;
    }

}
