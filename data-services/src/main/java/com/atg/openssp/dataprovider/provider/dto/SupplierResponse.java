package com.atg.openssp.dataprovider.provider.dto;

import com.atg.openssp.common.demand.Supplier;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class SupplierResponse {
    private ResponseStatus status;
    private String reason="";
    private List<Supplier> suppliers;

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
    
    public void setSupplier(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public List<Supplier> getSupplier() {
        return suppliers;
    }

}
