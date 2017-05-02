package auction;

import java.util.ArrayList;
import java.util.List;

import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.DirectDeal;
import openrtb.bidrequest.model.Impression;
import openrtb.bidrequest.model.PMP;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;

/**
 * @author André Schmer
 *
 */
public class RequestResponseHelper {

	private static Bid.Builder createBid(final float price, final String dealID) {
		final Bid.Builder bidBuilder = new Bid.Builder();
		bidBuilder.setId("1");
		bidBuilder.setPrice(price);
		bidBuilder.setImpid("1");
		bidBuilder.setDealid(dealID);

		return bidBuilder;
	}

	private static SeatBid.Builder createSeatBid(final Bid.Builder[] bids) {
		final SeatBid.Builder seatBid = new SeatBid.Builder();
		for (final Bid.Builder bid : bids) {
			seatBid.addBid(bid);
		}
		return seatBid;
	}

	private static Impression.Builder createImpression(final float floor, final float dealFloor, final String cur,
			final String dealid, final int private_auction) {
		final Impression.Builder imp = new Impression.Builder();
		imp.setId("1").setBidfloor(floor).setBidfloorcurrency(cur);
		if (dealid != null) {
			imp.setPmp(createPMP(private_auction, 1, dealFloor, dealid));
		}
		return imp;
	}

	private static PMP.Builder createPMP(final int privateAuction, final int countDeals, final float dealFloor,
			final String dealid) {
		final PMP.Builder pmp = new PMP.Builder();
		pmp.setPrivateAuction(privateAuction);
		createDeals(countDeals, dealFloor, dealid).forEach(d -> pmp.addDirectDeal(d));
		return pmp;
	}

	private static List<DirectDeal.Builder> createDeals(final int countDeals, final float dealFloor,
			final String dealid) {
		final List<DirectDeal.Builder> deals = new ArrayList<>();
		for (int i = 0; i < countDeals; i++) {
			final DirectDeal.Builder directDeal = new DirectDeal.Builder();
			directDeal.setId(dealid).setBidfloorcur("USD").setBidfloor(dealFloor);
			deals.add(directDeal);
		}
		return deals;
	}

	public static BidRequest.Builder createRequest(final float floor, final float dealFloor, final String cur,
			final String dealid, final int private_auction) {
		final BidRequest.Builder bidRequest = new BidRequest.Builder();
		// impression
		final Impression.Builder[] imps = new Impression.Builder[1];
		imps[0] = createImpression(floor, dealFloor, cur, dealid, private_auction);
		bidRequest.addImp(imps[0]);
		return bidRequest;
	}

	public static BidResponse.Builder createResponse(final float price, final String cur, final String dealId) {
		final BidResponse.Builder responseBuilder = new BidResponse.Builder();
		// Bid
		final Bid.Builder[] bids = new Bid.Builder[1];
		bids[0] = createBid(price, dealId);

		// SeatBid
		final SeatBid.Builder[] seats = new SeatBid.Builder[1];
		seats[0] = createSeatBid(bids);

		// response
		return responseBuilder.addSeatbidBuilder(seats[0]).setCur(cur);
	}

	public static BidResponse.Builder createResponseMultiBidMultiSeat(final float[][] prices, final String cur) {
		final BidResponse.Builder response = new BidResponse.Builder();
		// Bid

		final SeatBid.Builder[] seats = new SeatBid.Builder[prices.length];
		for (int i = 0; i < prices.length; i++) {
			final Bid.Builder[] bids = new Bid.Builder[prices[i].length];
			for (int j = 0; j < prices[i].length; j++) {
				bids[j] = createBid(prices[i][j], null);
			}
			// SeatBid
			seats[i] = createSeatBid(bids);
		}

		// response
		for (final SeatBid.Builder seatBid : seats) {
			response.addSeatbidBuilder(seatBid);
		}
		response.setCur(cur);
		return response;
	}

	public static BidResponse.Builder createResponseMultiBid(final float[] prices, final String cur) {
		final BidResponse.Builder response = new BidResponse.Builder();
		// Bid

		final Bid.Builder[] bids = new Bid.Builder[prices.length];
		for (int i = 0; i < prices.length; i++) {
			bids[i] = createBid(prices[i], null);
		}
		final SeatBid.Builder[] seats = new SeatBid.Builder[1];
		// SeatBid
		seats[0] = createSeatBid(bids);

		// response
		for (final SeatBid.Builder seatBid : seats) {
			response.addSeatbidBuilder(seatBid);
		}
		response.setCur(cur);
		return response;
	}
}
