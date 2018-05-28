package request;

import java.io.IOException;

import openrtb.bidrequest.model.*;
import openrtb.tables.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openrtb.validator.OpenRtbInputType;
import org.openrtb.validator.OpenRtbValidator;
import org.openrtb.validator.OpenRtbValidatorFactory;
import org.openrtb.validator.OpenRtbVersion;
import org.openrtb.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author André Schmer
 *
 */
public class BidRequestBuilderTest {

	Logger log = LoggerFactory.getLogger(BidRequestBuilderTest.class);

    private static Gson gson1_0 = new GsonBuilder().setVersion(1.0).create();
    private static Gson gson2_0 = new GsonBuilder().setVersion(2.0).create();
    private static Gson gson2_1 = new GsonBuilder().setVersion(2.1).create();
    private static Gson gson2_2 = new GsonBuilder().setVersion(2.2).create();
    private static Gson gson2_3 = new GsonBuilder().setVersion(2.3).create();
    private static Gson gson2_4 = new GsonBuilder().setVersion(2.4).create();
	private static BidRequest bidRequestVideo = null;
    private static BidRequest bidRequestBanner = null;

	@BeforeClass
	public static void setUp() {
	    //TODO:  BKS add app testing
        //TODO:  BKS need native testing

        bidRequestBanner = new BidRequest.Builder()
                .setId("1234U8Bc3")
                .setSite(new Site.Builder()
                        .setId("1234")
                        .setDomain("yourdomain.com")
                        .addPagecat(ContentCategory.IAB2)
                        .addSectioncat(ContentCategory.IAB3)
                        .addCat(ContentCategory.IAB1)
                        .addCat(ContentCategory.IAB11)
                        .setName("yourSiteObject")
                        .setPage("page").build()
                )
                .setDevice(new Device.Builder()
                        .setUa("Smart Browser")
                        .setGeo(new Geo.Builder()
                                .setType(GeoType.IP)
                                .setCity("Hamburg")
                                .setCountry("DEU")
                                .setRegion("region")
                                .setLat(53.563452f)
                                .setLon(9.925742f)
                                .setZip("22761")
                                .setUtcOffset(1)
                                .build()
                        )
                        .setIp("192.168.0.1")
                        .setDeviceType(DeviceType.MOBILE_OR_TABLET)
                        .setMake("Apple")
                        .setModel("IPhone5")
                        .setOs("MyOs")
                        .setOsv("1.5")
                        .setHwv("6")
                        .setH(240)
                        .setW(300)
                        .setPpi(8)
                        .setPxratio(3f)
                        .setJs(JavascriptSupport.NO)
                        .setFlashver("2")
                        .setLanguage("en")
                        .setCarrier("Sprint")
                        .setConnectiontype(NetworkConnectionType.CELLULAR_NETWORK_3G)
                        .setIfa("my-ifa")
                        .setDidsha1("my-didsha1")
                        .setDidmd5("my-didmd5")
                        .setDpidsha1("my-dpidsha1")
                        .setDpidmd5("my-dpidmd5")
                        .setMacsha1("my-macsha1")
                        .setMacmd5("my-macmd5")
                        .build()
                ).addImp(new Impression.Builder()
                        .setImpid("45")
                        .setId("45")
                        .setBanner(new Banner.Builder()
                                .setId("123-33")
                                .setW(600)
                                .setH(400)
                                .setPos(AddPosition.FOOTER)
                                .addBattr(CreativeAttribute.AD_PROVICES_SKIP_BUTTON)
                                .addBtype(BannerAdType.XHTML_TEXT_AD)
                                .setMimes(new String[] {"text/html"})
                                .build()
                        )
                        .setBidfloor(0.01f)
                        .setSecure(ImpressionSecurity.NON_SECURE)
                        .build()
                )
                .setUser(new User.Builder()
                        .setBuyeruid("HHcFrt-76Gh4aPl")
                        .setGender(Gender.MALE)
                        .setId("99")
                        .setYob(1981).build()
                )
                .setAt(AuctionType.SECOND_PRICE)
                .setTmax(120)
                .addCur("EUR")
                .addBcat(ContentCategory.IAB11)
                .addBadv("ford.com")
                .build();

		bidRequestVideo = new BidRequest.Builder()
                .setId("1234U8Bc3")
                .setSite(new Site.Builder()
                        .setId("1234")
                        .setDomain("mydomain.com")
                        .addPagecat(ContentCategory.IAB2)
                        .addSectioncat(ContentCategory.IAB3)
                        .addCat(ContentCategory.IAB1)
                        .addCat(ContentCategory.IAB11)
                        .setName("mySiteObject")
                        .setPage("page").build()
                )
                .setDevice(new Device.Builder()
                        .setUa("Smart Browser")
                        .setGeo(new Geo.Builder()
                                .setType(GeoType.IP)
                                .setCity("Hamburg")
                                .setCountry("DEU")
                                .setRegion("region")
                                .setLat(53.563452f)
                                .setLon(9.925742f)
                                .setZip("22761")
                                .setUtcOffset(1)
                                .build()
                        )
                        .setIp("192.168.0.1")
                        .setDeviceType(DeviceType.MOBILE_OR_TABLET)
                        .setMake("Apple")
                        .setModel("IPhone5")
                        .setOs("MyOs")
                        .setOsv("1.5")
                        .setHwv("6")
                        .setH(240)
                        .setW(300)
                        .setPpi(8)
                        .setPxratio(3f)
                        .setJs(JavascriptSupport.NO)
                        .setFlashver("2")
                        .setLanguage("en")
                        .setCarrier("Sprint")
                        .setConnectiontype(NetworkConnectionType.CELLULAR_NETWORK_3G)
                        .setIfa("my-ifa")
                        .setDidsha1("my-didsha1")
                        .setDidmd5("my-didmd5")
                        .setDpidsha1("my-dpidsha1")
                        .setDpidmd5("my-dpidmd5")
                        .setMacsha1("my-macsha1")
                        .setMacmd5("my-macmd5")
                        .build()
                ).addImp(new Impression.Builder()
                        .setImpid("45")
                        .setId("45")
                        .setVideo(new Video.Builder()
                                .addMime("application/x-shockwave-flash")
                                .setH(400)
                                .setW(600)
                                .setMaxduration(100)
                                .setMinduration(30)
                                .setProtocol(VideoBidResponseProtocol.VAST_2_0)
                                .addToProtocols(VideoBidResponseProtocol.VAST_2_0)
                                .addApi(ApiFramework.MRAID_1)
                                .setLinearity(VideoLinearity.LINEAR)
                                // start delay has a maximum of 0 until 2.3
                                .setStartdelay(-1)
                                .addBattr(CreativeAttribute.AD_PROVICES_SKIP_BUTTON)
                                .build()
                        )
                        .setBidfloor(0.01f)
                        .setSecure(ImpressionSecurity.NON_SECURE)
                        .build()
                )
                .setUser(new User.Builder()
                        .setBuyeruid("HHcFrt-76Gh4aPl")
                        .setGender(Gender.MALE)
                        .setId("99")
                        .setYob(1981).build()
                )
                .setAt(AuctionType.FIRST_PRICE)
                .setTmax(120)
                .addCur("USD")
                .addBcat(ContentCategory.IAB1)
                .addBadv("ford.com")
                .build();

        //System.out.println(new Gson().toJson(bidRequestBanner));
        //System.out.println(new Gson().toJson(bidRequestVideo));

    }

