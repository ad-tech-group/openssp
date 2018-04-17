package com.atg.openssp.common.core.exchange;

import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.SessionAgentType;
import com.atg.openssp.common.exception.RequestException;
import openrtb.bidrequest.model.BidRequest;

/**
 * RequestBuilder builds the BidRequest Object for RTB Exchange.
 *
 * @author André Schmer
 */
public final class BidRequestBuilder {
    private static BidRequestBuilder instance;
    private BidRequestBuilderHandler handler;
    private boolean initialized;

    private BidRequestBuilder() {
    }

    /**
     * Build a request object regarding to the OpenRTB Specification.
     *
     * @return {@see BidRequest}
     */
    public BidRequest build(final RequestSessionAgent agent) throws RequestException {
        if (!initialized) {
            initialized = true;

            BiddingServiceInfo info = agent.getBiddingServiceInfo();
            SessionAgentType type = info.getType();

            if (type == SessionAgentType.VIDEO) {
                try {
                    String handlerClassName = GlobalContext.getBidRequestBuilderHandlerForVideoObjectsClass();
                    Class c = Class.forName(handlerClassName);
                    handler = (BidRequestBuilderHandler) c.getConstructor(null).newInstance(null);
                } catch (Exception e) {
                    handler = new TestBidRequestBuilderHandler();
                }
            } else if (type == SessionAgentType.BANNER) {
                try {
                    String handlerClassName = GlobalContext.getBidRequestBuilderHandlerForBannerObjectsClass();
                    Class c = Class.forName(handlerClassName);
                    handler = (BidRequestBuilderHandler) c.getConstructor(null).newInstance(null);
                } catch (Exception e) {
                    handler = new TestBidRequestBuilderHandler();
                }
            } else if (type == SessionAgentType.HEADER) {
                try {
                    String handlerClassName = GlobalContext.getBidRequestBuilderHandlerForHeaderBiddingClass();
                    Class c = Class.forName(handlerClassName);
                    handler = (BidRequestBuilderHandler) c.getConstructor(null).newInstance(null);
                } catch (Exception e) {
                    handler = new TestBidRequestBuilderHandler();
                }
            } else {
                handler = new TestBidRequestBuilderHandler();
            }
        }

        return handler.constructRequest(agent);
    }

    public synchronized static BidRequestBuilder getInstance() {
        if (instance == null) {
            instance = new BidRequestBuilder();
        }
        return instance;
    }
}
