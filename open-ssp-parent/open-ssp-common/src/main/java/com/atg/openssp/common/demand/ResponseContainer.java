package com.atg.openssp.common.demand;

import openrtb.bidresponse.model.BidResponse;

/**
 * @author André Schmer
 *
 */
public class ResponseContainer {

	private final Supplier supplier;

	private final BidResponse bidResponse;

	public ResponseContainer(final Supplier supplier, final BidResponse bidResponse) {
		this.supplier = supplier;
		this.bidResponse = bidResponse;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public BidResponse getBidResponse() {
		return bidResponse;
	}

}
