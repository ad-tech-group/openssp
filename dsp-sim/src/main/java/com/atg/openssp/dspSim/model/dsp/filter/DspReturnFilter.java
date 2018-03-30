package com.atg.openssp.dspSim.model.dsp.filter;

import openrtb.bidresponse.model.BidResponse;

public abstract class DspReturnFilter {
    public abstract String filterResult(BidResponse brsp);
}
