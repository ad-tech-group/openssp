package request;

import java.io.IOException;

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

import openrtb.bidrequest.extension.Contact;
import openrtb.bidrequest.extension.ContactGroup;
import openrtb.bidrequest.extension.ContactGroupBuilder;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.Device;
import openrtb.bidrequest.model.Gender;
import openrtb.bidrequest.model.Geo;
import openrtb.bidrequest.model.Impression;
import openrtb.bidrequest.model.Site;
import openrtb.bidrequest.model.User;
import openrtb.bidrequest.model.Video;
import openrtb.tables.VideoBidResponseProtocol;

/**
 * 
 * @author André Schmer
 *
 */
public class BidRequestBuilderTest {

	Logger log = LoggerFactory.getLogger(BidRequestBuilderTest.class);

	private static BidRequest bidRequest = null;
	private static Gson gson;

	@BeforeClass
	public static void setUp() {
		bidRequest = new BidRequest.Builder().setId("1234U8Bc3").setSite(new Site.Builder().setId("1234").setDomain("domain").addCat("IAB1").addCat("IAB1-1").setName(
		        "mySiteObject").setPage("page").build()).setDevice(new Device.Builder().setGeo(new Geo.Builder().setCity("Hamburg").setCountry("DEU").setLat(53.563452f).setLon(
		                9.925742f).setZip("22761").build()).build()).addImp(new Impression.Builder().setId("45").setVideo(new Video.Builder().addMime(
		                        "application/x-shockwave-flash").setH(400).setW(600).setMaxduration(100).setMinduration(30).addProtocol(VideoBidResponseProtocol.VAST_2_0
		                                .getValue()).setStartdelay(1).build()).build()).setUser(new User.Builder().setBuyeruid("HHcFrt-76Gh4aPl").setGender(Gender.MALE).setId("99")
		                                        .setYob(1981).build()).setExtension(new ContactGroupBuilder().addContactGroup(new Contact.Builder().setAgevarianz("20-29").setAge(
		                                                "23").setGender(Gender.MALE).build()).build()).build();

		gson = new GsonBuilder().setVersion(2.4).create();
	}

	@Test
	public void testBidRequest() {
		// Site
		Assert.assertEquals("IAB1", bidRequest.getSite().getCat().get(0));
		Assert.assertEquals("IAB1-1", bidRequest.getSite().getCat().get(1));
		Assert.assertEquals(2, bidRequest.getSite().getCat().size());
		Assert.assertEquals("domain", bidRequest.getSite().getDomain());
		Assert.assertEquals("mySiteObject", bidRequest.getSite().getName());
		Assert.assertEquals("page", bidRequest.getSite().getPage());

		// Device -> Geo
		Assert.assertEquals("Hamburg", bidRequest.getDevice().getGeo().getCity());
		Assert.assertEquals("DEU", bidRequest.getDevice().getGeo().getCountry());
		Assert.assertEquals(53.563452f, bidRequest.getDevice().getGeo().getLat(), 0);
		Assert.assertEquals(9.925742f, bidRequest.getDevice().getGeo().getLon(), 0);
		Assert.assertEquals("22761", bidRequest.getDevice().getGeo().getZip());

		// Impression
		Assert.assertEquals(1, bidRequest.getImp().size());
		Assert.assertNotNull(bidRequest.getImp().get(0));
		Assert.assertEquals("45", bidRequest.getImp().get(0).getId());
		// Impression -> Video
		Assert.assertEquals(400, bidRequest.getImp().get(0).getVideo().getH());
		Assert.assertEquals(600, bidRequest.getImp().get(0).getVideo().getW());
		Assert.assertEquals(100, bidRequest.getImp().get(0).getVideo().getMaxduration());
		Assert.assertEquals(30, bidRequest.getImp().get(0).getVideo().getMinduration());
		Assert.assertEquals(1, bidRequest.getImp().get(0).getVideo().getStartdelay());
		Assert.assertEquals(1, bidRequest.getImp().get(0).getVideo().getProtocols().size());
		Assert.assertEquals(2, bidRequest.getImp().get(0).getVideo().getProtocols().get(0).intValue());
		Assert.assertEquals(1, bidRequest.getImp().get(0).getVideo().getMimes().size());
		Assert.assertEquals("application/x-shockwave-flash", bidRequest.getImp().get(0).getVideo().getMimes().get(0));

		// User
		Assert.assertEquals("99", bidRequest.getUser().getId());
		Assert.assertEquals("HHcFrt-76Gh4aPl", bidRequest.getUser().getBuyeruid());
		Assert.assertEquals("M", bidRequest.getUser().getGender());
		Assert.assertEquals(1981, bidRequest.getUser().getYob());

		// Extension
		final ContactGroup cg = (ContactGroup) bidRequest.getExt();
		Assert.assertEquals("23", cg.getContacts().get(0).getAge());
		Assert.assertEquals("20-29", cg.getContacts().get(0).getAgevarianz());
		Assert.assertEquals("M", cg.getContacts().get(0).getGender());
	}

	@Test
	public void testBuilderException() {

	}

	@Test
	public void testValidator() throws IOException {
		final String jsonBidrequest = gson.toJson(bidRequest.getBuilder().build(), BidRequest.class);
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator(OpenRtbInputType.BID_REQUEST, OpenRtbVersion.V2_4);
		final ValidationResult result = validator.validate(jsonBidrequest);
		log.info(result.getResult());
		Assert.assertTrue(validator.isValid(jsonBidrequest));
	}
}
