package com.atg.openssp.core.cache.broker.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.atg.openssp.common.demand.Supplier;

/**
 * 
 * @author André Schmer
 *
 */
public class SupplierDto implements Serializable {

	private static final long serialVersionUID = 2494786819460515865L;

	private List<Supplier> suppliers = new ArrayList();

	public SupplierDto() {}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(final List<Supplier> suppliers) {
		suppliers.clear();
		if (suppliers != null) {
			this.suppliers.addAll(suppliers);
		}
	}

	@Override
	public String toString() {
		return String.format("SupplierDto [supplier=%s]", suppliers);
	}

}