	@Test
	public void testBidRequest() {
		// Site
		Assert.assertEquals(ContentCategory.IAB1, bidRequestVideo.getSite().getCat().get(0));
		Assert.assertEquals(ContentCategory.IAB11, bidRequestVideo.getSite().getCat().get(1));
		Assert.assertEquals(2, bidRequestVideo.getSite().getCat().size());
		Assert.assertEquals(ContentCategory.IAB2, bidRequestVideo.getSite().getPagecat().get(0));
		Assert.assertEquals(1, bidRequestVideo.getSite().getPagecat().size());
		Assert.assertEquals(ContentCategory.IAB3, bidRequestVideo.getSite().getSectioncat().get(0));
		Assert.assertEquals(1, bidRequestVideo.getSite().getSectioncat().size());
		Assert.assertEquals("mydomain.com", bidRequestVideo.getSite().getDomain());
		Assert.assertEquals("mySiteObject", bidRequestVideo.getSite().getName());
		Assert.assertEquals("page", bidRequestVideo.getSite().getPage());

		// Device -> Geo
		Assert.assertEquals("Hamburg", bidRequestVideo.getDevice().getGeo().getCity());
		Assert.assertEquals("DEU", bidRequestVideo.getDevice().getGeo().getCountry());
		Assert.assertEquals(53.563452f, bidRequestVideo.getDevice().getGeo().getLat(), 0);
		Assert.assertEquals(9.925742f, bidRequestVideo.getDevice().getGeo().getLon(), 0);
		Assert.assertEquals("22761", bidRequestVideo.getDevice().getGeo().getZip());

		// Impression
		Assert.assertEquals(1, bidRequestVideo.getImp().size());
		Assert.assertNotNull(bidRequestVideo.getImp().get(0));
		Assert.assertEquals("45", bidRequestVideo.getImp().get(0).getId());
		// Impression -> Video
		Assert.assertEquals(400, bidRequestVideo.getImp().get(0).getVideo().getH());
		Assert.assertEquals(600, bidRequestVideo.getImp().get(0).getVideo().getW());
		Assert.assertEquals(100, bidRequestVideo.getImp().get(0).getVideo().getMaxduration());
		Assert.assertEquals(30, bidRequestVideo.getImp().get(0).getVideo().getMinduration());
		Assert.assertEquals(-1, bidRequestVideo.getImp().get(0).getVideo().getStartdelay());
		//TODO: BKS
		Assert.assertEquals(1, bidRequestVideo.getImp().get(0).getVideo().getProtocols().size());
		Assert.assertEquals(2, bidRequestVideo.getImp().get(0).getVideo().getProtocols().get(0).getValue());
		Assert.assertEquals(1, bidRequestVideo.getImp().get(0).getVideo().getMimes().size());
		Assert.assertEquals("application/x-shockwave-flash", bidRequestVideo.getImp().get(0).getVideo().getMimes().get(0));

		// User
		Assert.assertEquals("99", bidRequestVideo.getUser().getId());
		Assert.assertEquals("HHcFrt-76Gh4aPl", bidRequestVideo.getUser().getBuyeruid());
		Assert.assertEquals("M", bidRequestVideo.getUser().getGender());
		Assert.assertEquals(1981, bidRequestVideo.getUser().getYob());
	}

