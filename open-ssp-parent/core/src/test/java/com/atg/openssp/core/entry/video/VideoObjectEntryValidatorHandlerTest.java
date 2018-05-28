package com.atg.openssp.core.entry.video;

import com.atg.openssp.common.cache.dto.VideoAd;
import com.atg.openssp.common.core.cache.type.SiteDataCache;
import com.atg.openssp.common.core.cache.type.VideoAdDataCache;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.demand.VideoObjectParamValue;
import com.atg.openssp.common.exception.RequestException;
import openrtb.bidrequest.model.Banner;
import openrtb.bidrequest.model.Site;
import openrtb.tables.CreativeAttribute;
import openrtb.tables.ApiFramework;
import openrtb.tables.VideoBidResponseProtocol;
import openrtb.tables.VideoLinearity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class VideoObjectEntryValidatorHandlerTest {

    @BeforeClass
    public static void init()
    {
        Site s = new Site();
        s.setId("1");
        s.setPage("http://site1.com/page.html");
        s.setDomain("site1.com");
        SiteDataCache.instance.put("1", s);
        SiteDataCache.instance.switchCache();

        VideoAd vid = new VideoAd();
        vid.setId("v-id");
        vid.setVidId("123");
        vid.setBidfloorPrice(13.13f);
        vid.setBidfloorCurrency("USD");
        vid.setW(13);
        vid.setH(14);
        vid.setMinDuration(15);
        vid.setMaxDuration(16);
        vid.setStartDelay(12);
        List<String> mimes = new ArrayList<>();
        mimes.add("mime1");
        vid.setMimes(mimes);
        List<VideoBidResponseProtocol> protocols = new ArrayList<>();
        protocols.add(VideoBidResponseProtocol.VAST_2_0_WRAPPER);
        vid.setProtocols(protocols);
        List<CreativeAttribute> battr = new ArrayList<>();
        battr.add(CreativeAttribute.EXPANDABLE_USER_INITIATED_CLICK);
        vid.setBattr(battr);
        vid.setLinearity(VideoLinearity.NON_LINEAR);
        List<ApiFramework> api = new ArrayList<>();
        api.add(ApiFramework.MRAID_1);
        vid.setApi(api);
        VideoAdDataCache.instance.put("123", vid);
        VideoAdDataCache.instance.switchCache();
    }

    @Test
    public void processValidationFromPost() {

        JSONObject obj = new JSONObject();
        obj.put("id", "i-id");
        obj.put("site", "1");
//        obj.put("app", "app");
        obj.put("page", "page");
        obj.put("hash", "hash");
        obj.put("loc", "loc");
        obj.put("uid", "uid");
        obj.put("sid", "sid");

        JSONArray units = new JSONArray();
        obj.put("adUnitsToBidUpon", units);
        JSONObject vid = new JSONObject();
        vid.put("id", "v-id");
        vid.put("videoad_id", "123");
        vid.put("bidfloor_price", 13.13f);
        vid.put("bidfloor_currency", "USD");
        vid.put("w", 13);
        vid.put("h", 14);
        vid.put("min_duration", 15);
        vid.put("max_duration", 16);
        vid.put("start_delay", 12);
        vid.put("linearity", 2);
        JSONArray mimes = new JSONArray();
        vid.put("mimes", mimes);
        mimes.add(0, "mime1");
        JSONArray protocols = new JSONArray();
        vid.put("protocols", protocols);
        protocols.add(0, 5);
        JSONArray battr = new JSONArray();
        vid.put("battr", battr);
        battr.add(0, 4);
        JSONArray api = new JSONArray();
        vid.put("api", api);
        api.add(0, 3);

        units.add(vid);

        /*

	// required
    @Until(2.2)
	private int protocol;

	private List<Banner> companionad;

	private Object ext;

         */

        //sb.append("}");
        String ut = obj.toJSONString();
        ByteArrayInputStream is = new ByteArrayInputStream(ut.getBytes());
        VideoObjectEntryValidatorHandler e = new VideoObjectEntryValidatorHandler();
        final List<ParamValue> pmList = new ArrayList<>();
        final int contentLength = ut.length();
        final String referrer = "bmc.org";
        final String ipAddress = "21.11.22.12";
        final String userAgent = "ua stuff";
        try {
            e.processValidationFromPost(is, pmList, contentLength, referrer, ipAddress, userAgent);
            verify(pmList);
        } catch (RequestException e1) {
            e1.printStackTrace();
            fail("problem");
        }
        finally {
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void verify(List<ParamValue> pmList) {
        assertEquals(1, pmList.size());
        VideoObjectParamValue pv = (VideoObjectParamValue) pmList.get(0);
        assertEquals("1", pv.getSite().getId());
        assertEquals("http://site1.com/page.html", pv.getSite().getPage());
        assertEquals("bmc.org", pv.getSite().getRef());
        assertEquals(null, pv.getApp());
        assertEquals("i-id", pv.getRequestId());
        assertEquals("uid", pv.getUid());
        assertEquals("21.11.22.12", pv.getIpAddress());
        assertEquals("ua stuff", pv.getBrowserUserAgentString());
        assertEquals("0", pv.getPsa());
        assertEquals("hash", pv.getHash());
        assertEquals("sid", pv.getSid());
        assertEquals("loc", pv.getLoc());
        assertEquals(null, pv.getReferrer());
        assertEquals(null, pv.getOverrideBidFloor());

        VideoAd va = pv.getVideoad();
        assertNotNull(va);

        assertEquals("v-id", va.getId());
        assertEquals("USD", va.getBidfloorCurrency());
        assertEquals(13.13, va.getBidfloorPrice(), 0.001);
        List<String> mimes2 = va.getMimes();
        assertEquals(1, mimes2.size());
        assertEquals("[mime1]", mimes2.toString());

        assertEquals(new Integer(13), va.getW());
        assertEquals(new Integer(14), va.getH());
        assertEquals(15, va.getMinDuration());
        assertEquals(16, va.getMaxDuration());
        assertEquals(new Integer(12), va.getStartDelay());
        List<VideoBidResponseProtocol> protocols2 = va.getProtocols();
        assertEquals(1, protocols2.size());
        assertEquals("["+VideoBidResponseProtocol.VAST_2_0_WRAPPER+"]", protocols2.toString());
        List<CreativeAttribute> battr2 = va.getBattr();
        assertEquals(1, battr2.size());
        assertEquals("["+CreativeAttribute.EXPANDABLE_USER_INITIATED_CLICK+"]", battr2.toString());

        assertEquals(VideoLinearity.NON_LINEAR, va.getLinearity());
        List<Banner> banners = va.getCompanionad();
        assertNull(banners);
        List<ApiFramework> api2 = va.getApi();
        assertEquals(1, api2.size(), 1);
        assertEquals("["+ ApiFramework.MRAID_1+"]", api2.toString());
        assertEquals(null, va.getExt());

        assertEquals("123", va.getVidId());

    }

    @Test
    public void processValidationFromGet() {
        VideoObjectEntryValidatorHandler e = new VideoObjectEntryValidatorHandler();
        Map<String, String> params = new HashMap<>();
        params.put("videoad_id", "123");
        params.put("site", "1");
        params.put("id", "i-id");
        params.put("page", "page");
        params.put("hash", "hash");
        params.put("loc", "loc");
        params.put("uid", "uid");
        params.put("sid", "sid");

        final List<ParamValue> pmList = new ArrayList<>();
        final String referrer = "bmc.org";
        final String ipAddress = "21.11.22.12";
        final String userAgent = "ua stuff";
        try {
            e.processValidationFromGet(params, pmList, referrer, ipAddress, userAgent);
            verify(pmList);

        } catch (RequestException e1) {
            e1.printStackTrace();
            fail("problem");
        }
    }

}