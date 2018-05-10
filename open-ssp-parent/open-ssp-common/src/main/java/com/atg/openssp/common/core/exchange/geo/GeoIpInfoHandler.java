package com.atg.openssp.common.core.exchange.geo;

import openrtb.bidrequest.model.GeoIpInfo;

import java.io.IOException;

public abstract class GeoIpInfoHandler {
    public abstract GeoIpInfo lookupGeoInfo(String ipAddress) throws IOException, AddressNotFoundException, UnavailableHandlerException;
}
