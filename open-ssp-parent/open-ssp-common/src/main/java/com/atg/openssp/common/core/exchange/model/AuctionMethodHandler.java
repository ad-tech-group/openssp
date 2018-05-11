package com.atg.openssp.common.core.exchange.model;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.core.exchange.Auction;
import com.atg.openssp.common.exception.InvalidBidException;
import util.math.FloatComparator;

import java.util.List;

public abstract class AuctionMethodHandler {
    public final AuctionWinner generate(List<Bidder> bidders) throws InvalidBidException {
        final Bidder bestBidder = bidders.get(0);
        // returns 1 if bidder is already system's currency
        final float bestBidCurrencyRate = CurrencyCache.instance.get(bestBidder.getCurrency());
        // normalize the price to system currency
        final float exchangedBestBidPrice = bestBidder.getPrice() / bestBidCurrencyRate;

        // floor in system's currency
        final float floorCurrencyRate = CurrencyCache.instance.get(bestBidder.getBidfloorCurrency());
        final float exchangedFloorPrice = bestBidder.getBidFloorprice() / floorCurrencyRate;
        if (false == FloatComparator.greaterOrEqual(exchangedBestBidPrice, exchangedFloorPrice)) {
            //TODO: BKS output needs to be localized
            throw new InvalidBidException(Auction.class.getSimpleName() + ", winner invalid cause bid lower than floor [" + exchangedBestBidPrice + " EUR < " + exchangedFloorPrice + " EUR] "
                    + bestBidder.getSupplier().getShortName() + " [" + floorCurrencyRate + "," + bestBidder.getBidFloorprice() + ", " + bestBidder.getBidfloorCurrency()
                    + "] DealID:" + bestBidder.getDealId());
        }

        return generateWinner(bidders, bestBidder, exchangedBestBidPrice, exchangedFloorPrice);

    }

    protected float calcPriceIncrement(final float exchangedSecondBidPrice, final float exchangedBestBidPrice) {
        final float defautlIncrement = 0.01f;
        if (
                FloatComparator.isEqual(exchangedSecondBidPrice, exchangedBestBidPrice) ||
                FloatComparator.isDiffSmallerThanEpsilon(exchangedBestBidPrice, exchangedSecondBidPrice, defautlIncrement)
                ) {
            return 0f;
        }
        return defautlIncrement;
    }


    protected abstract AuctionWinner generateWinner(List<Bidder> bidders, Bidder bestBidder, float exchangedBestBidPrice, float exchangedFloor);
}
