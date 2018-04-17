package com.atg.openssp.common.core.exchange.model;

import java.util.List;

public abstract class AuctionMethodHandler {
    public abstract AuctionWinner generate(List<Bidder> bidders);
}
