package com.atg.openssp.common.core.broker.dto;

import com.atg.openssp.common.model.EurRef;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author André Schmer
 *
 */
public class CurrencyDto implements Serializable {

	private static final long serialVersionUID = 3455274178456736049L;

	private String currency = "EUR";

	private List<EurRef> data;

	public CurrencyDto() {}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

    public List<EurRef> getData() {
        return data;
    }

    public void setData(final List<EurRef> data) {
        this.data = data;
    }

    @Override
	public String toString() {
		return String.format("CurrencyDto [currency=%s, data=%s]", currency, data);
	}

}
