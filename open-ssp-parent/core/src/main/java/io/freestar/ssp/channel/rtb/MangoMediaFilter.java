package io.freestar.ssp.channel.rtb;

import com.atg.openssp.core.exchange.channel.rtb.DemandBrokerFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.BidResponse;

public class MangoMediaFilter extends DemandBrokerFilter {
    @Override
    public String filterRequest(Gson gson, BidRequest bidrequest) {
        JsonObject req = gson.toJsonTree(bidrequest, BidRequest.class).getAsJsonObject();
        JsonObject site = (JsonObject) req.get("site");
        site.remove("id");
        site.remove("name");
        site.remove("domain");
        site.remove("cat");
        site.remove("pagecat");
        site.remove("sectioncat");
        site.remove("ref");
        JsonArray impA = (JsonArray) req.get("imp");
        for (int i=0; i<impA.size(); i++) {
            JsonObject imp = (JsonObject) impA.get(i);
            imp.remove("secure");
            imp.remove("bidfloor");
            imp.remove("bidfloorcur");
            imp.remove("pmp");
            JsonObject banner = (JsonObject) imp.get("banner");
            banner.remove("id");
            banner.remove("pos");
            banner.remove("btype");
            banner.remove("battr");
            banner.remove("mimes");
            banner.remove("topframe");
            banner.remove("wmax");
            banner.remove("hmax");
            banner.remove("wmin");
            banner.remove("hmin");
            banner.remove("format");
        }
        JsonObject device = (JsonObject) req.get("device");
        device.remove("geo");
        device.remove("dnt");
        device.remove("lmt");
        device.remove("devicetype");
        device.remove("h");
        device.remove("w");
        device.remove("ppi");
        device.remove("pxratio");
        device.remove("js");
        device.remove("geofetch");
        device.remove("connectiontype");

        JsonObject user = (JsonObject) req.get("user");
        req.remove("user");

        req.remove("badv");
        req.remove("bcat");
        req.remove("at");
        req.remove("tmax");
        JsonArray cur = (JsonArray) req.get("cur");

        req.remove("cur");
        req.addProperty("test", 1);

        return req.toString();
    }

    @Override
    public BidResponse filterResponse(Gson gson, String response) {
        return gson.fromJson(response, BidResponse.class);
    }
}
