package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.dspSimUi.model.dsp.SimBidder;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class ServerResponse {
    private ResponseStatus status;
    private String reason="";
    private List<SimBidder> bidders;
    private ServerCommandType mode;

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
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

    public void setMode(ServerCommandType mode) {
        this.mode = mode;
    }

    public ServerCommandType getMode() {
        return mode;
    }
}
