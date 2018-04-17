package com.atg.openssp.common.core.exchange.channel.rtb;

import com.google.gson.Gson;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.BidResponse;

public class PassthroughFilter extends DemandBrokerFilter {
    public PassthroughFilter() {
    }

    @Override
    public String filterRequest(Gson gson, BidRequest bidrequest) {
        return gson.toJson(bidrequest, BidRequest.class);
    }

    @Override
    public BidResponse filterResponse(Gson gson, String response) {
        return gson.fromJson(response, BidResponse.class);
    }
}
