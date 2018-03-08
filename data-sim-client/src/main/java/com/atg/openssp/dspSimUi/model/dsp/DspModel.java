package com.atg.openssp.dspSimUi.model.dsp;

import com.atg.openssp.dspSimUi.ServerHandler;
import com.atg.openssp.dspSimUi.model.BaseModel;
import com.atg.openssp.dspSimUi.model.ModelException;
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
    private DefaultListModel<SimBidder> mBidders = new DefaultListModel<SimBidder>();
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
        HashMap<String, SimBidder> map = new HashMap<String, SimBidder>();
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
                check.setImpId(bidder.getImpId());
                check.setPrice(bidder.getPrice());
                check.setAdId(bidder.getAdId());
                check.setNUrl(bidder.getNUrl());
                check.setAdm(bidder.getAdm());
                check.setAdomain(bidder.getAdomain());
                check.setIUrl(bidder.getIUrl());
                check.setCId(bidder.getCId());
                check.setCrId(bidder.getCrId());
                check.setCat(bidder.getCat());
            }
        }
        // if any are left, remove them as extras
        for (Map.Entry<String, SimBidder> tBidder : map.entrySet()) {
            mBidders.removeElement(tBidder.getValue());
        }
    }

    public void sendAddCommand(SimBidder sb) throws ModelException {
        serverHandler.sendAddCommand(sb);
    }

    public void sendUpdateCommand(SimBidder sb) throws ModelException {
        serverHandler.sendUpdateCommand(sb);
    }

    public void sendRemoveCommand(String id) throws ModelException {
        serverHandler.sendRemoveCommand(id);
    }

}
