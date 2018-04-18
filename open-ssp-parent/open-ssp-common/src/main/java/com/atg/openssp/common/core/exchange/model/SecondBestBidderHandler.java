package com.atg.openssp.common.core.exchange.model;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.configuration.GlobalContext;
import util.math.FloatComparator;

import java.util.List;

public class SecondBestBidderHandler extends AuctionMethodHandler {
    @Override
    protected AuctionWinner generateWinner(List<Bidder> bidders, Bidder bestBidder, float exchangedBestBidPrice, float exchangedFloorPrice) {
        final Bidder secondBestBidder = bidders.get(1);
        final float secondBestBidprice = secondBestBidder.getPrice();
        final float secondBestBidCurrencyRate = CurrencyCache.instance.get(secondBestBidder.getCurrency());
        final float exchangedSecondBidPrice = secondBestBidprice / secondBestBidCurrencyRate;
        AuctionWinner winner =  new AuctionWinner(secondBestBidder);
        if (FloatComparator.greaterOrEqual(exchangedSecondBidPrice, exchangedFloorPrice)) {
            // in the case that we have a second bidder wen need a increment value to increment the bidprice
            final float priceIncrement = calcPriceIncrement(exchangedSecondBidPrice, exchangedBestBidPrice);

            winner.setPrice(exchangedSecondBidPrice);
            winner.setExchangedPrice(exchangedSecondBidPrice + priceIncrement);
        } else {

            float exchangedWinnerPrice = 0;
            if (exchangedFloorPrice > 0) {
                exchangedWinnerPrice = exchangedFloorPrice;
            } else {
                exchangedWinnerPrice = exchangedBestBidPrice - (exchangedBestBidPrice * GlobalContext.getDrawModeration());
            }
            winner.setPrice(exchangedBestBidPrice);
            winner.setExchangedPrice(exchangedWinnerPrice);
        }
        return winner;
    }
}
