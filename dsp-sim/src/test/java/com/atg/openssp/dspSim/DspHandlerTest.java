package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.atg.openssp.unittest.UnitTestHttpExchange;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import static org.junit.Assert.*;

public class DspHandlerTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void holder() {

    }

    //@org.junit.Test
    public void handle() {
        try {
            DspModel m = new DspModel();
            DspHandler h = new DspHandler(m);
            StringBuilder body = new StringBuilder();
            body.append("{\n" +
                    "  'id': '4487159888663217854',\n" +
                    "  'imp': [\n" +
                    "    {\n" +
                    "      'id': '1',\n" +
                    "      'banner': {\n" +
                    "        'w': '300',\n" +
                    "        'h': '250'\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  'site': {\n" +
                    "    'page': 'http://test.com/page1?param=value'\n" +
                    "  },\n" +
                    "  'device': {\n" +
                    "    'ua': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/537.13 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2',\n" +
                    "    'ip': '1.1.1.1'\n" +
                    "  }\n" +
                    "}");

            JsonArray rBidList = new JsonArray();
            JsonObject rBid = new JsonObject();
            rBidList.add(rBid);
            rBid.addProperty("id", "QRh2T-YNIFk_0");
            rBid.addProperty("impid", "1");
            rBid.addProperty("price", 0.01);
            rBid.addProperty("adid", "adid1");
            rBid.addProperty("nurl", "http://friendly.com:20/win?i=QRh2T-YNIFk_0&price=${AUCTION_PRICE}");
            rBid.addProperty("adm", "<a href=\'http://rtb.adkernel.com/click?i=QRh2T-YNIFk_0\' target=\'_blank\'><img src=\'http://rtb.adkernel.com/n1/ad/300x250_EUNqbCsW.png\' width=\'300\' height=\'250\' border=\'0\' ></a><img src='http://rtb.adkernel.com/pixel?i=QRh2T-YNIFk_0' alt=' ' style='display:none'>");

            JsonArray rAddDomain = new JsonArray();
            rBid.add("adomain", rAddDomain);
            rAddDomain.add("adkernel.com");

            JsonArray rAttr = new JsonArray();
            rBid.add("attr", rAttr);

//            rBid.addProperty("iurl", "http://xs.wowconversions.com/n1/ad/300x250_EUNqbCsW.png");
//            rBid.addProperty("cid", "28734");
//            rBid.addProperty("crid", "823011");
            rBid.addProperty("api", 0);
            rBid.addProperty("protocol", 0);
            rBid.addProperty("w", 300);
            rBid.addProperty("h", 250);

            JsonArray rCat = new JsonArray();
            rBid.add("cat", rCat);
//            rCat.add("IAB3-1");

            JsonArray rSeatBidList = new JsonArray();
            JsonObject rSeatBid = new JsonObject();
            rSeatBidList.add(rSeatBid);
            rSeatBid.add("bid", rBidList);
            rSeatBid.addProperty("group", 0);

            //<...3217854","seatbid":[[{"bid":[{"id":"QRh2T-YNIFk_0","impid":"1","price":0.01,"adid":"adid1","nurl":"http://friendly.com:20/win?i=QRh2T-YNIFk_0&price=${AUCTION_PRICE}","adm":"<a href='http://rtb.adkernel.com/click?i=QRh2T-YNIFk_0' target='_blank'><img src='http://rtb.adkernel.com/n1/ad/300x250_EUNqbCsW.png' width='300' height='250' border='0' ></a><img src='http://rtb.adkernel.com/pixel?i=QRh2T-YNIFk_0' alt=' ' style='display:none'>","adomain":["adkernel.com"],"attr":[],"api":0,"protocol":0,"w":300,"h":250,"cat":[]}],"group":0}],"bidid":"-zap-"]}>
            //<...3217854","seatbid":[[],"bidid":"-zap-","cur":"USD","nbr":-1]}>
            JsonObject result = new JsonObject();
            result.addProperty("id", "4487159888663217854");
            result.add("seatbid", rSeatBidList);
            result.addProperty("bidid", "-zap-");
//            result.addProperty("cur", "USD");
//            result.addProperty("nbr", -1);

            UnitTestHttpExchange e = new UnitTestHttpExchange(body.toString());
            h.handle(e);

            assertEquals(e.getTestStatus(), 200);
            long testLen = e.getTestLength();

            JsonParser parser = new JsonParser();
            JsonObject testNode = parser.parse(e.getTestResult()).getAsJsonObject();
            testNode.addProperty("bidid", "-zap-");

            assertEquals(result.toString(), testNode.toString());

        } catch (ModelException e) {
            e.printStackTrace();
            fail("problem");
        } catch (IOException e) {
            e.printStackTrace();
            fail("problem");
        }
    }
}