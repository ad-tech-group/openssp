package com.atg.openssp.dspSim.model.dsp.filter;

import com.atg.openssp.dspSim.model.dsp.filter.DspReturnFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import openrtb.bidresponse.model.BidResponse;

/**
 * Some DSPs may have a subset of the spec that they return.
 */
public class ExampleSupplierFilter extends DspReturnFilter {
    @Override
    public String filterResult(BidResponse brsp) {
        JsonObject filter = new Gson().toJsonTree(brsp).getAsJsonObject();
        JsonArray sbList = filter.getAsJsonArray("seatbid");
        for (int i=0; i<sbList.size(); i++) {
            JsonObject sb = (JsonObject) sbList.get(i);
            JsonArray bList = sb.getAsJsonArray("bid");
            for (int j=0; j<bList.size(); j++) {
                JsonObject b = (JsonObject) bList.get(i);
                b.remove("w");
                b.remove("h");
                b.remove("attr");
                b.remove("api");
                b.remove("protocol");
            }
            sb.remove("group");
        }
        filter.remove("bidid");
        filter.remove("cur");
        filter.remove("nbr");
        return new Gson().toJson(filter);
    }
}
