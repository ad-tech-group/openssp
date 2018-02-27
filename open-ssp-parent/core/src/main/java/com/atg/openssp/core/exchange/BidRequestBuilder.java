package com.atg.openssp.core.exchange;

import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.core.system.loader.LocalContextLoader;
import openrtb.bidrequest.model.*;
import openrtb.tables.VideoBidResponseProtocol;

/**
 * RequestBuilder builds the BidRequest Object for RTB Exchange.
 *
 * @author André Schmer
 */
public final class BidRequestBuilder {
    private static BidRequestBuilder instance;
    private BidRequestBuilderHandler handler;

    private BidRequestBuilder() {
        String handlerClassName = GlobalContext.getBidRequestBuilderHandlerClass();
        if (handlerClassName != null && !"".equals(handlerClassName)) {
            try {
                Class c = Class.forName(handlerClassName);
                handler = (BidRequestBuilderHandler) c.getConstructor(null).newInstance(null);
            } catch (Exception e) {
                handler = new TestBidRequestBuilderHandler();
                e.printStackTrace();
            }
        } else {
            handler = new TestBidRequestBuilderHandler();
        }

    }

    /**
     * Build a request object regarding to the OpenRTB Specification.
     *
     * @return {@see BidRequest}
     */
    public BidRequest build(final SessionAgent agent) {

        return handler.constructRequest(agent);
    }

    public synchronized static BidRequestBuilder getInstance() {
        if (instance == null) {
            instance = new BidRequestBuilder();
        }
        return instance;
    }
}
