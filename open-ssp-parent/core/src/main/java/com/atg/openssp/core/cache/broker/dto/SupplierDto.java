package com.atg.openssp.core.cache.broker.dto;

import java.io.Serializable;
import java.util.List;

import com.atg.openssp.common.demand.Supplier;

/**
 *
 * @author André Schmer
 *
 */
public class SupplierDto implements Serializable {

	private static final long serialVersionUID = 2494786819460515865L;

	private List<Supplier> supplier;

	public SupplierDto() {}

	public List<Supplier> getSupplier() {
		return supplier;
	}

	public void setSupplier(final List<Supplier> supplier) {
		this.supplier = supplier;
	}

	@Override
	public String toString() {
		return String.format("SupplierDto [supplier=%s]", supplier);
	}

}

