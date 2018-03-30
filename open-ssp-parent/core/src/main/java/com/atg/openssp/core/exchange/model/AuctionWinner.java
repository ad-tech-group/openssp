package com.atg.openssp.core.exchange.model;

import com.atg.openssp.common.demand.Supplier;
import openrtb.bidresponse.model.SeatBid;

public class AuctionWinner {
    private float price;
    private float adjustedCurrencyPrice;
    private Supplier supplier;
    private SeatBid seat;
    private String currency;
    private String dealId;
    private Bidder bestBidder;

    public AuctionWinner(Bidder bestBidder) {
    }

    public float getPrice() {
        return price;
    }

    public float getAdjustedCurrencyPrice() {
        return adjustedCurrencyPrice;
    }

    public Supplier getSupplier() {
        return bestBidder.getSupplier();
    }

    public SeatBid getSeat() {
        return bestBidder.getSeat();
    }

    public String getCurrency() {
        return bestBidder.getCurrency();
    }

    public String getDealId() {
        return bestBidder.getDealId();
    }

}
