package com.atg.openssp.core.exchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.exception.InvalidBidException;

import com.atg.openssp.common.provider.AdProviderReader;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.DirectDeal;
import openrtb.bidrequest.model.Impression;
import openrtb.bidrequest.model.PMP;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;
import util.math.FloatComparator;

/**
 * This is the standard Auction Service.
 * 
 * @author André Schmer
 *
 */
public class Auction {

	/**
	 * Calculates the the winner of the RTB auction considering the behaviour of a private deal.
	 *
	 * @param bidExchange
	 * @return RtbAdProvider
	 * @throws InvalidBidException TODO: big issue: eval to extract to more generla context
	 */
	public static AuctionResult auctioneer(final BidExchange bidExchange) throws InvalidBidException {
		final List<Bidder> dealBidList = new ArrayList<>();
		final List<Bidder> nonDealBidList = new ArrayList<>();

		// bidExchange.getAllBidResponses().entrySet().forEach(new Consumer<Entry<Supplier, BidResponse.Builder>>() {
		// @Override
		// public void accept(final Entry<Supplier, BidResponse.Builder> bidResponses) {
		//
		// final BidResponse bidResponse = bidResponses.getValue().build();
		// if (bidResponse != null) {
		// final BidRequest request = bidExchange.getBidRequest(bidResponses.getKey()).build();
		// // considering that only ONE (the first) impression containing the bidrequest
		// final Impression imp = request.getImp().get(0);
		// bidResponse.getSeatbid().stream().peek(seatBid -> {
		// seatBid.getBid().stream().peek(bid -> {
		// final Bidder bidder = new Bidder(bidResponses.getKey());
		// bidder.setSeat(seatBid);
		// bidder.setPrice(bid.getPrice());
		// bidder.setCurrency(bidResponse.getCur());
		// final DirectDeal matchingDeal = checkForDealMatch(imp.getPmp(), bid);
		// if (matchingDeal != null) {
		// bidder.setDealId(matchingDeal.getId());
		// bidder.setBidFloorcurrency(matchingDeal.getBidFloorcurrrency());
		// bidder.setBidFloorprice(matchingDeal.getBidFloorprice());
		// dealBidList.add(bidder);
		// } else {
		// bidder.setBidFloorcurrency(imp.getBidfloorcur());
		// bidder.setBidFloorprice(imp.getBidfloor());
		// nonDealBidList.add(bidder);
		// }
		// });
		// });
		// }
		// }
		// });

		for (final Entry<Supplier, BidResponse> bidResponses : bidExchange.getAllBidResponses().entrySet()) {
			final BidResponse bidResponse = bidResponses.getValue();
			if (bidResponse == null) {
				continue;
			}
			final BidRequest request = bidExchange.getBidRequest(bidResponses.getKey());
			// considering that only ONE impression containing the bidrequest
			final Impression imp = request.getImp().get(0);
			for (final SeatBid seatBid : bidResponse.getSeatbid()) {
				for (final Bid bid : seatBid.getBid()) {
					final Bidder bidder = new Bidder(bidResponses.getKey());
					bidder.setSeat(seatBid);
					bidder.setPrice(bid.getPrice());
					bidder.setCurrency(bidResponse.getCur());
					final DirectDeal matchingDeal = checkForDealMatch(imp.getPmp(), bid);
					if (matchingDeal != null) {
						bidder.setDealId(matchingDeal.getId());
						bidder.setBidFloorcurrency(matchingDeal.getBidFloorcurrrency());
						bidder.setBidFloorprice(matchingDeal.getBidFloorprice());
						dealBidList.add(bidder);
					} else {
						bidder.setBidFloorcurrency(imp.getBidfloorcur());
						bidder.setBidFloorprice(imp.getBidfloor());
						nonDealBidList.add(bidder);
					}
				}
			}
		}

		RtbAdProvider winningProvider = null;
		// 1. als erstes die bids für die deals evaluieren
		if (false == dealBidList.isEmpty()) {
			Collections.sort(dealBidList);
			winningProvider = evaluateWinner(dealBidList);
		}

		// 2. evaluiere NON-Deals-Bids, falls kein DealBid bereits gewonnen hat
		if (winningProvider == null && false == nonDealBidList.isEmpty()) {
			Collections.sort(nonDealBidList);
			winningProvider = evaluateWinner(nonDealBidList);
		}

		AuctionResult dealWinner = new AuctionResult();
		dealWinner.setBidRequest(bidExchange.getBidRequest(winningProvider.getSupplier()));
		dealWinner.setWinningProvider(winningProvider);
		return dealWinner;
	}

