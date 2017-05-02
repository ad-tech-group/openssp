package agent;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.atg.openssp.common.dto.VideoAd;
import com.atg.openssp.common.dto.Website;
import com.atg.openssp.common.dto.Zone;
import com.atg.openssp.core.cache.type.VideoAdDataCache;
import com.atg.openssp.core.cache.type.WebsiteDataCache;
import com.atg.openssp.core.cache.type.ZoneDataCache;
import com.atg.openssp.core.exchange.RequestSessionAgent;
import com.atg.openssp.core.system.MetricFactory;

import common.RequestException;
import junit.framework.Assert;

/**
 * 
 * @author André Schmer
 *
 */
public class SessionAgentParamsTest {

	private static final int zoneid = 141;
	private static final int adid = 4398;
	private static final int websiteid = 543;

	@BeforeClass
	public static void setUp() {
		final Zone zone = new Zone();
		zone.setZoneId(zoneid);
		zone.setWebsiteId(websiteid);

		ZoneDataCache.instance.put(zoneid, zone);
		ZoneDataCache.instance.switchCache();

		final VideoAd videoad = new VideoAd();
		videoad.setVideoadId(adid);

		VideoAdDataCache.instance.put(adid, videoad);
		VideoAdDataCache.instance.switchCache();

		final Website website = new Website();
		website.setWebsiteId(websiteid);
		final Zone[] zones = { zone };
		website.setZones(zones);

		WebsiteDataCache.instance.put(websiteid, website);
		WebsiteDataCache.instance.switchCache();

		MetricFactory.instance.initMetrics();
	}

	/**
	 * ?zone={zone_id}&pub={publisher_id}&prot={protocol}&h={height}&w={width}&sd={startdelay}&mime={mime_type}&domain={domain}&page={page}&ad={adid}
	 */

	@Test
	public void testParamsComplete() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();

		request.addParameter("zone", String.valueOf(zoneid));
		request.addParameter("w", "1024");
		request.addParameter("h", "768");
		request.addParameter("sd", "1");
		request.addParameter("pub", "5");
		request.addParameter("prot", "3");
		request.addParameter("mimes", "flash");
		request.addParameter("domain", "atg.com");
		request.addParameter("page", "contact.html");
		request.addParameter("ad", String.valueOf(adid));

		RequestSessionAgent agent = null;
		try {
			agent = new RequestSessionAgent(request, response);
		} catch (final RequestException e) {
			Assert.fail(e.getMessage());
		}

		Assert.assertEquals(zoneid, agent.getParamValues().getZone().getZoneId());
		Assert.assertEquals(websiteid, agent.getParamValues().getZone().getWebsiteId());
		Assert.assertEquals(zoneid, agent.getParamValues().getWebsite().getZones()[0].getZoneId());
		Assert.assertEquals(adid, agent.getParamValues().getVideoad().getVideoadId());
		Assert.assertEquals(1, agent.getParamValues().getStartdelay());
		Assert.assertEquals("atg.com", agent.getParamValues().getDomain());
		Assert.assertEquals("768", agent.getParamValues().getH());
		Assert.assertEquals("1024", agent.getParamValues().getW());
		Assert.assertEquals("contact.html", agent.getParamValues().getPage());
		Assert.assertEquals("5", agent.getParamValues().getPublisherid());
		Assert.assertEquals("flash", agent.getParamValues().getMimes().get(0));
		Assert.assertEquals(new Integer(3), agent.getParamValues().getProtocols().get(0));
	}

	@Test
	public void testParamsAlternative() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();

		request.addParameter("zone", String.valueOf(zoneid));
		request.addParameter("w", "1024");
		request.addParameter("h", "768");
		request.addParameter("prot", "3");
		request.addParameter("domain", "atg.com");
		// request.addParameter("page", "contact.html");
		request.addParameter("ad", String.valueOf(adid));

		RequestSessionAgent agent = null;
		try {
			agent = new RequestSessionAgent(request, response);
		} catch (final RequestException e) {
			Assert.fail(e.getMessage());
		}

		Assert.assertEquals(zoneid, agent.getParamValues().getZone().getZoneId());
		Assert.assertEquals(websiteid, agent.getParamValues().getZone().getWebsiteId());
		Assert.assertEquals(zoneid, agent.getParamValues().getWebsite().getZones()[0].getZoneId());
		Assert.assertEquals(adid, agent.getParamValues().getVideoad().getVideoadId());
		Assert.assertEquals(0, agent.getParamValues().getStartdelay());
		Assert.assertEquals("atg.com", agent.getParamValues().getDomain());
		Assert.assertEquals("768", agent.getParamValues().getH());
		Assert.assertEquals("1024", agent.getParamValues().getW());
		// Assert.assertEquals("contact.html",
		// agent.getParamValues().getPage());
		Assert.assertEquals(String.valueOf(websiteid), agent.getParamValues().getPublisherid());
		Assert.assertEquals("video/mp4", agent.getParamValues().getMimes().get(0));
		Assert.assertEquals(new Integer(3), agent.getParamValues().getProtocols().get(0));
		Assert.assertEquals("atg.com", agent.getParamValues().getPage());
	}

	@Test(expected = RequestException.class)
	public void testParamsNoZone() throws RequestException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();

		request.addParameter("w", "1024");
		request.addParameter("h", "768");
		request.addParameter("sd", "1");
		request.addParameter("pub", "5");
		request.addParameter("prot", "3");
		request.addParameter("mimes", "flash");
		request.addParameter("domain", "atg.com");
		request.addParameter("page", "contact.html");
		request.addParameter("ad", String.valueOf(adid));

		new RequestSessionAgent(request, response);
		Assert.fail();
	}

	@Test(expected = RequestException.class)
	public void testParamsNoVideoad() throws RequestException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();

		request.addParameter("zone", String.valueOf(zoneid));
		request.addParameter("w", "1024");
		request.addParameter("h", "768");
		request.addParameter("sd", "1");
		request.addParameter("pub", "5");
		request.addParameter("prot", "3");
		request.addParameter("mimes", "flash");
		request.addParameter("domain", "atg.com");
		request.addParameter("page", "contact.html");

		new RequestSessionAgent(request, response);
		Assert.fail();
	}

}
