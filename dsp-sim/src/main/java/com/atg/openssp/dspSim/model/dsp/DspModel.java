package com.atg.openssp.dspSim.model.dsp;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.dsp.filter.DspReturnFilter;
import com.atg.openssp.dspSim.model.dsp.filter.PassthroughFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import openrtb.bidrequest.model.*;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;
import openrtb.tables.ContentCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
    private final Properties properties = new Properties();
    private static BigDecimal priceOffset = new BigDecimal(0);
    private DspReturnFilter filter;

    public DspModel() throws ModelException {
        loadProperties();
        loadModel();
        loadFilter();
    }

    private void loadProperties() {
        File file = new File("dsp-sim-dsp.properties");
        if (file.exists()) {
            try {
                FileInputStream is = new FileInputStream("dsp-sim-dsp.properties");
                properties.load(is);
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream("dsp-sim-dsp.properties");
                if (is != null) {
                    properties.load(is);
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    private void loadFilter() throws ModelException {
        if (filter == null) {
            String filterClassName = this.getProperty("filter-class");
            if (filterClassName == null) {
                filter = new PassthroughFilter();
            } else {
                try {
                    Class<? extends DspReturnFilter> filterClass = (Class<? extends DspReturnFilter>) Class.forName(filterClassName);
                    Constructor<? extends DspReturnFilter> cc = filterClass.getConstructor(new Class[]{});
                    filter = cc.newInstance(new Object[]{});
                } catch (Exception e) {
                    throw new ModelException(e.getMessage());
                }
            }
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

    public BidResponse createBidResponse(String server, int port, BidRequest request) {
        BidResponse response = new BidResponse();
        response.setId(request.getId());
        response.setBidid(UUID.randomUUID().toString());

        for (Impression i : request.getImp()) {
            for (SimBidder sb : bList) {
                response.addSeatBid(fabricateSeatBid(server, port, request.getSite(), sb, i));
            }
        }
        return response;
    }

    private SeatBid fabricateSeatBid(String server, int port, Site site, SimBidder simBidder, Impression i) {
        SeatBid sb = new SeatBid();
        Bid b = new Bid();
        sb.getBid().add(b);
        b.setId(simBidder.getId());
        b.setImpid(i.getId());
        priceOffset = priceOffset.add(new BigDecimal(.01));
        BigDecimal d = new BigDecimal(simBidder.getPrice());
        d = d.add(priceOffset);
        b.setPrice(priceOffset.floatValue());
        b.setAdid(simBidder.getAdId());
        String nUrl = "http://"+server+":"+port+"/win?i="+simBidder.getId()+"&price=${AUCTION_PRICE}";
        b.setNurl(nUrl);
//        b.setNurl(simBidder.getNUrl());
        b.setAdm(simBidder.getAdm());
        b.setAdomain(simBidder.getAdomain());
        b.setIurl(simBidder.getIUrl());
        b.setCid(simBidder.getCId());
        b.setCrid(simBidder.getCrId());
        b.addAllCat(site.getCat());
        List<ContentCategory> c2  = site.getPagecat();
        List<ContentCategory> c3  = site.getSectioncat();
        if (site.getPublisher() != null) {
            for (ContentCategory c : site.getPublisher().getCat()) {
                if (!b.getCat().contains(c)) {
                    b.addCat(c);
                }
            }
        }

        if (i.hasVideo()) {
            Video video = i.getVideo();
            b.setH(video.getH());
            b.setW(video.getW());
        } else if (i.hasBanner()) {
            Banner banner = i.getBanner();
            b.setH(banner.getH());
            b.setW(banner.getW());
        }
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

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String v = getProperty(key);
        if (v == null) {
            return defaultValue;
        } else {
            return v;
        }
    }

    public String filterResult(BidResponse brsp) {
        return filter.filterResult(brsp);
    }

}
