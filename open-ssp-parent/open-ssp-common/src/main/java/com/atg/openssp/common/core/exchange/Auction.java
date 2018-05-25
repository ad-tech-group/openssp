package com.atg.openssp.common.core.exchange;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.configuration.GlobalContext;
import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.exception.InvalidBidException;
import com.atg.openssp.common.logadapter.AuctionLogProcessor;
import com.atg.openssp.common.provider.AdProviderReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.*;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;
import openrtb.tables.AuctionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.math.FloatComparator;

import java.util.*;
import java.util.Map.Entry;

/**
 * This is the standard Auction Service.
 * 
 * @author André Schmer
 *
 */
public class Auction {
	private static final Logger LOGGER = LoggerFactory.getLogger(Auction.class);

	/**
	 * Calculates the the winner of the RTB auction considering the behaviour of a private deal.
	 *
	 *
	 * @param info
	 * @param bidExchange
	 * @return RtbAdProvider
	 * @throws InvalidBidException TODO: big issue: eval to extract to more generla context
	 */
	public static AuctionResult auctioneer(BiddingServiceInfo info, final BidExchange bidExchange) throws InvalidBidException {
		HashMap<String, List<Bidder>> dealBidListMap = new HashMap<String, List<Bidder>>();
		HashMap<String, List<Bidder>> nonDealBidListMap = new HashMap<String, List<Bidder>>();

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
            ArrayList<Bid> logBidList = new ArrayList();
			// considering that only ONE impression containing the bidrequest
			for (Impression imp : request.getImp()) {
				List<Bidder> dealBidList = dealBidListMap.get(imp.getId());
				if (dealBidList == null) {
					dealBidList = new ArrayList<Bidder>();
					dealBidListMap.put(imp.getId(), dealBidList);
				}
				List<Bidder> nonDealBidList = nonDealBidListMap.get(imp.getId());
				if (nonDealBidList == null) {
					nonDealBidList = new ArrayList<Bidder>();
					nonDealBidListMap.put(imp.getId(), nonDealBidList);
				}

				for (final SeatBid seatBid : bidResponse.getSeatbid()) {
					for (final Bid bid : seatBid.getBid()) {
						if (imp.getId().equals(bid.getImpid())) {
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
							logBidList.add(bid);
						}
					}
				}
			}
			User user = request.getUser();
			AuctionLogProcessor.instance.setLogData(
					info.getLoggingId(),
					request.getId(),
					request.getUser(),
					bidResponses.getKey().getSupplierId(),
					bidResponses.getKey().getShortName(),
					request.getSite(),
					logBidList);

		}
		LOGGER.debug("got auction: "+dealBidListMap+"::"+nonDealBidListMap);



		HashMap<String, RtbAdProvider> winningProviderMap = new HashMap<String, RtbAdProvider>();
		for (Entry<String, List<Bidder>> e : dealBidListMap.entrySet()) {
			List<Bidder> dealBidList = e.getValue();
			// 1. als erstes die bids für die deals evaluieren
			if (false == dealBidList.isEmpty()) {
				Collections.sort(dealBidList);
				winningProviderMap.put(e.getKey(), evaluateWinner(info, dealBidList));
			}
		}
		LOGGER.debug("first run done");

		for (Entry<String, List<Bidder>> e : nonDealBidListMap.entrySet()) {
			RtbAdProvider winningProvider = winningProviderMap.get(e.getKey());
			List<Bidder> nonDealBidList = e.getValue();
			// 2. evaluiere NON-Deals-Bids, falls kein DealBid bereits gewonnen hat
			if (winningProvider == null && false == nonDealBidList.isEmpty()) {
				Collections.sort(nonDealBidList);
				winningProviderMap.put(e.getKey(), evaluateWinner(info, nonDealBidList));
			}
		}
		LOGGER.debug("second run done");

