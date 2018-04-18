package com.atg.openssp.common.core.exchange.channel.rtb;

import com.google.gson.Gson;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.BidResponse;

public abstract class DemandBrokerFilter {
    public abstract String filterRequest(Gson gson, BidRequest bidrequest);

    public abstract BidResponse filterResponse(Gson gson, String response);
}
