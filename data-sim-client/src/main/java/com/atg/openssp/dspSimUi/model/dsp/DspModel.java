package com.atg.openssp.dspSimUi.model.dsp;

import com.atg.openssp.common.cache.ListCache;
import com.atg.openssp.dspSimUi.ServerHandler;
import com.atg.openssp.dspSimUi.model.BaseModel;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.client.ServerCommandType;
import com.atg.openssp.dspSimUi.view.dsp.SimBidderPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Brian Sorensen
 */
public class DspModel extends BaseModel {
    private static final Logger log = LoggerFactory.getLogger(DspModel.class);
    private final ResourceBundle bundle;
    private ArrayList<ModeChangeListener> modeChangeListeners = new ArrayList<ModeChangeListener>();
    private final Properties props = new Properties();
    private DefaultListModel<SimBidder> mBidders = new DefaultListModel<SimBidder>();
    private final ServerHandler serverHandler;

    public DspModel() throws ModelException {
        bundle = ResourceBundle.getBundle("DisplayTemplate");
        loadProperties();
        serverHandler = new ServerHandler(this);
    }

    private void loadProperties() {
        try {
            File file = new File("dsp-sim-client.properties");
            InputStream is;
            if (file.exists()) {
                is = new FileInputStream(file);
            } else {
                is = getClass().getClassLoader().getSystemResourceAsStream(file.getName());
            }
            props.load(is);
            is.close();
        } catch (IOException e) {
            log.warn("Could not load properties file.", e);
        }
    }

    public Properties getProperties()
    {
        return props;
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

    private void setBidders(HashMap<String, SimBidder>  mBidders) {
        this.mBidders.clear();
        for (Map.Entry<String, SimBidder> e : mBidders.entrySet()) {
            this.mBidders.addElement(e.getValue());
        }
    }

    public DefaultListModel<SimBidder> getBidderModel() {
        return mBidders;
    }

    public void start() {
        serverHandler.start();
    }

    public void handleList(List<SimBidder> bidders) {
        HashMap<String, SimBidder> map = getBidders();
        if (bidders != null) {
            for (SimBidder bidder : bidders) {
                SimBidder check = map.get(bidder.getId());
                if (check == null) {
                    mBidders.addElement(bidder);
                } else {
                    map.remove(bidder.getId());
                    check.setImpid(bidder.getImpid());
                    check.setPrice(bidder.getPrice());
                    check.setAdid(bidder.getAdid());
                    check.setNurl(bidder.getNurl());
                    check.setAdm(bidder.getAdm());
                    check.setAdomain(bidder.getAdomain());
                    check.setIurl(bidder.getIurl());
                    check.setCid(bidder.getCid());
                    check.setCrid(bidder.getCrid());
                    check.setCats(bidder.getCats());
                }
            }
            // if any are left, remove them as extras
            for (Map.Entry<String, SimBidder> tBidder : map.entrySet()) {
                mBidders.removeElement(tBidder.getValue());
            }
        }
    }

    public void sendUpdateCommand(SimBidder sb) throws ModelException {
        serverHandler.sendUpdateCommand(sb);
    }

    public void sendRemoveCommand(String id) throws ModelException {
        serverHandler.sendRemoveCommand(id);
    }

    public void sendShutdownCommand() throws ModelException {
        serverHandler.sendShutdownCommand();
    }

    public void sendRestartCommand() throws ModelException {
        serverHandler.sendRestartCommand();
    }

    public void sendNormalCommand() throws ModelException {
        serverHandler.sendNormalCommand();
    }

    public void sendReturnNoneCommand() throws ModelException {
        serverHandler.sendReturnNoneCommand();
    }

    public void send400Command() throws ModelException {
        serverHandler.send400Command();
    }

    public void send500Command() throws ModelException {
        serverHandler.send500Command();
    }

    public void addModeChangeListener(ModeChangeListener lis) {
        modeChangeListeners.add(lis);
    }

    public void handleMode(ServerCommandType mode) {
        for (ModeChangeListener lis : modeChangeListeners) {
            lis.updateMode(mode);
        }
    }

    public String getTemplateText(String tag) {
        return bundle.getString(tag);
    }
}
