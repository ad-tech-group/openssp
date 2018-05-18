package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;

/**
 * @author Brian Sorensen
 */
public class CurrencyResponse {
    private ResponseStatus status;
    private String reason="";
    private CurrencyDto dto;

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
    
    public void setDto(CurrencyDto dto) {
        this.dto = dto;
    }

    public CurrencyDto getDto() {
        return dto;
    }

}