	@Test
	public void testBuilderException() {

	}

	@Test
	public void testValidator1_0_Banner() throws IOException {
		final String jsonBidrequest = gson1_0.toJson(bidRequestVideo.getBuilder().build(), BidRequest.class);
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V1_0);
		final ValidationResult result = validator.validate(jsonBidrequest);
		log.info(result.getResult());
		Assert.assertTrue(validator.isValid(jsonBidrequest));
	}

	@Test
	public void testValidator2_0_Banner() throws IOException {
		final String jsonBidrequest = gson2_0.toJson(bidRequestVideo.getBuilder().build(), BidRequest.class);
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_0);
		final ValidationResult result = validator.validate(jsonBidrequest);
		log.info(result.getResult());
		Assert.assertTrue(validator.isValid(jsonBidrequest));
	}

	@Test
	public void testValidator2_1_Banner() throws IOException {
		final String jsonBidrequest = gson2_1.toJson(bidRequestBanner.getBuilder().build(), BidRequest.class);
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_1);
		final ValidationResult result = validator.validate(jsonBidrequest);
		log.info(result.getResult());
		Assert.assertTrue(validator.isValid(jsonBidrequest));
	}

	@Test
	public void testValidator2_2_Banner() throws IOException {
		final String jsonBidrequest = gson2_2.toJson(bidRequestBanner.getBuilder().build(), BidRequest.class);
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_2);
		final ValidationResult result = validator.validate(jsonBidrequest);
		log.info(result.getResult());
		Assert.assertTrue(validator.isValid(jsonBidrequest));
	}

	@Test
	public void testValidator2_3_Banner() throws IOException {
		final String jsonBidrequest = gson2_3.toJson(bidRequestBanner.getBuilder().build(), BidRequest.class);
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_3);
		final ValidationResult result = validator.validate(jsonBidrequest);
		log.info(result.getResult());
		Assert.assertTrue(validator.isValid(jsonBidrequest));
	}

	@Test
	public void testValidator2_4_Banner() throws IOException {
		final String jsonBidrequest = gson2_4.toJson(bidRequestBanner.getBuilder().build(), BidRequest.class);
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_4);
		final ValidationResult result = validator.validate(jsonBidrequest);
		log.info(result.getResult());
		Assert.assertTrue(validator.isValid(jsonBidrequest));
	}

    @Test
    public void testValidator1_0_Video() throws IOException {
        final String jsonBidrequest = gson1_0.toJson(bidRequestVideo.getBuilder().build(), BidRequest.class);
        final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V1_0);
        final ValidationResult result = validator.validate(jsonBidrequest);
        log.info(result.getResult());
        Assert.assertTrue(validator.isValid(jsonBidrequest));
    }

    @Test
    public void testValidator2_0_Video() throws IOException {
        final String jsonBidrequest = gson2_0.toJson(bidRequestVideo.getBuilder().build(), BidRequest.class);
        final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_0);
        final ValidationResult result = validator.validate(jsonBidrequest);
        log.info(result.getResult());
        Assert.assertTrue(validator.isValid(jsonBidrequest));
    }

    @Test
    public void testValidator2_1_Video() throws IOException {
        final String jsonBidrequest = gson2_1.toJson(bidRequestVideo.getBuilder().build(), BidRequest.class);
        final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_1);
        final ValidationResult result = validator.validate(jsonBidrequest);
        log.info(result.getResult());
        Assert.assertTrue(validator.isValid(jsonBidrequest));
    }

    @Test
    public void testValidator2_2_Video() throws IOException {
        final String jsonBidrequest = gson2_2.toJson(bidRequestVideo.getBuilder().build(), BidRequest.class);
        final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_2);
        final ValidationResult result = validator.validate(jsonBidrequest);
        log.info(result.getResult());
        Assert.assertTrue(validator.isValid(jsonBidrequest));
    }

    @Test
    public void testValidator2_3_Video() throws IOException {
        final String jsonBidrequest = gson2_3.toJson(bidRequestVideo.getBuilder().build(), BidRequest.class);
        final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_3);
        final ValidationResult result = validator.validate(jsonBidrequest);
        log.info(result.getResult());
        Assert.assertTrue(validator.isValid(jsonBidrequest));
    }

    @Test
    public void testValidator2_4_Video() throws IOException {
        final String jsonBidrequest = gson2_4.toJson(bidRequestVideo.getBuilder().build(), BidRequest.class);
        final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_4);
        final ValidationResult result = validator.validate(jsonBidrequest);
        log.info(result.getResult());
        Assert.assertTrue(validator.isValid(jsonBidrequest));
    }

}