		Set<Entry<String, RtbAdProvider>> winningProviderSet = winningProviderMap.entrySet();
		AuctionResult dealWinner;
		if (winningProviderSet.size() > 1) {
			LOGGER.debug("set>1");
			dealWinner = new MultipleAuctionResult();
			// just use the first one for the supplier
			dealWinner.setBidRequest((BidRequest) bidExchange.getAllBidRequests().values().toArray()[0]);
			for (Entry<String, RtbAdProvider> e : winningProviderSet) {
				if (e.getValue() != null) {
					((MultipleAuctionResult)dealWinner).addWinningProvider(e.getValue());
				}
			}

		} else if (winningProviderSet.size() == 1) {
			LOGGER.debug("set <= 1");
			dealWinner = new SingularAuctionResult();
			String key = (String) winningProviderMap.keySet().toArray()[0];
			RtbAdProvider winningProvider = winningProviderMap.get(key);
			if (winningProvider != null) {
				dealWinner.setBidRequest(bidExchange.getBidRequest(winningProvider.getSupplier()));
				((SingularAuctionResult)dealWinner).setWinningProvider(winningProvider);
			}
		}
		else {
			LOGGER.debug("set == 0");
			dealWinner = new SingularAuctionResult();
		}
		LOGGER.debug("return winner: "+dealWinner);

