package com.atg.openssp.core.exchange.model;

import com.atg.openssp.core.exchange.Auction;

import java.util.List;

public abstract class AuctionMethodHandler {
    public abstract AuctionWinner generate(List<Bidder> bidders);
}
