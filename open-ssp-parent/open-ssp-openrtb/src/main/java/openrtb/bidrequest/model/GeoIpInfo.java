package openrtb.bidrequest.model;

import openrtb.tables.IpServiceType;

public class GeoIpInfo {
    private float latitude;
    private float longitude;
    private String zip_code;
    private String city;
    private String ip;
    private String country_code;
    private String country_name;
    private String region_code;
    private String region_name;
    private String time_zone;
    private String metro_code;
    private IpServiceType ipServiceType;

    public float getLat() {
        return latitude;
    }

    public void setLat(float lat) {
        this.latitude = lat;
    }

    public float getLon() {
        return longitude;
    }

    public void setLon(float lon) {
        this.longitude = lon;
    }

    public String getZip() {
        return zip_code;
    }

    public void setZip(String zip) {
        this.zip_code = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setCountryCode(String countryCode) {
        this.country_code = countryCode;
    }

    public String getCountryCode() {
        return country_code;
    }

    public void setCountryName(String countryName) {
        this.country_name = countryName;
    }

    public String getCountryName() {
        return country_name;
    }

    public void setRegionCode(String regionCode) {
        this.region_code = regionCode;
    }

    public String getRegionCode() {
        return region_code;
    }

    public void setRegionName(String regionName) {
        this.region_name = regionName;
    }

    public String getRegionName() {
        return region_name;
    }

    public void setTimeZone(String timeZone) {
        this.time_zone = timeZone;
    }

    public String getTimeZone() {
        return time_zone;
    }

    public void setMetroCode(String metroCode) {
        this.metro_code = metroCode;
    }

    public String getMetroCode() {
        return metro_code;
    }

    public void setIpServiceType(IpServiceType ipServiceType) {
        this.ipServiceType = ipServiceType;
    }

    public IpServiceType getIpServiceType() {
        return ipServiceType;
    }
}
