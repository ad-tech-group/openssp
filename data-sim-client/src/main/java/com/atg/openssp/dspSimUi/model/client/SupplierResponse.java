package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.dspSimUi.model.dsp.SimBidder;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class SupplierResponse {
    private ServerResponseStatus status;
    private String reason="";
    private List<Supplier> suppliers;

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
    
    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

}
