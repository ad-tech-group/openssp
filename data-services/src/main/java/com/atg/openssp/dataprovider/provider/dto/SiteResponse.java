package com.atg.openssp.dataprovider.provider.dto;

import openrtb.bidrequest.model.Site;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class SiteResponse {
    private ResponseStatus status;
    private String reason="";
    private List<Site> sites;

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
    
    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public List<Site> getSites() {
        return sites;
    }

}
