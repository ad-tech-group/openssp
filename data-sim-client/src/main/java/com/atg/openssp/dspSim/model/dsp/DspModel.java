package com.atg.openssp.dspSim.model.dsp;

import com.atg.openssp.dspSim.ServerHandler;
import com.atg.openssp.dspSim.model.BaseModel;
import com.atg.openssp.dspSim.model.ModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.*;

/**
 * @author Brian Sorensen
 */
public class DspModel extends BaseModel {
    private static final Logger log = LoggerFactory.getLogger(DspModel.class);
    private final Properties props;
    private DefaultListModel<SimBidder> mBidders = new DefaultListModel();
    private final ServerHandler serverHandler;

    public DspModel(Properties props) throws ModelException {
        this.props = props;
        serverHandler = new ServerHandler(this);
    }

    public String lookupProperty(String key) {
        return props.getProperty(key);
    }

    public String lookupProperty(String key, String defaultValue) {
        String v = lookupProperty(key);
        if (v == null) {
            return defaultValue;
        } else {
            return v;
        }
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
