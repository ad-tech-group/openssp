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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DspModel {
    private static final Logger log = LoggerFactory.getLogger(DspModel.class);
    private static final String FILE_NAME = "DSP_SIM_MODEL.json";
    private final ArrayList<SimBidderListener> simBidderListeners = new ArrayList();
    private final ArrayList<SimBidder> bidders = new ArrayList();

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
                bidders.addAll(buffer);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            throw new ModelException("Could not load model from store.");
        }
    }

    public void saveModel() throws ModelException {
        try {
            PrintWriter fw = new PrintWriter(new FileWriter(FILE_NAME));
            String json = new Gson().toJson(bidders);
            fw.println(json);
            fw.close();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new ModelException("Could not save model from store.");
        }
    }

    public synchronized void add(SimBidder sb) {
        bidders.add(sb);
        notifySimBidderAdded(sb);
    }

    private void notifySimBidderAdded(SimBidder sb) {
        for (SimBidderListener lis : simBidderListeners) {
            lis.added(sb);
        }
    }

    public BidResponse createBidResponse(BidRequest request) {
        BidResponse response = new BidResponse();
        response.setId(UUID.randomUUID().toString());
        response.setBidid(request.getId());

        for (Impression i : request.getImp()) {
            for (SimBidder sb : bidders) {
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
        for (SimBidder sb : bidders) {
            lis.added(sb);
        }
    }

    public void removeSimBidderListener(SimBidderListener lis) {
        simBidderListeners.remove(lis);
    }

}
