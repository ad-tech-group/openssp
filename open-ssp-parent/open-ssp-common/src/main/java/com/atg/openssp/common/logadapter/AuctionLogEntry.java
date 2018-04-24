package com.atg.openssp.common.logadapter;

import openrtb.bidrequest.model.Site;
import openrtb.bidresponse.model.Bid;

import java.util.List;

public class AuctionLogEntry {
    private String logginId;
    private String requestId;
    private String userId;
    private Long supplierId;
    private String supplierName;
    private String page;
    private List<Bid> bids;
    private Site site;

    public String getLogginId() {
        return logginId;
    }

    public void setLogginId(String logginId) {
        this.logginId = logginId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Site getSite() {
        return site;
    }

}
