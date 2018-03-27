package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.dspSimUi.model.dsp.SimBidder;
import openrtb.bidrequest.model.Site;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class SiteResponse {
    private ServerResponseStatus status;
    private String reason="";
    private List<Site> sites;

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
    
    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public List<Site> getSites() {
        return sites;
    }

}
