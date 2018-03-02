package com.atg.openssp.dspSim.model.dsp;

import com.atg.openssp.dspSim.ServerHandler;
import com.atg.openssp.dspSim.model.ModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DspModel {
    private static final Logger log = LoggerFactory.getLogger(DspModel.class);
    private DefaultListModel<SimBidder> mBidders = new DefaultListModel();
    private final ServerHandler serverHandler;

    public DspModel() throws ModelException {
        serverHandler = new ServerHandler(this);
    }

    private HashMap<String, SimBidder> getBidders() {
        HashMap<String, SimBidder> map = new HashMap();
        for (int i=0; i<mBidders.size(); i++) {
            map.put(mBidders.get(i).getId(), mBidders.get(i));
        }
        return map;
    }

    public DefaultListModel<SimBidder> getBidderModel() {
        return mBidders;
    }

    public void start() {
        serverHandler.start();
    }

    public void handleList(List<SimBidder> bidders) {
        HashMap<String, SimBidder> map = getBidders();
        for (SimBidder bidder : bidders) {
            SimBidder check = map.get(bidder.getId());
            if (check == null) {
                mBidders.addElement(bidder);
            } else {
                map.remove(bidder.getId());
                check.setPrice(bidder.getPrice());
            }
        }
        // if any are left, remove them as extras
        for (Map.Entry<String, SimBidder> tBidder : map.entrySet()) {
            mBidders.removeElement(tBidder.getValue());
        }
    }

    public void sendAddCommand(float price) throws ModelException {
        SimBidder sb = new SimBidder(UUID.randomUUID().toString());
        sb.setPrice(price);
        serverHandler.sendAddCommand(sb);
    }

    public void sendUpdateCommand(SimBidder sb, float newPrice) throws ModelException {
        serverHandler.sendUpdateCommand(sb, newPrice);
    }

    public void sendRemoveCommand(SimBidder sb) throws ModelException {
        serverHandler.sendRemoveCommand(sb);
    }
}
