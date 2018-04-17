package com.atg.openssp.common.core.exchange;

import com.atg.openssp.common.exception.RequestException;
import openrtb.bidrequest.model.BidRequest;

public abstract class BidRequestBuilderHandler {
    public abstract BidRequest constructRequest(RequestSessionAgent agent) throws RequestException;
}
