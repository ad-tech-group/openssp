package auction;

import java.util.ArrayList;
import java.util.List;

import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.exchange.Auction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.exception.InvalidBidException;

import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.DirectDeal;
import openrtb.bidrequest.model.Impression;
import openrtb.bidrequest.model.PMP;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;
import util.math.FloatComparator;

/**
 * @author André Schmer
 *
 */
public class AuctionServiceDealSingleBidTest {
	private static Supplier supplier;

	@BeforeClass
	public static void setUp() throws Exception {
		CurrencyCache.instance.put("EUR", 1.0f);
		CurrencyCache.instance.put("USD", 1.079f);
		CurrencyCache.instance.switchCache();

		supplier = new Supplier();
		supplier.setShortName("dsp1");
		supplier.setSupplierId(1l);
		supplier.setTmax(300);
	}

	@Before
	public void before() {

	}

	@After
	public void tearDown() throws Exception {}

	@Test
	public final void testSingleDealBidWins() {
		final BidExchange bidExchange = new BidExchange();

		// bidrequest
		final float impFloor = 0.88f;
		final float dealFloor = 3.f;
		final String currency = "USD";
		final BidRequest bidRequest = createRequest(impFloor, dealFloor, currency, true).build();

		// bidresponse
		final float bidPrice = 3.5f;
		final BidResponse response = createResponse(bidPrice, currency, "998877");

		bidExchange.setBidRequest(supplier, bidRequest);
		bidExchange.setBidResponse(supplier, response);
		BiddingServiceInfo info = new BiddingServiceInfo();
		try {
			final Auction.AuctionResult winner = Auction.auctioneer(info, bidExchange);
			Assert.assertEquals(impFloor, winner.getPrice(), 0);
			Assert.assertTrue(winner.isValid());
		} catch (final InvalidBidException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test(expected = InvalidBidException.class)
	public void testSingleDealBidLowerThanFloor() throws InvalidBidException {
		final BidExchange bidExchange = new BidExchange();
		final float bidPrice = 2.8f;
		final float impFloor = 3.f;
		final float dealFloor = 3.8f;
		final String currency = "USD";

		// bidrequest
		final BidRequest bidRequest = createRequest(impFloor, dealFloor, currency, true).build();

		// bidresponse
		final BidResponse response = createResponse(bidPrice, currency, "998877");
		bidExchange.setBidRequest(supplier, bidRequest);
		bidExchange.setBidResponse(supplier, response);
		BiddingServiceInfo info = new BiddingServiceInfo();
		Auction.auctioneer(info, bidExchange);
	}

	@Test
	public final void testSingleDealBidEqualsFloor() {
		final BidExchange bidExchange = new BidExchange();
		final float bidPrice = 3.f;
		final float impFloor = 0.88f;
		final float dealFloor = 3.f;
		final String currency = "USD";

		// bidrequest
		final BidRequest bidRequest = createRequest(impFloor, dealFloor, currency, true).build();

		// bidresponse
		final BidResponse response = createResponse(bidPrice, currency, "998877");

		bidExchange.setBidRequest(supplier, bidRequest);
		bidExchange.setBidResponse(supplier, response);
		BiddingServiceInfo info = new BiddingServiceInfo();
		try {
			final Auction.AuctionResult winner = Auction.auctioneer(info, bidExchange);
			Assert.assertEquals(impFloor, winner.getPrice(), 0);
			final float currencyRate = CurrencyCache.instance.get(currency);
			Assert.assertEquals(FloatComparator.rr(impFloor / currencyRate), winner.getExchangedCurrencyPrice(), 0);
			Assert.assertTrue(winner.isValid());
		} catch (final InvalidBidException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public final void testSingleDealBidFloorisEmpty() {
		final BidExchange bidExchange = new BidExchange();
		final float bidPrice = 3.f;
		final float impFloor = 0.88f;
		final float dealFloor = 0;
		final String currency = "USD";

		// bidrequest
		final BidRequest bidRequest = createRequest(impFloor, dealFloor, currency, true).build();

		// bidresponse
		final BidResponse response = createResponse(bidPrice, currency, "998877");
		bidExchange.setBidRequest(supplier, bidRequest);
		bidExchange.setBidResponse(supplier, response);
		BiddingServiceInfo info = new BiddingServiceInfo();
		try {
			final Auction.AuctionResult winner = Auction.auctioneer(info, bidExchange);
			final float currencyRate = CurrencyCache.instance.get(currency);
			Assert.assertEquals(impFloor, winner.getPrice(), 0);
			Assert.assertEquals(FloatComparator.rr(impFloor / currencyRate), winner.getExchangedCurrencyPrice(), 0);
			Assert.assertTrue(winner.isValid());
		} catch (final InvalidBidException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test(expected = InvalidBidException.class)
	public final void testSingleDealBidisEmpty() throws InvalidBidException {
		final BidExchange bidExchange = new BidExchange();
		final float bidPrice = .0f;
		final float impFloor = 0.88f;
		final float dealFloor = 3.0f;
		final String currency = "USD";

		// bidrequest
		final BidRequest bidRequest = createRequest(impFloor, dealFloor, currency, true).build();

		// bidresponse
		final BidResponse response = createResponse(bidPrice, currency, "998877");
		bidExchange.setBidRequest(supplier, bidRequest);
		bidExchange.setBidResponse(supplier, response);
		BiddingServiceInfo info = new BiddingServiceInfo();
		Auction.auctioneer(info, bidExchange);
	}

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
		for (final Bid builder : bids) {
			seatBid.getBid().add(builder);
		}
		return seatBid;
	}

	private Impression.Builder createImpression(final float floor, final float dealFloor, final String cur, final boolean withPMP) {
		final Impression.Builder imp = new Impression.Builder();
		imp.setId("1").setBidfloor(floor).setBidfloorcurrency(cur);
		if (withPMP) {
			imp.setPmp(createPMP(0, 1, dealFloor));
		}
		return imp;
	}

	private PMP.Builder createPMP(final int privateAuction, final int countDeals, final float dealFloor) {
		final PMP.Builder pmp = new PMP.Builder();
		pmp.setPrivateAuction(privateAuction);
		createDeals(countDeals, dealFloor).forEach(d -> pmp.addDirectDeal(d));
		return pmp;
	}

	private List<DirectDeal.Builder> createDeals(final int countDeals, final float dealFloor) {
		final List<DirectDeal.Builder> deals = new ArrayList<>();
		for (int i = 0; i < countDeals; i++) {
			final DirectDeal.Builder directDeal = new DirectDeal.Builder();
			directDeal.setId("99887" + i).setBidfloorcur("USD").setBidfloor(dealFloor);
			deals.add(directDeal);
		}
		return deals;
	}

	private BidRequest.Builder createRequest(final float floor, final float dealFloor, final String cur, final boolean withPMP) {
		final BidRequest.Builder bidRequest = new BidRequest.Builder();
		// impression
		final Impression.Builder[] imps = new Impression.Builder[1];
		imps[0] = createImpression(floor, dealFloor, cur, withPMP);
		bidRequest.addImp(imps[0]);
		return bidRequest;
	}

	private static BidResponse createResponse(final float price, final String cur, final String dealId) {
		final BidResponse response = new BidResponse();
		// Bid
		final Bid[] bids = new Bid[1];
		bids[0] = createBid(price, dealId);

		// SeatBid
		final SeatBid[] seats = new SeatBid[1];
		seats[0] = createSeatBid(bids);

		// response
		for (final SeatBid seatBid : seats) {
			response.addSeatBid(seatBid);
		}
		response.setCur(cur);
		return response;
	}
}
