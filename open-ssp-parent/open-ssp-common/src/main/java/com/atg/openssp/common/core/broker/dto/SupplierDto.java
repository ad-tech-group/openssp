package com.atg.openssp.common.core.broker.dto;

import com.atg.openssp.common.demand.Supplier;

import java.io.Serializable;
import java.util.List;

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

