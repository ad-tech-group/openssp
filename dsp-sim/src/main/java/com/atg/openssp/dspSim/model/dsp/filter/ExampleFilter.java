package com.atg.openssp.dspSim.model.dsp.filter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import openrtb.bidresponse.model.BidResponse;

public class ExampleFilter extends DspReturnFilter {
    @Override
    public String filterResult(BidResponse brsp) {
        JsonObject filter = new Gson().toJsonTree(brsp).getAsJsonObject();
        /*
        JsonArray sbList = filter.getAsJsonArray("seatbid");
        for (int i=0; i<sbList.size(); i++) {
            JsonObject sb = (JsonObject) sbList.get(i);
            JsonArray bList = sb.getAsJsonArray("bid");
            for (int j=0; j<bList.size(); j++) {
                JsonObject b = (JsonObject) bList.get(i);
                b.remove("attr");
                b.remove("api");
                b.remove("protocol");
//                    b.remove("cat");
            }
            sb.remove("group");
        }
        filter.remove("bidid");
        filter.remove("nbr");
        */
        return new Gson().toJson(filter);
    }
}
