package com.atg.openssp.dspSim.model.dsp;

import com.google.gson.Gson;
import openrtb.bidrequest.model.BidRequest;
import openrtb.bidrequest.model.Impression;
import openrtb.bidresponse.model.Bid;
import openrtb.bidresponse.model.BidResponse;
import openrtb.bidresponse.model.SeatBid;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class DspModel {
    private static final String FILE_NAME = "DSP_SIM_MODEL.json";
    private final ArrayList<SimBidderListener> simBidderListeners = new ArrayList();
    private final ArrayList<SimBidder> bidders = new ArrayList();

    public DspModel() {
        loadModel();
    }

    private void loadModel() {
        try {
            File f = new File(FILE_NAME);
            if (f.exists()) {
                FileReader fr = new FileReader(f);
                ArrayList buffer = new Gson().fromJson(fr, ArrayList.class);
                fr.close();
                System.out.println(buffer.get(0));
//                bidders.addAll(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimBidder sb = new SimBidder("331a77c4-dcdd-4d8f-8138-5d049163c2ea");
        sb.setPrice(1f);
        add(sb);
        sb = new SimBidder("9a038482-c91e-4369-b85d-6e75d9e09c19");
        sb.setPrice(0.3f);
        add(sb);
    }

    public void saveModel() {
        try {
            PrintWriter fw = new PrintWriter(new FileWriter(FILE_NAME));
            String json = new Gson().toJson(bidders);
            fw.println(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
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
