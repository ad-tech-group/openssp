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

	private static Bid createBid(final float price, final String dealID) {
		final Bid bid = new Bid();
		bid.setId("1");
		bid.setPrice(price);
		bid.setImpid("1");
		bid.setDealid(dealID);

		return bid;
	}

	private static SeatBid createSeatBid(final Bid[] bids) {
		final SeatBid seatBid = new SeatBid();
		for (final Bid bid : bids) {
			seatBid.getBid().add(bid);
		}
		return seatBid;
	}

	private static Impression.Builder createImpression(final float floor, final float dealFloor, final String cur, final String dealid, final int private_auction) {
		final Impression.Builder imp = new Impression.Builder();
		imp.setId("1").setBidfloor(floor).setBidfloorcurrency(cur);
		if (dealid != null) {
			imp.setPmp(createPMP(private_auction, 1, dealFloor, dealid));
		}
		return imp;
	}

	private static PMP.Builder createPMP(final int privateAuction, final int countDeals, final float dealFloor, final String dealid) {
		final PMP.Builder pmp = new PMP.Builder();
		pmp.setPrivateAuction(privateAuction);
		createDeals(countDeals, dealFloor, dealid).forEach(d -> pmp.addDirectDeal(d));
		return pmp;
	}

	private static List<DirectDeal.Builder> createDeals(final int countDeals, final float dealFloor, final String dealid) {
		final List<DirectDeal.Builder> deals = new ArrayList<>();
		for (int i = 0; i < countDeals; i++) {
			final DirectDeal.Builder directDeal = new DirectDeal.Builder();
			directDeal.setId(dealid).setBidfloorcur("USD").setBidfloor(dealFloor);
			deals.add(directDeal);
		}
		return deals;
	}

	public static BidRequest.Builder createRequest(final float floor, final float dealFloor, final String cur, final String dealid, final int private_auction) {
		final BidRequest.Builder bidRequest = new BidRequest.Builder();
		// impression
		final Impression.Builder[] imps = new Impression.Builder[1];
		imps[0] = createImpression(floor, dealFloor, cur, dealid, private_auction);
		bidRequest.addImp(imps[0]);
		return bidRequest;
	}

	public static BidRequest.Builder createRequest(final float floor, final float[] dealFloor, final String cur, final String dealid, final int private_auction) {
		final BidRequest.Builder bidRequest = new BidRequest.Builder();
		// impression
		final Impression.Builder[] imps = new Impression.Builder[dealFloor.length];
		for (int i=0; i<dealFloor.length; i++) {
			imps[i] = createImpression(floor, dealFloor[i], cur, dealid, private_auction);
			bidRequest.addImp(imps[i]);
		}
		return bidRequest;
	}

	public static BidResponse createResponse(final float price, final String cur, final String dealId) {

		final BidResponse responseBuilder = new BidResponse();
		// Bid
		final Bid[] bids = new Bid[1];
		bids[0] = createBid(price, dealId);

		// SeatBid
		final SeatBid[] seats = new SeatBid[1];
		seats[0] = createSeatBid(bids);
		responseBuilder.setCur(cur);
		responseBuilder.addSeatBid(seats[0]);

		// response
		return responseBuilder;
	}

	public static BidResponse createResponseMultiBidMultiSeat(final float[][] prices, final String cur) {
		final BidResponse response = new BidResponse();

		final SeatBid[] seats = new SeatBid[prices.length];
		for (int i = 0; i < prices.length; i++) {
			final Bid[] bids = new Bid[prices[i].length];
			for (int j = 0; j < prices[i].length; j++) {
				bids[j] = createBid(prices[i][j], null);
			}
			// SeatBid
			seats[i] = createSeatBid(bids);
		}

		// response
		for (final SeatBid seatBid : seats) {
			response.addSeatBid(seatBid);
		}
		response.setCur(cur);
		return response;
	}

	public static BidResponse createResponseMultiBid(final float[] prices, final String cur) {
		final BidResponse response = new BidResponse();
		// Bid

		final Bid[] bids = new Bid[prices.length];
		for (int i = 0; i < prices.length; i++) {
			bids[i] = createBid(prices[i], null);
		}
		final SeatBid[] seats = new SeatBid[1];
		// SeatBid
		seats[0] = createSeatBid(bids);

		// response
		for (final SeatBid seatBid : seats) {
			response.addSeatBid(seatBid);
		}
		response.setCur(cur);
		return response;
	}
}
