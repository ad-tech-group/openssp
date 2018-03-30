package com.atg.openssp.core.exchange;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.exception.InvalidBidException;
import com.atg.openssp.common.provider.AdProviderReader;
import com.atg.openssp.core.entry.BiddingServiceInfo;
import com.atg.openssp.core.exchange.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.DirectDeal;
import openrtb.bidrequest.model.Impression;
import openrtb.bidrequest.model.PMP;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;
import openrtb.tables.AuctionType;

import java.util.*;
import java.util.Map.Entry;

/**
 * This is the standard Auction Service.
 * 
 * @author André Schmer
 *
 */
public class Auction2 {

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
						}
					}
				}
			}
		}

		HashMap<String, RtbAdProvider> winningProviderMap = new HashMap<String, RtbAdProvider>();
		for (Entry<String, List<Bidder>> e : dealBidListMap.entrySet()) {
			List<Bidder> dealBidList = e.getValue();
			// 1. als erstes die bids für die deals evaluieren
			if (false == dealBidList.isEmpty()) {
				Collections.sort(dealBidList);
				winningProviderMap.put(e.getKey(), evaluateWinner(info, dealBidList));
			}
		}

		for (Entry<String, List<Bidder>> e : nonDealBidListMap.entrySet()) {
			RtbAdProvider winningProvider = winningProviderMap.get(e.getKey());
			List<Bidder> nonDealBidList = e.getValue();
			// 2. evaluiere NON-Deals-Bids, falls kein DealBid bereits gewonnen hat
			if (winningProvider == null && false == nonDealBidList.isEmpty()) {
				Collections.sort(nonDealBidList);
				winningProviderMap.put(e.getKey(), evaluateWinner(info, nonDealBidList));
			}
		}

		Set<Entry<String, RtbAdProvider>> winningProviderSet = winningProviderMap.entrySet();
		AuctionResult dealWinner;
		if (winningProviderSet.size() > 1) {
			dealWinner = new MultipleAuctionResult();
			// just use the first one for the supplier
			dealWinner.setBidRequest((BidRequest) bidExchange.getAllBidRequests().values().toArray()[0]);
			for (Entry<String, RtbAdProvider> e : winningProviderSet) {
				if (e.getValue() != null) {
					((MultipleAuctionResult)dealWinner).addWinningProvider(e.getValue());
				}
			}

		} else {
			dealWinner = new SingularAuctionResult();
			String key = (String) winningProviderMap.keySet().toArray()[0];
			RtbAdProvider winningProvider = winningProviderMap.get(key);
			if (winningProvider != null) {
				dealWinner.setBidRequest(bidExchange.getBidRequest(winningProvider.getSupplier()));
				((SingularAuctionResult)dealWinner).setWinningProvider(winningProvider);
			}
		}

		return dealWinner;
	}

	// bidList must be sorted
	private static RtbAdProvider evaluateWinner(BiddingServiceInfo info, final List<Bidder> bidList) throws InvalidBidException {

	    AuctionWinner winner;
        if (info.getAuctionType() == AuctionType.SECOND_PRICE) {
            AuctionMethodHandler methodHandler = new SecondBestBidderHandler();
            winner = methodHandler.generate(bidList);
        } else {
            AuctionMethodHandler methodHandler = new SingleBidderHandler();
            winner = methodHandler.generate(bidList);
        }

        return new RtbAdProvider.Builder()
                .setIsValid(true)
                .setPrice(winner.getPrice())
                .setExchangedCurrencyPrice(winner.getExchangedPrice())
                .setSupplier(winner.getSupplier())
                .setWinningSeat(winner.getSeat())
                .setCurrency(winner.getCurrency())
                .setDealId(winner.getDealId())
                .build();
	}

	private static DirectDeal checkForDealMatch(final PMP pmp, final Bid bid) {
		if (pmp.getPrivate_auction() == 1 && pmp.getDeals() != null) {
			return pmp.getDeals().stream().filter(deal -> deal.getId().equals(bid.getDealid())).findFirst().orElse(null);
		}
		return null;
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

}
