package com.atg.openssp.core.cache.broker.context;

import java.io.Serializable;
import java.util.List;

import com.atg.openssp.common.model.EurRef;

/**
 * 
 * @author André Schmer
 *
 */
class CurrencyDto implements Serializable {

	private static final long serialVersionUID = 3455274178456736049L;

	private List<EurRef> data;

	public CurrencyDto() {}

	public List<EurRef> getData() {
		return data;
	}

	public void setData(final List<EurRef> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("EurRefDto [data=%s]", data);
	}

}
