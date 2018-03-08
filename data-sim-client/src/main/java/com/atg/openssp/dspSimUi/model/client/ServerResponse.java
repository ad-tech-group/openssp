package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.dspSimUi.model.dsp.SimBidder;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class ServerResponse {
    private ServerResponseStatus status;
    private String reason="";
    private List<SimBidder> bidders;

    public void setStatus(ServerResponseStatus status) {
        this.status = status;
    }

    public ServerResponseStatus getStatus() {
        return status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
    
    public void setBidders(List<SimBidder> bidders) {
        this.bidders = bidders;
    }

    public List<SimBidder> getBidders() {
        return bidders;
    }

}
