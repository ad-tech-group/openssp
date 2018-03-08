package com.atg.openssp.dspSim.model.dsp;

import com.atg.openssp.dspSim.model.ModelException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.Impression;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author Brian Sorensen
 */
public class DspModel {
    private static final Logger log = LoggerFactory.getLogger(DspModel.class);
    private static final String FILE_NAME = "DSP_SIM_MODEL.json";
    private final ArrayList<SimBidderListener> simBidderListeners = new ArrayList<SimBidderListener>();
    private final ArrayList<SimBidder> bList = new ArrayList<SimBidder>();
    private final HashMap<String, SimBidder> bMap = new LinkedHashMap<String, SimBidder>();

    public DspModel() throws ModelException {
        loadModel();
    }

    private void loadModel() throws ModelException {
        try {
            File f = new File(FILE_NAME);
            if (f.exists()) {
                FileReader fr = new FileReader(f);
                List<SimBidder> buffer = new Gson().fromJson(fr, new TypeToken<List<SimBidder>>(){}.getType());
                fr.close();
                bList.addAll(buffer);
                for (SimBidder sb : bList) {
                    bMap.put(sb.getId(), sb);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            throw new ModelException("Could not load model from store.");
        }
    }

    public void saveModel() throws ModelException {
        try {
            PrintWriter fw = new PrintWriter(new FileWriter(FILE_NAME));
            String json = new Gson().toJson(bList);
            fw.println(json);
            fw.close();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new ModelException("Could not save model from store.");
        }
    }

    public synchronized void add(SimBidder sb) {
        bList.add(sb);
        bMap.put(sb.getId(), sb);
        notifySimBidderAdded(sb);
    }

    private void notifySimBidderAdded(SimBidder sb) {
        for (SimBidderListener lis : simBidderListeners) {
            lis.added(sb);
        }
    }

    public synchronized void remove(SimBidder sb) {
        bList.remove(sb);
        bMap.remove(sb.getId());
        notifySimBidderRemoved(sb);
    }

    private void notifySimBidderRemoved(SimBidder sb) {
        for (SimBidderListener lis : simBidderListeners) {
            lis.removed(sb);
        }
    }

    public BidResponse createBidResponse(BidRequest request) {
        BidResponse response = new BidResponse();
        response.setId(UUID.randomUUID().toString());
        response.setBidid(request.getId());

        for (Impression i : request.getImp()) {
            for (SimBidder sb : bList) {
                response.addSeatBid(fabricateSeatBid(request.getId(), sb, i));
            }
        }
        return response;
    }

    private SeatBid fabricateSeatBid(String requestId, SimBidder simBidder, Impression i) {
        SeatBid sb = new SeatBid();
        Bid b = new Bid();
        sb.getBid().add(b);
        b.setId(simBidder.getId());
        b.setImpid(i.getId());
        b.setPrice(simBidder.getPrice());
        b.setAdid(simBidder.getAdId());
        b.setNurl(simBidder.getNUrl());
        b.setAdm(simBidder.getAdm());
        b.setAdomain(simBidder.getAdomain());
        b.setIurl(simBidder.getIUrl());
        b.setCid(simBidder.getCId());
        b.setCrid(simBidder.getCrId());
        b.setCat(simBidder.getCat());
        return sb;
    }
        /*
{
  "id": "4487159888663217854",
  "imp": [
    {
      "id": "1",
      "banner": {
        "w": "300",
        "h": "250"
      }
    }
  ],
  "site": {
    "page": "http://test.com/page1?param=value"
  },
  "device": {
    "ua": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/537.13 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
    "ip": "1.1.1.1"
  }
}

{
  "id": "4487159888663217854",
  "seatbid": [
    {
      "bid": [
        {
          "id": "QRh2T-YNIFk_0",
          "impid": "1",
          "price": 0.01,
          "adid": "823011",
          "nurl": "http://rtb.adkernel.com/win?i=QRh2T-YNIFk_0&price=${AUCTION_PRICE}",
          "adm": "<a href=\"http://rtb.adkernel.com/click?i=QRh2T-YNIFk_0\" target=\"_blank\"><img src=\"http://rtb.adkernel.com/n1/ad/300x250_EUNqbCsW.png\" width=\"300\" height=\"250\" border=\"0\" ></a><img src='http://rtb.adkernel.com/pixel?i=QRh2T-YNIFk_0' alt=' ' style='display:none'>",
          "adomain": [
            "adkernel.com"
          ],
          "iurl": "http://xs.wowconversions.com/n1/ad/300x250_EUNqbCsW.png",
          "cid": "28734",
          "crid": "823011",
          "cat": [
            "IAB3-1"
          ]
        }
      ]
    }
  ]
}

         */

    public synchronized void addSimBidderListener(SimBidderListener lis) {
        simBidderListeners.add(lis);
        for (SimBidder sb : bList) {
            lis.added(sb);
        }
    }

    public void removeSimBidderListener(SimBidderListener lis) {
        simBidderListeners.remove(lis);
    }

    public List<SimBidder> getBidders() {
        return bList;
    }

    public SimBidder lookupBidder(String id) {
        return bMap.get(id);
    }

}
