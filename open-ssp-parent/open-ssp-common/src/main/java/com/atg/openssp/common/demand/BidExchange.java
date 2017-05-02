package com.atg.openssp.common.demand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.atg.service.LogFacade;

import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.BidRequest.Builder;
import openrtb.bidresponse.model.BidResponse;

/**
 * @author André Schmer
 *
 */
public class BidExchange {

	private final List<Supplier> supplierList;

	private final List<BidRequest> bidRequestList;

	private final Map<Supplier, BidRequest.Builder> bidRequestMap;

	private final Map<Supplier, BidResponse.Builder> bidResponseMap;

	private long winnerId;

	public BidExchange() {
		supplierList = new CopyOnWriteArrayList<>();
		bidRequestMap = new HashMap<>();
		bidResponseMap = new HashMap<>();
		bidRequestList = new ArrayList<>();
	}

	public Map<Supplier, BidRequest.Builder> getAllBidRequests() {
		return bidRequestMap;
	}

	public Map<Supplier, BidResponse.Builder> getAllBidResponses() {
		return bidResponseMap;
	}

	public BidRequest.Builder getBidRequest(final Supplier supplier) {
		return bidRequestMap.get(supplier);
	}

	public void setBidRequest(final Supplier supplier, final Builder bidRequest1) {
		bidRequestMap.put(supplier, bidRequest1);
	}

	public BidResponse.Builder getBidResponse(final Supplier supplier) {
		return bidResponseMap.get(supplier);
	}

	public void setBidResponse(final Supplier supplier, final BidResponse.Builder bidResponse) {
		bidResponseMap.put(supplier, bidResponse);
	}

	public List<Supplier> getSupplierList() {
		return supplierList;
	}

	public void addSupplier(final Supplier supplier) {
		supplierList.add(supplier);
	}

	public void setWinnerId(final long winnerId) {
		this.winnerId = winnerId;
	}

	public long getWinnerId() {
		return winnerId;
	}

	public void addBidRequest(final BidRequest request) {
		bidRequestList.add(request);
	}

	public List<BidRequest> getbidRequestList() {
		return bidRequestList;
	}

	public StringBuilder buildReport() {
		if (!getSupplierList().isEmpty()) {
			for (final Supplier supplier : getSupplierList()) {
				final StringBuilder sb = new StringBuilder();
				final long supplierId = supplier.getSupplierId();
				sb.append(supplierId).append(LogFacade.LOG_DELIMITER);
				if (supplierId == getWinnerId()) {
					sb.append("1");
				} else {
					sb.append("0");
				}
				return sb;
			}
		}
		return null;
	}

}
