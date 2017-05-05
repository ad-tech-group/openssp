package com.atg.openssp.core.cache.broker.context;

import java.io.Serializable;
import java.util.List;

import com.atg.openssp.common.demand.Supplier;

/**
 * 
 * @author André Schmer
 *
 */
class SupplierDto implements Serializable {

	private static final long serialVersionUID = 2494786819460515865L;

	private List<Supplier> data;

	public SupplierDto() {}

	public List<Supplier> getData() {
		return data;
	}

	public void setData(final List<Supplier> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("SupplierDto [supplier=%s]", data);
	}

}
