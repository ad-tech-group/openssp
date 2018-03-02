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

public class DspModel {
    private static final Logger log = LoggerFactory.getLogger(DspModel.class);
    private static final String FILE_NAME = "DSP_SIM_MODEL.json";
    private final ArrayList<SimBidderListener> simBidderListeners = new ArrayList();
    private final ArrayList<SimBidder> bList = new ArrayList();
    private final HashMap<String, SimBidder> bMap = new LinkedHashMap();

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
                response.addSeatBid(fabricateSeatBid(sb, i));
            }
        }
        return response;
    }

    private SeatBid fabricateSeatBid(SimBidder simBidder, Impression i) {
        SeatBid sb = new SeatBid();
        Bid b = new Bid();
        b.setId(simBidder.getId());
        b.setImpid(i.getId());
        b.setPrice(simBidder.getPrice());
        sb.getBid().add(b);
        return sb;
    }

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
