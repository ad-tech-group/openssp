package com.atg.openssp.dspSim.model.client;

import com.atg.openssp.dspSim.model.dsp.SimBidder;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class ClientResponse {
    private ClientResponseStatus status;
    private String reason="";
    private List<SimBidder> bidders;
    private ClientCommandType mode;

    public void setStatus(ClientResponseStatus status) {
        this.status = status;
    }

    public ClientResponseStatus getStatus() {
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

    public void setMode(ClientCommandType mode) {
        this.mode = mode;
    }

    public ClientCommandType getMode() {
        return mode;
    }
}
