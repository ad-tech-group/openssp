package openrtb.bidresponse.model;

import com.google.gson.Gson;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

import static org.junit.Assert.*;

/**
 *
 * @author André Schmer
 *
 */
public class BidTest {

    private static Bid bid;

    @BeforeClass
    public static void setUp() {
        bid = new Bid();
    }

    @Test
    public void testGSON() {
        Gson gson = new Gson();

        String str = "          {\"id\": \"QRh2T-YNIFk_0\",\n" +
                "          \"impid\": \"1\",\n" +
                "          \"price\": 0.01,\n" +
                "          \"adid\": \"823011\",\n" +
                "          \"nurl\": \"http://rtb.adkernel.com/win?i=QRh2T-YNIFk_0&price=${AUCTION_PRICE}\",\n" +
                "          \"adm\": \"<a href=\\\"http://rtb.adkernel.com/click?i=QRh2T-YNIFk_0\\\" target=\\\"_blank\\\"><img src=\\\"http://rtb.adkernel.com/n1/ad/300x250_EUNqbCsW.png\\\" width=\\\"300\\\" height=\\\"250\\\" border=\\\"0\\\" ></a><img src='http://rtb.adkernel.com/pixel?i=QRh2T-YNIFk_0' alt=' ' style='display:none'>\",\n" +
                "          \"adomain\": [\n" +
                "            \"adkernel.com\"\n" +
                "          ],\n" +
                "          \"iurl\": \"http://xs.wowconversions.com/n1/ad/300x250_EUNqbCsW.png\",\n" +
                "          \"cid\": \"28734\",\n" +
                "          \"crid\": \"823011\",\n" +
                "          \"cat\": [\n" +
                "            \"IAB3-1\"\n" +
                "          ]}\n";
        Bid tBid =  gson.fromJson(str, Bid.class);
        assertNotNull(tBid);
        assertNotNull(tBid.getCat());
        assertNotNull(tBid.getCatList());
        assertEquals(tBid.getCat().size(), 1);

        str = "{\n" +
                "  \"id\": \"00abc1fb-f81b-4d12-9b53-7b5bc7f05f3d\",\n" +
                "  \"seatbid\": [\n" +
                "    {\n" +
                "      \"bid\": [\n" +
                "        {\n" +
                "          \"id\": \"eb62bcab-df1a-4121-9336-c1e17d7d7cd2\",\n" +
                "          \"impid\": \"9ec96b97e89c5d\",\n" +
                "          \"price\": 0.01,\n" +
                "          \"adid\": \"823011\",\n" +
                "          \"nurl\": \"http://rtb.adkernel.com/win?i\\u003dQRh2T-YNIFk_0\\u0026price\\u003d${AUCTION_PRICE}\",\n" +
                "          \"adm\": \"\\u003ca href\\u003d\\\"http://rtb.adkernel.com/click?i\\u003dQRh2T-YNIFk_0\\\" target\\u003d\\\"_blank\\\"\\u003e\\u003cimg src\\u003d\\\"http://rtb.adkernel.com/n1/ad/300x250_EUNqbCsW.png\\\" width\\u003d\\\"300\\\" height\\u003d\\\"250\\\" border\\u003d\\\"0\\\" \\u003e\\u003c/a\\u003e\\u003cimg src\\u003d\\u0027http://rtb.adkernel.com/pixel?i\\u003dQRh2T-YNIFk_0\\u0027 alt\\u003d\\u0027 \\u0027 style\\u003d\\u0027display:none\\u0027\\u003e\",\n" +
                "          \"adomain\": [\n" +
                "            \"adkernel.com\"\n" +
                "          ],\n" +
                "          \"iurl\": \"http://xs.wowconversions.com/n1/ad/300x250_EUNqbCsW.png\",\n" +
                "          \"cid\": \"28734\",\n" +
                "          \"crid\": \"823011\",\n" +
                "          \"attr\": [],\n" +
                "          \"api\": 0,\n" +
                "          \"protocol\": 0,\n" +
                "          \"h\": 0,\n" +
                "          \"w\": 0,\n" +
                "          \"cat\": [\n" +
                "            \"IAB3-1\"\n" +
                "          ]\n" +
                "        }\n" +
                "      ],\n" +
                "      \"group\": 0\n" +
                "    }\n" +
                "  ],\n" +
                "  \"bidid\": \"AGDXnsOKXwphYAZI\",\n" +
                "  \"cur\": \"USD\",\n" +
                "  \"nbr\": -1\n" +
                "}";

        BidResponse tBidResponse =  gson.fromJson(str, BidResponse.class);
        assertNotNull(tBidResponse);
        assertNotNull(tBidResponse.getSeatbid().get(0).getBid().get(0).getCat());
        assertNotNull(tBidResponse.getSeatbid().get(0).getBid().get(0).getCatList());
        assertEquals(tBidResponse.getSeatbid().get(0).getBid().get(0).getCat().size(), 1);
        System.out.println(gson.toJson(tBidResponse));

//        StringBuilder sb = new StringBuilder("{\"price\":0.0,\"adomain\":[],\"attr\":[],\"api\":0,\"protocol\":0,\"h\":0,\"w\":0,\"cat\":[]}");
//        assertEquals(gson.toJson(bid), sb.toString());
    }

}