		return dealWinner;
	}

	// bidList must be sorted
	private static RtbAdProvider evaluateWinner(BiddingServiceInfo info, final List<Bidder> bidList) throws InvalidBidException {
		final Bidder bestBidder = bidList.get(0);
		// returns 1 if bidder is already system's currency
		final float bestBidCurrencyRate = CurrencyCache.instance.get(bestBidder.getCurrency());
		// normalize the price to system currency
		final float exchangedBestBidPrice = bestBidder.getPrice() / bestBidCurrencyRate;

		// floor in system's currency
		final float floorCurrencyRate = CurrencyCache.instance.get(bestBidder.getBidfloorCurrency());
		final float exchangedFloor = bestBidder.getBidFloorprice() / floorCurrencyRate;
		if (false == FloatComparator.greaterOrEqual(exchangedBestBidPrice, exchangedFloor)) {
		    //TODO: BKS output needs to be localized
			throw new InvalidBidException(Auction.class.getSimpleName() + ", winner invalid cause bid lower than floor [" + exchangedBestBidPrice + " EUR < " + exchangedFloor + " EUR] "
			        + bestBidder.getSupplier().getShortName() + " [" + floorCurrencyRate + "," + bestBidder.getBidFloorprice() + ", " + bestBidder.getBidfloorCurrency()
			        + "] DealID:" + bestBidder.getDealId());
		}

		float exchangedWinnerPrice;
		if (bidList.size() > 1) {
			if (info.getAuctionType() == AuctionType.SECOND_PRICE) {
				AuctionMethodHandler methodHandler = new SecondBestBidderHandler();
                exchangedWinnerPrice = methodHandler.generateWinningPrice(bidList, exchangedFloor, exchangedBestBidPrice);
			} else {
			    // First Best - let it ride
                exchangedWinnerPrice = exchangedBestBidPrice;
			}
		} else {
			AuctionMethodHandler methodHandler = new SingleBidderHandler();
            exchangedWinnerPrice = methodHandler.generateWinningPrice(bidList, exchangedFloor, exchangedBestBidPrice);
		}

		return new RtbAdProvider.Builder().setIsValid(true).setPrice(FloatComparator.rr(exchangedWinnerPrice * bestBidCurrencyRate)).setExchangedCurrencyPrice(FloatComparator.rr(exchangedWinnerPrice))
		        .setSupplier(bestBidder.getSupplier()).setWinningSeat(bestBidder.getSeat()).setCurrency(bestBidder.getCurrency()).setDealId(bestBidder.getDealId()).build();
	}

	private static float calcPriceIncrement(final float exchangedSecondBidPrice, final float exchangedBestBidPrice) {
		final float defautlIncrement = 0.01f;
		if (FloatComparator.isEqual(exchangedSecondBidPrice, exchangedBestBidPrice) || FloatComparator.isDiffSmallerThanEpsilon(exchangedBestBidPrice, exchangedSecondBidPrice, defautlIncrement)) {
			return 0f;
		}
		return defautlIncrement;
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

	public interface AuctionResult extends AdProviderReader {
		BidRequest getBidRequest();

		void setBidRequest(BidRequest bidRequest);

		String buildHeaderBidResponse();

		Supplier getSupplier();

		String getDealId();
	}

	public static class SingularAuctionResult implements AuctionResult {
		private BidRequest bidRequest;
		private RtbAdProvider winningProvider;

		@Override
		public BidRequest getBidRequest() {
			return bidRequest;
		}

		@Override
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
		public float getExchangedCurrencyPrice() {
			return winningProvider.getExchangedCurrencyPrice();
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
			if (winningProvider != null) {
				return winningProvider.isValid();
			} else {
				return false;
			}
		}

		@Override
		public String getAdid() {
			return winningProvider.getAdid();
		}

		@Override
		public Supplier getSupplier() {
			return winningProvider.getSupplier();
		}

		@Override
		public String getDealId() {
			return winningProvider.getDealId();
		}

		@Override
		public String buildHeaderBidResponse() {
			Gson gson = new GsonBuilder().setVersion(Double.valueOf(getSupplier().getOpenRtbVersion())).create();
			final String json = gson.toJson(this, SingularAuctionResult.class);
			return json;
		}
	}

	public static class MultipleAuctionResult implements AuctionResult {
		private BidRequest bidRequest;
		private ArrayList<RtbAdProvider> winningProvider = new ArrayList<>();

		@Override
		public BidRequest getBidRequest() {
			return bidRequest;
		}

		@Override
		public void setBidRequest(BidRequest bidRequest) {
			this.bidRequest = bidRequest;
		}

		public void addWinningProvider(RtbAdProvider winningProvider) {
			this.winningProvider.add(winningProvider);
		}

		public List<RtbAdProvider> getWinningProvider() {
			return winningProvider;
		}

		@Override
		public float getPrice() {
			return winningProvider.get(0).getPrice();
		}

		@Override
		public float getExchangedCurrencyPrice() {
			return winningProvider.get(0).getExchangedCurrencyPrice();
		}

		@Override
		public String getCurrrency() {
			return winningProvider.get(0).getCurrrency();
		}

		@Override
		public void perform(SessionAgent agent) {
			winningProvider.get(0).perform(agent);
		}

		@Override
		public String buildResponse() {
			return winningProvider.get(0).buildResponse();
		}

		@Override
		public String getVendorId() {
			return winningProvider.get(0).getVendorId();
		}

		@Override
		public boolean isValid() {
			if (winningProvider != null) {
				return winningProvider.get(0).isValid();
			} else {
				return false;
			}
		}

		@Override
		public String getAdid() {
			return winningProvider.get(0).getAdid();
		}

		@Override
		public Supplier getSupplier() {
			return winningProvider.get(0).getSupplier();
		}

		@Override
		public String getDealId() {
			return winningProvider.get(0).getDealId();
		}

		@Override
		public String buildHeaderBidResponse() {
			Gson gson = new GsonBuilder().setVersion(Double.valueOf(getSupplier().getOpenRtbVersion())).create();
			final String json = gson.toJson(this, MultipleAuctionResult.class);
			return json;
		}
	}

	public static abstract class AuctionMethodHandler {
		public abstract float generateWinningPrice(List<Bidder> bidList, float exchangedFloorPrice, float exchangedBestBidPrice);

		protected final float calcPriceForSingleBid(float exchangedFloorPrice, float exchangedBestBidPrice) {
			float exchangedWinnerPrice = 0;
			if (exchangedFloorPrice > 0) {
                exchangedWinnerPrice = exchangedFloorPrice;
			} else {
                exchangedWinnerPrice = exchangedBestBidPrice - (exchangedBestBidPrice * GlobalContext.getDrawModeration());
			}
			return exchangedWinnerPrice;
		}
	}

	public static class SecondBestBidderHandler extends AuctionMethodHandler {
		@Override
		public float generateWinningPrice(List<Bidder> bidList, float exchangedFloorPrice, float exchangedBestBidPrice) {
			final Auction.Bidder secondBestBidder = bidList.get(1);
			final float secondBestBidprice = secondBestBidder.getPrice();
			final float secondBestBidCurrencyRate = CurrencyCache.instance.get(secondBestBidder.getCurrency());
			final float exchangedSecondBidPrice = secondBestBidprice / secondBestBidCurrencyRate;
			if (FloatComparator.greaterOrEqual(exchangedSecondBidPrice, exchangedFloorPrice)) {
				// in the case that we have a second bidder wen need a increment value to increment the bidprice
				final float priceIncrement = calcPriceIncrement(exchangedSecondBidPrice, exchangedBestBidPrice);
				return exchangedSecondBidPrice + priceIncrement;
			} else {
				return calcPriceForSingleBid(exchangedFloorPrice, exchangedBestBidPrice);
			}
		}
	}

	public static class SingleBidderHandler extends AuctionMethodHandler {
		@Override
		public float generateWinningPrice(List<Bidder> bidList, float exchangedFloorPrice, float exchangedBestBidPrice) {
			return calcPriceForSingleBid(exchangedFloorPrice, exchangedBestBidPrice);
		}
	}

}
