package com.atg.openssp.dspSim.model.dsp.filter;

import com.google.gson.Gson;
import openrtb.bidresponse.model.BidResponse;

public class PassthroughFilter extends DspReturnFilter {
    @Override
    public String filterResult(BidResponse brsp) {
        return new Gson().toJson(brsp);
    }
}
