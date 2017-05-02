package auction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.core.exchange.Auction;
import com.atg.openssp.core.exchange.RtbAdProvider;

import common.InvalidBidException;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidresponse.model.BidResponse;
import util.math.FloatComparator;

/**
 * @author André Schmer
 *
 */
public class PrivateAuctionServiceDealMultiBidTest {

	private static Supplier supplier1;
	private static Supplier supplier2;
	private static Supplier supplier3;
	private static Supplier supplier4;

	public PrivateAuctionServiceDealMultiBidTest() {}

	@BeforeClass
	public static void setUp() throws Exception {
		CurrencyCache.instance.put("EUR", 1.0f);
		CurrencyCache.instance.put("USD", 1.079f);
		CurrencyCache.instance.switchCache();

		supplier1 = new Supplier();
		supplier1.setShortName("dsp1");
		supplier1.setSupplierId(1l);

		supplier2 = new Supplier();
		supplier2.setShortName("dsp2");
		supplier2.setSupplierId(2l);

		supplier3 = new Supplier();
		supplier3.setShortName("dsp3");
		supplier3.setSupplierId(3l);

		supplier4 = new Supplier();
		supplier4.setShortName("dsp4");
		supplier4.setSupplierId(4l);
	}

	@Before
	public void before() {

	}

	public void tearDown() throws Exception {}

	@Test
	public final void testMultiBidDealWins() {
		final BidExchange bidExchange = new BidExchange();
		final float impFloor = 0.88f;
		final float dealFloor1 = 3.f;
		final float dealFloor2 = 2.8f;
		final String currency = "USD";

		final String deal_id_1 = "998877";
		final String deal_id_2 = "998866";

		// bidrequest1
		final BidRequest.Builder bidRequest1 = RequestResponseHelper.createRequest(impFloor, dealFloor1, currency, deal_id_1, 1);
		// bidrequest1
		final BidRequest.Builder bidRequest2 = RequestResponseHelper.createRequest(impFloor, dealFloor2, currency, deal_id_2, 1);

		// bidresponse, price in USD
		final float bidPrice1 = 3.5f;
		final BidResponse.Builder response = RequestResponseHelper.createResponse(bidPrice1, currency, deal_id_1);
		bidExchange.setBidRequest(supplier1, bidRequest1);
		bidExchange.setBidResponse(supplier1, response);

		// bidresponse2, price in USD
		final float bidPrice2 = 4.10f;
		final BidResponse.Builder response2 = RequestResponseHelper.createResponse(bidPrice2, currency, deal_id_2);
		bidExchange.setBidRequest(supplier2, bidRequest2);
		bidExchange.setBidResponse(supplier2, response2);

		try {
			final RtbAdProvider winner = Auction.auctioneer(bidExchange);
			Assert.assertTrue(winner.isValid());
			Assert.assertEquals(3.51f, winner.getPrice(), 0);
			final float currencyRateEUR = CurrencyCache.instance.get(currency);
			Assert.assertEquals(FloatComparator.rr(3.51f / currencyRateEUR), winner.getPriceEur(), 0);
			Assert.assertEquals(supplier2.getShortName(), winner.getSupplier().getShortName());
			Assert.assertEquals("998866", winner.getDealId());
		} catch (final InvalidBidException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test(expected = InvalidBidException.class)
	public final void testMultiBidNoWinner() throws InvalidBidException {
		final BidExchange bidExchange = new BidExchange();
		final float impFloor = 0.88f;
		final float dealFloor1 = 3.f;
		final float dealFloor2 = 3.15f;
		final String currency = "USD";

		final String deal_id_1 = "998877";
		final String deal_id_2 = "998866";

		// bidrequest1
		final BidRequest.Builder bidRequest1 = RequestResponseHelper.createRequest(impFloor, dealFloor1, currency, deal_id_1, 1);
		// bidrequest2
		final BidRequest.Builder bidRequest2 = RequestResponseHelper.createRequest(impFloor, dealFloor2, currency, deal_id_2, 1);

		// bidresponse1 Deal
		final float bidPrice1 = 2.8f;
		final BidResponse.Builder response1 = RequestResponseHelper.createResponse(bidPrice1, currency, deal_id_1);
		bidExchange.setBidRequest(supplier1, bidRequest1);
		bidExchange.setBidResponse(supplier1, response1);

		// bidresponse2 Deal
		final float bidPrice2 = 2.9f;
		final BidResponse.Builder response2 = RequestResponseHelper.createResponse(bidPrice2, currency, deal_id_2);
		bidExchange.setBidRequest(supplier3, bidRequest2);
		bidExchange.setBidResponse(supplier3, response2);

		// bidresponse3 NON Deal
		final float bidPrice3 = 1.10f;
		final BidResponse.Builder response3 = RequestResponseHelper.createResponse(bidPrice3, currency, null);
		bidExchange.setBidRequest(supplier2, bidRequest1);
		bidExchange.setBidResponse(supplier2, response3);

		Auction.auctioneer(bidExchange);
		Assert.fail();
	}

	@Test
	public final void testMultiBidHigherDealWins() {
		final BidExchange bidExchange = new BidExchange();
		final float impFloor = 0.88f;
		final float dealFloor1 = 3.f;
		final float dealFloor2 = 3.15f;
		final String currency = "USD";

		final String deal_id_1 = "998877";
		final String deal_id_2 = "998866";

		// bidrequest
		final BidRequest.Builder bidRequest1 = RequestResponseHelper.createRequest(impFloor, dealFloor1, currency, deal_id_1, 1);
		// bidresponse1 Deal
		final float bidPrice1 = 2.8f;
		final BidResponse.Builder response1 = RequestResponseHelper.createResponse(bidPrice1, currency, deal_id_1);
		bidExchange.setBidRequest(supplier1, bidRequest1);
		bidExchange.setBidResponse(supplier1, response1);

		// bidrequest
		final BidRequest.Builder bidRequest2 = RequestResponseHelper.createRequest(impFloor, dealFloor2, currency, deal_id_2, 1);
		// bidresponse2 Deal
		final float bidPrice2 = 3.20f;
		final BidResponse.Builder response2 = RequestResponseHelper.createResponse(bidPrice2, currency, deal_id_2);
		bidExchange.setBidRequest(supplier3, bidRequest2);
		bidExchange.setBidResponse(supplier3, response2);

		// bidresponse3 NON Deal
		final float bidPrice3 = 1.10f;
		final BidResponse.Builder response3 = RequestResponseHelper.createResponse(bidPrice3, currency, null);
		bidExchange.setBidRequest(supplier2, bidRequest1);
		bidExchange.setBidResponse(supplier2, response3);

		// bidresponse4 NON Deal
		final float bidPrice4 = 1.50f;
		final BidResponse.Builder response4 = RequestResponseHelper.createResponse(bidPrice4, currency, null);
		bidExchange.setBidRequest(supplier4, bidRequest1);
		bidExchange.setBidResponse(supplier4, response4);

		try {
			final RtbAdProvider winner = Auction.auctioneer(bidExchange);
			Assert.assertTrue(winner.isValid());
			Assert.assertEquals(winner.getPrice(), 3.15f, 0);
			final float currencyRateEUR = CurrencyCache.instance.get(currency);
			Assert.assertEquals(FloatComparator.rr(3.15f / currencyRateEUR), winner.getPriceEur(), 0);
			Assert.assertEquals(winner.getSupplier().getShortName(), supplier3.getShortName());
			Assert.assertEquals(winner.getDealId(), deal_id_2);
		} catch (final InvalidBidException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
		}
	}

}
