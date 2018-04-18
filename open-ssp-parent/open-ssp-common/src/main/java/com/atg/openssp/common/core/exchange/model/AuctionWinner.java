package com.atg.openssp.common.core.exchange.model;

import com.atg.openssp.common.demand.Supplier;
import openrtb.bidresponse.model.SeatBid;

public class AuctionWinner {
    private float price;
    private float exchangedPrice;
    private Bidder bestBidder;

    public AuctionWinner(Bidder bestBidder) {
        this.bestBidder = bestBidder;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getExchangedPrice() {
        return exchangedPrice;
    }

    public void setExchangedPrice(float exchangedPrice) {
        this.exchangedPrice = exchangedPrice;
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