	// bidList must be sorted
	private static RtbAdProvider evaluateWinner(final List<Bidder> bidList) throws InvalidBidException {
		final Bidder bestBidder = bidList.get(0);
		// returns 1 if bidder is already in EUR
		final float bestBidCurrencyRate = CurrencyCache.instance.get(bestBidder.getCurrency());
		// normalize the price to EUR
		final float bestBidPriceEUR = bestBidder.getPrice() / bestBidCurrencyRate;

		// floor in EUR
		final float floorCurrencyRate = CurrencyCache.instance.get(bestBidder.getBidfloorCurrency());
		final float floorEUR = bestBidder.getBidFloorprice() / floorCurrencyRate;
		if (false == FloatComparator.greaterOrEqual(bestBidPriceEUR, floorEUR)) {
			throw new InvalidBidException(Auction.class.getSimpleName() + ", winner invalid cause bid lower than floor [" + bestBidPriceEUR + " EUR < " + floorEUR + " EUR] "
			        + bestBidder.getSupplier().getShortName() + " [" + floorCurrencyRate + "," + bestBidder.getBidFloorprice() + ", " + bestBidder.getBidfloorCurrency()
			        + "] DealID:" + bestBidder.getDealId());
		}

		float winnerPriceEUR = 0;
		// final float winnerPrice = 0;
		if (bidList.size() > 1) {
			final Bidder secondBestBidder = bidList.get(1);
			final float secondBestBidprice = secondBestBidder.getPrice();
			final float secondBestBidCurrencyRate = CurrencyCache.instance.get(secondBestBidder.getCurrency());
			final float secondBidPriceEUR = secondBestBidprice / secondBestBidCurrencyRate;
			if (FloatComparator.greaterOrEqual(secondBidPriceEUR, floorEUR)) {
				// in the case that we have a second bidder wen need a increment value to increment the bidprice
				final float priceIncrement = calcPriceIncrement(secondBidPriceEUR, bestBidPriceEUR);
				winnerPriceEUR = secondBidPriceEUR + priceIncrement;
			} else {
				winnerPriceEUR = calcPriceForSingleBid(floorEUR, bestBidPriceEUR);
			}
		} else {
			winnerPriceEUR = calcPriceForSingleBid(floorEUR, bestBidPriceEUR);
		}

		return new RtbAdProvider.Builder().setIsValid(true).setPrice(FloatComparator.rr(winnerPriceEUR * bestBidCurrencyRate)).setNormalizedPrice(FloatComparator.rr(winnerPriceEUR))
		        .setSupplier(bestBidder.getSupplier()).setWinningSeat(bestBidder.getSeat()).setCurrency(bestBidder.getCurrency()).setDealId(bestBidder.getDealId()).build();
	}

	private static float calcPriceIncrement(final float secondBidPriceEUR, final float bestBidPriceEUR) {
		final float defautlIncrement = 0.01f;
		if (FloatComparator.isEqual(secondBidPriceEUR, bestBidPriceEUR) || FloatComparator.isDiffSmallerThanEpsilon(bestBidPriceEUR, secondBidPriceEUR, defautlIncrement)) {
			return 0f;
		}
		return defautlIncrement;
	}

	// assumes that floorPriceEUR is smaller than bestBidPriceEUR
	private static float calcPriceForSingleBid(final float floorPriceEUR, final float bestBidPriceEUR) {
		float winnerPriceEUR = 0;
		if (floorPriceEUR > 0) {
			winnerPriceEUR = floorPriceEUR;
		} else {
			winnerPriceEUR = bestBidPriceEUR - (bestBidPriceEUR * GlobalContext.getDrawModeration());
		}
		return winnerPriceEUR;
	}

	private static DirectDeal checkForDealMatch(final PMP pmp, final Bid bid) {
		if (pmp.getPrivate_auction() == 1 && pmp.getDeals() != null) {
			return pmp.getDeals().stream().filter(deal -> deal.getId().equals(bid.getDealid())).findFirst().orElse(null);
		}
		return null;
	}

	private static class Bidder implements Comparable<Bidder> {

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

	public static class AuctionResult implements AdProviderReader {
		private BidRequest bidRequest;
		private RtbAdProvider winningProvider;

		public BidRequest getBidRequest() {
			return bidRequest;
		}

		public void setBidRequest(BidRequest bidRequest) {
			this.bidRequest = bidRequest;
		}

		public void setWinningProvider(RtbAdProvider winningProvider) {
			this.winningProvider = winningProvider;
		}

		public RtbAdProvider getWinningProvider() {
			return winningProvider;
		}

		@Override
		public float getPrice() {
			return winningProvider.getPrice();
		}

		@Override
		public float getNormalizedPrice() {
			return winningProvider.getNormalizedPrice();
		}

		@Override
		public String getCurrrency() {
			return winningProvider.getCurrrency();
		}

		@Override
		public void perform(SessionAgent agent) {
			winningProvider.perform(agent);
		}

		@Override
		public String buildResponse() {
			return winningProvider.buildResponse();
		}

		@Override
		public String getVendorId() {
			return winningProvider.getVendorId();
		}

		@Override
		public boolean isValid() {
			return winningProvider.isValid();
		}

		@Override
		public String getAdid() {
			return winningProvider.getAdid();
		}

		public Supplier getSupplier() {
			return winningProvider.getSupplier();
		}

		public String getDealId() {
			return winningProvider.getDealId();
		}

		public String buildHeaderBidResponse() {
			Gson gson = new GsonBuilder().setVersion(Double.valueOf(getSupplier().getOpenRtbVersion())).create();
			final String json = gson.toJson(this, AuctionResult.class);
			return json;
		}
	}

}
