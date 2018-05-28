package com.atg.openssp.core.entry.banner;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.core.cache.type.SiteDataCache;
import com.atg.openssp.common.core.cache.type.BannerAdDataCache;
import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.demand.BannerObjectParamValue;
import com.atg.openssp.common.exception.RequestException;
import com.google.gson.annotations.SerializedName;
import openrtb.bidrequest.model.Site;
import openrtb.tables.ApiFramework;
import openrtb.tables.BannerAdType;
import openrtb.tables.CreativeAttribute;
import openrtb.tables.ExpandableDirectionType;
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

public class BannerObjectEntryValidatorHandlerTest {

    @BeforeClass
    public static void init()
    {
        Site s = new Site();
        s.setId("1");
        s.setPage("http://site1.com/page.html");
        s.setDomain("site1.com");
        SiteDataCache.instance.put("1", s);
        SiteDataCache.instance.switchCache();

        BannerAd ban = new BannerAd();
        ban.setId("b-id");
        ban.setPlacementId("123");
        ban.setBidfloorPrice(13.13f);
        ban.setBidfloorCurrency("USD");
        ban.setAdUnitCode("auc");
        ban.setPromoSizes("promo");
        ban.setSize("size");
        ban.setW(321);
        ban.setH(322);
        ban.addMime("mime1");
        ban.addBtype(BannerAdType.XHTML_TEXT_AD);
        ban.addBattr(CreativeAttribute.AUDIO_AD_AUTO_PLAY);
        ban.setTopframe(99);
        ban.addExpdir(ExpandableDirectionType.Right);
        ban.addApi(ApiFramework.MRAID_1);
        ban.setExt(null);
        ban.setWmax(32);
        ban.setWmin(31);
        ban.setHmax(34);
        ban.setHmin(33);

//        ban.setPsa("psa");

        BannerAdDataCache.instance.put("123", ban);
        BannerAdDataCache.instance.switchCache();
    }

    @Test
    public void processValidationFromPost() {

        JSONObject obj = new JSONObject();
        obj.put("id", "i-id");
        obj.put("site", "1");
//        obj.put("app", "app");
        obj.put("page", "/hb_page.html");
        obj.put("hash", "hash");
        obj.put("loc", "loc");
        obj.put("uid", "uid");
        obj.put("sid", "sid");

        JSONArray units = new JSONArray();
        obj.put("adUnitsToBidUpon", units);
        JSONObject ban = new JSONObject();
        ban.put("id", "b-id");
        ban.put("placement_id", "123");
        ban.put("bidfloor_price", 13.13f);
        ban.put("bidfloor_currency", "USD");

        ban.put("adUnitCode", "auc");
        ban.put("size", "size");
        ban.put("promo_sizes", "promo");

        ban.put("w", "321");
        ban.put("h", "322");

        JSONArray mimes = new JSONArray();
        ban.put("mimes", mimes);
        mimes.add(0, "mime1");

        JSONArray btypes = new JSONArray();
        ban.put("btypes", btypes);
        btypes.add(0, BannerAdType.XHTML_TEXT_AD);

        JSONArray battrs = new JSONArray();
        ban.put("battrs", battrs);
        battrs.add(0, CreativeAttribute.AUDIO_AD_AUTO_PLAY);

        ban.put("topframe", 99);

        JSONArray expdir = new JSONArray();
        ban.put("expdir", expdir);
        expdir.add(0, ExpandableDirectionType.Right);

        JSONArray api = new JSONArray();
        ban.put("api", api);
        api.add(0, "3");

        ban.put("ext", null);

        ban.put("wmax", 32);
        ban.put("wmin", 31);
        ban.put("hmax", 34);
        ban.put("hmin", 33);

//        ban.put("psa", "psa");

        units.add(ban);

        String ut = obj.toJSONString();
        ByteArrayInputStream is = new ByteArrayInputStream(ut.getBytes());
        BannerObjectEntryValidatorHandler e = new BannerObjectEntryValidatorHandler();
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
        BannerObjectParamValue pv = (BannerObjectParamValue) pmList.get(0);
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

        BannerAd va = pv.getBannerad();
        assertNotNull(va);

        assertEquals("b-id", va.getId());
        assertEquals("USD", va.getBidfloorCurrency());
        assertEquals(13.13, va.getBidfloorPrice(), 0.001);
        assertEquals("auc", va.getAdUnitCode());
        assertEquals("size", va.getSize());
        assertEquals("promo", va.getPromoSizes());
        assertEquals("123", va.getPlacementId());

        assertEquals(new Integer(321), va.getW());
        assertEquals(new Integer(322), va.getH());

        assertEquals("[mime1]", va.getMimes().toString());
        assertEquals("[XHTML_TEXT_AD]", va.getBtypes().toString());
        assertEquals("[AUDIO_AD_AUTO_PLAY]", va.getBattrs().toString());

        assertEquals(99, va.getTopframe());
        assertEquals("[Right]", va.getExpdir().toString());
        assertEquals("[MRAID_1]", va.getApi().toString());
        assertEquals(null, va.getExt());
        assertEquals(32, va.getWmax());
        assertEquals(31, va.getWmin());
        assertEquals(34, va.getHmax());
        assertEquals(33, va.getHmin());

//        assertEquals("psa", va.getPsa());
    }

    @Test
    public void processValidationFromGet() {
        BannerObjectEntryValidatorHandler e = new BannerObjectEntryValidatorHandler();
        Map<String, String> params = new HashMap<>();
        params.put("placement_id", "123");
        params.put("site", "1");
        params.put("id", "i-id");
        params.put("page", "/hb_page.html");
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