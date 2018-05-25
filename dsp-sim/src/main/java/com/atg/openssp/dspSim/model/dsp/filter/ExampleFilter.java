package com.atg.openssp.dspSim.model.dsp.filter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import openrtb.bidresponse.model.BidResponse;

public class ExampleFilter extends DspReturnFilter {
    @Override
    public String filterResult(BidResponse brsp) {
        JsonObject filter = new Gson().toJsonTree(brsp).getAsJsonObject();
        return new Gson().toJson(filter);
    }
}
