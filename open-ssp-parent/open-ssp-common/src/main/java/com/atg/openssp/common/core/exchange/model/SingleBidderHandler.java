package com.atg.openssp.common.core.exchange.model;

import java.util.List;

public class SingleBidderHandler extends AuctionMethodHandler {
    @Override
    protected AuctionWinner generateWinner(List<Bidder> bidders, Bidder bestBidder, float exchangedBestBidPrice, float exchangedFloor) {
        AuctionWinner winner =  new AuctionWinner(bestBidder);
        winner.setPrice(exchangedBestBidPrice);
        winner.setExchangedPrice(exchangedBestBidPrice);
        return winner;
    }
}
