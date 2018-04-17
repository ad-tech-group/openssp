package com.atg.openssp.common.core.exchange.model;

import com.atg.openssp.common.demand.Supplier;
import openrtb.bidresponse.model.SeatBid;

public class Bidder implements  Comparable<Bidder> {
    private float bid;
    private SeatBid seat;
    private String bidfloorCurrency;
    private float bidfloor;
    private String dealId;
    private final Supplier supplierId;
    private String currency;

    public Bidder(final Supplier supplierId) {
        this.supplierId = supplierId;
    }

    public void setCurrency(final String cur) {
        currency = cur;
    }

    public String getCurrency() {
        return currency;
    }

    public Supplier getSupplier() {
        return supplierId;
    }

    public SeatBid getSeat() {
        return seat;
    }

    public void setSeat(final SeatBid seat) {
        this.seat = seat;
    }

    public float getPrice() {
        return bid;
    }

    public void setPrice(final float price) {
        bid = price;
    }

    public String getBidfloorCurrency() {
        return bidfloorCurrency;
    }

    public void setBidFloorcurrency(final String bidfloorcur) {
        bidfloorCurrency = bidfloorcur;
    }

    public float getBidFloorprice() {
        return bidfloor;
    }

    public void setBidFloorprice(final float bidfloor) {
        this.bidfloor = bidfloor;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(final String dealId) {
        this.dealId = dealId;
    }


    // Descending order
    @Override
    public int compareTo(final Bidder o) {
        if (o.getPrice() > getPrice()) {
            return 1;
        }
        return -1;
    }

}
