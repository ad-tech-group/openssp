package com.atg.openssp.dspSimUi.model.client;

import openrtb.bidrequest.model.Pricelayer;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class PricelayerResponse {
    private ResponseStatus status;
    private String reason="";
    private List<Pricelayer> pricelayer;

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
    
    public void setPricelayers(List<Pricelayer> pricelayer) {
        this.pricelayer = pricelayer;
    }

    public List<Pricelayer> getPricelayers() {
        return pricelayer;
    }

}
