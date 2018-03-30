package com.atg.openssp.core.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.exception.RequestException;
import openrtb.bidrequest.model.BidRequest;

public abstract class BidRequestBuilderHandler {
    public abstract BidRequest constructRequest(RequestSessionAgent agent) throws RequestException;
}
