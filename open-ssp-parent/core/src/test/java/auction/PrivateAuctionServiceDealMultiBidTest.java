package auction;

import com.atg.openssp.common.core.entry.BiddingServiceInfo;
import com.atg.openssp.common.core.exchange.Auction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.demand.BidExchange;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.exception.InvalidBidException;

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
		supplier1.setTmax(300);

		supplier2 = new Supplier();
		supplier2.setShortName("dsp2");
		supplier2.setSupplierId(2l);
		supplier2.setTmax(300);

		supplier3 = new Supplier();
		supplier3.setShortName("dsp3");
		supplier3.setSupplierId(3l);
		supplier3.setTmax(300);

		supplier4 = new Supplier();
		supplier4.setShortName("dsp4");
		supplier4.setSupplierId(4l);
		supplier4.setTmax(300);
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
		final BidRequest bidRequest1 = RequestResponseHelper.createRequest(impFloor, dealFloor1, currency, deal_id_1, 1).build();
		// bidrequest1
		final BidRequest bidRequest2 = RequestResponseHelper.createRequest(impFloor, dealFloor2, currency, deal_id_2, 1).build();

		// bidresponse, price in USD
		final float bidPrice1 = 3.5f;
		final BidResponse response = RequestResponseHelper.createResponse(bidPrice1, currency, deal_id_1);
		bidExchange.setBidRequest(supplier1, bidRequest1);
		bidExchange.setBidResponse(supplier1, response);

		// bidresponse2, price in USD
		final float bidPrice2 = 4.10f;
		final BidResponse response2 = RequestResponseHelper.createResponse(bidPrice2, currency, deal_id_2);
		bidExchange.setBidRequest(supplier2, bidRequest2);
		bidExchange.setBidResponse(supplier2, response2);

		BiddingServiceInfo info = new BiddingServiceInfo();
		try {
			final Auction.AuctionResult winner = Auction.auctioneer(info, bidExchange);
			Assert.assertTrue(winner.isValid());
			Assert.assertEquals(3.51f, winner.getPrice(), 0);
			final float currencyRateEUR = CurrencyCache.instance.get(currency);
			Assert.assertEquals(FloatComparator.rr(3.51f / currencyRateEUR), winner.getExchangedCurrencyPrice(), 0);
			Assert.assertEquals(supplier2.getShortName(), winner.getSupplier().getShortName());
			Assert.assertEquals("998866", winner.getDealId());
		} catch (final InvalidBidException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public final void testMultiBidMultiImpressionDealWins() {
		final BidExchange bidExchange = new BidExchange();
		final float impFloor = 0.88f;
		final float dealFloor1a = 3.f;
		final float dealFloor1b = 3.1f;
		final float dealFloor1c = 3.6f;
		final float dealFloor2a = 2.8f;
		final float dealFloor2b = 2.9f;
		final float dealFloor2c = 1.8f;
		final String currency = "USD";

		final String deal_id_1 = "998877";
		final String deal_id_2 = "998866";

		// bidrequest1
		final BidRequest bidRequest1 = RequestResponseHelper.createRequest(impFloor, new float[]{dealFloor1a, dealFloor1b, dealFloor1c}, currency, deal_id_1, 1).build();

		// bidrequest1
		final BidRequest bidRequest2 = RequestResponseHelper.createRequest(impFloor, new float[]{dealFloor2a, dealFloor2b, dealFloor2c}, currency, deal_id_2, 1).build();

		// bidresponse, price in USD
		final float bidPrice1 = 3.5f;
		final BidResponse response = RequestResponseHelper.createResponse(bidPrice1, currency, deal_id_1);
		bidExchange.setBidRequest(supplier1, bidRequest1);
		bidExchange.setBidResponse(supplier1, response);

		// bidresponse2, price in USD
		final float bidPrice2 = 4.10f;
		final BidResponse response2 = RequestResponseHelper.createResponse(bidPrice2, currency, deal_id_2);
		bidExchange.setBidRequest(supplier2, bidRequest2);
		bidExchange.setBidResponse(supplier2, response2);

		BiddingServiceInfo info = new BiddingServiceInfo();
		try {
			final Auction.AuctionResult winner = Auction.auctioneer(info, bidExchange);
			Assert.assertTrue(winner.isValid());
			Assert.assertEquals(4.10f, winner.getPrice(), 0);
			final float currencyRateEUR = CurrencyCache.instance.get(currency);
			System.out.println(winner.getExchangedCurrencyPrice()*currencyRateEUR);
			Assert.assertEquals(FloatComparator.rr(4.10f / currencyRateEUR), winner.getExchangedCurrencyPrice(), 0);
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
		final BidRequest bidRequest1 = RequestResponseHelper.createRequest(impFloor, dealFloor1, currency, deal_id_1, 1).build();
		// bidrequest2
		final BidRequest bidRequest2 = RequestResponseHelper.createRequest(impFloor, dealFloor2, currency, deal_id_2, 1).build();

		// bidresponse1 Deal
		final float bidPrice1 = 2.8f;
		final BidResponse response1 = RequestResponseHelper.createResponse(bidPrice1, currency, deal_id_1);
		bidExchange.setBidRequest(supplier1, bidRequest1);
		bidExchange.setBidResponse(supplier1, response1);

		// bidresponse2 Deal
		final float bidPrice2 = 2.9f;
		final BidResponse response2 = RequestResponseHelper.createResponse(bidPrice2, currency, deal_id_2);
		bidExchange.setBidRequest(supplier3, bidRequest2);
		bidExchange.setBidResponse(supplier3, response2);

		// bidresponse3 NON Deal
		final float bidPrice3 = 1.10f;
		final BidResponse response3 = RequestResponseHelper.createResponse(bidPrice3, currency, null);
		bidExchange.setBidRequest(supplier2, bidRequest1);
		bidExchange.setBidResponse(supplier2, response3);

		BiddingServiceInfo info = new BiddingServiceInfo();
		Auction.auctioneer(info, bidExchange);
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
		final BidRequest bidRequest1 = RequestResponseHelper.createRequest(impFloor, dealFloor1, currency, deal_id_1, 1).build();
		// bidresponse1 Deal
		final float bidPrice1 = 2.8f;
		final BidResponse response1 = RequestResponseHelper.createResponse(bidPrice1, currency, deal_id_1);
		bidExchange.setBidRequest(supplier1, bidRequest1);
		bidExchange.setBidResponse(supplier1, response1);

		// bidrequest
		final BidRequest bidRequest2 = RequestResponseHelper.createRequest(impFloor, dealFloor2, currency, deal_id_2, 1).build();
		// bidresponse2 Deal
		final float bidPrice2 = 3.20f;
		final BidResponse response2 = RequestResponseHelper.createResponse(bidPrice2, currency, deal_id_2);
		bidExchange.setBidRequest(supplier3, bidRequest2);
		bidExchange.setBidResponse(supplier3, response2);

		// bidresponse3 NON Deal
		final float bidPrice3 = 1.10f;
		final BidResponse response3 = RequestResponseHelper.createResponse(bidPrice3, currency, null);
		bidExchange.setBidRequest(supplier2, bidRequest1);
		bidExchange.setBidResponse(supplier2, response3);

		// bidresponse4 NON Deal
		final float bidPrice4 = 1.50f;
		final BidResponse response4 = RequestResponseHelper.createResponse(bidPrice4, currency, null);
		bidExchange.setBidRequest(supplier4, bidRequest1);
		bidExchange.setBidResponse(supplier4, response4);

		BiddingServiceInfo info = new BiddingServiceInfo();
		try {
			final Auction.AuctionResult winner = Auction.auctioneer(info, bidExchange);
			Assert.assertTrue(winner.isValid());
			Assert.assertEquals(winner.getPrice(), 3.15f, 0);
			final float currencyRateEUR = CurrencyCache.instance.get(currency);
			Assert.assertEquals(FloatComparator.rr(3.15f / currencyRateEUR), winner.getExchangedCurrencyPrice(), 0);
			Assert.assertEquals(winner.getSupplier().getShortName(), supplier3.getShortName());
			Assert.assertEquals(winner.getDealId(), deal_id_2);
		} catch (final InvalidBidException e) {
			Assert.fail("Exception thrown: " + e.getMessage());
		}
	}

}
