package com.atg.openssp.dspSim.model.dsp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import openrtb.tables.ContentCategory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimBidderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void gson() {
//        JsonArray sbA = new JsonArray();
        JsonObject sb = new JsonObject();
        //sbA.add(sb);

        sb.addProperty("id", "id1");
        sb.addProperty("price", 1.4f);
//        sb.addProperty("adid", "adid1");
        sb.addProperty("adm", "adm1");
        JsonArray aDomain = new JsonArray();
        sb.add("adomain", aDomain);
        aDomain.add("adomain1");
        sb.addProperty("iurl", "iurl1");
        sb.addProperty("cid", "cid1");
        sb.addProperty("crid", "crid1");
        JsonArray aCat = new JsonArray();
        sb.add("cat", aCat);
        aCat.add(ContentCategory.IAB2.getValue());

        Gson gson = new Gson();
        String sbJson = gson.toJson(sb);
        System.out.println(sbJson);
        SimBidder t = gson.fromJson(sbJson, SimBidder.class);
        assertNotNull(t);
        String tJson = gson.toJson(t);
        System.out.println(tJson);
        assertEquals(sbJson, tJson);

    }
}