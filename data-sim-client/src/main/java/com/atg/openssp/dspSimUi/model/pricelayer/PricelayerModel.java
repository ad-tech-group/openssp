package com.atg.openssp.dspSimUi.model.pricelayer;

import com.atg.openssp.dspSimUi.model.BaseModel;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.pricelayer.PricelayerServerHandler;
import openrtb.bidrequest.model.Pricelayer;
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
public class PricelayerModel extends BaseModel {
    private static final Logger log = LoggerFactory.getLogger(PricelayerModel.class);
    private final ResourceBundle bundle;
    private final Properties props = new Properties();
    private DefaultListModel<Pricelayer> mPricelayers = new DefaultListModel<Pricelayer>();
    private final PricelayerServerHandler serverHandler;

    public PricelayerModel() throws ModelException {
        bundle = ResourceBundle.getBundle("PricelayerDisplayTemplate");
        loadProperties();
        serverHandler = new PricelayerServerHandler(this);
    }

    private void loadProperties() {
        try {
            File file = new File("pricelayer-client.properties");
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

    public Properties getProperties() {
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

    private HashMap<String, Pricelayer> getPricelayers() {
        HashMap<String, Pricelayer> map = new HashMap<String, Pricelayer>();
        for (int i = 0; i < mPricelayers.size(); i++) {
            map.put(mPricelayers.get(i).getSiteid(), mPricelayers.get(i));
        }
        return map;
    }

    public DefaultListModel<Pricelayer> getPricelayerModel() {
        return mPricelayers;
    }

    public void start() {
        serverHandler.start();
    }

    public void handleList(List<Pricelayer> pricelayers) {
        if (pricelayers == null) {
            return;
        }
        HashMap<String, Pricelayer> map = getPricelayers();
        for (Pricelayer pricelayer : pricelayers) {
            Pricelayer check = map.get(pricelayer.getSiteid());
            if (check == null) {
                mPricelayers.addElement(pricelayer);
            } else {
                map.remove(pricelayer.getSiteid());

                //TODO: BKS
                /*
                check.setPublisher(pricelayer.getPublisher());
                check.setCat(pricelayer.getCat());
                check.setDomain(pricelayer.getDomain());
                check.setPagecat(pricelayer.getPagecat());
                check.setPage(pricelayer.getPage());
                check.setSectioncat(pricelayer.getSectioncat());
                check.setName(pricelayer.getName());
                check.setRef(pricelayer.getRef());
                check.setExt(pricelayer.getExt());
                */
            }
        }
        // if any are left, remove them as extras
        for (Map.Entry<String, Pricelayer> tPricelayer : map.entrySet()) {
            mPricelayers.removeElement(tPricelayer.getValue());
        }
    }

    public void sendAddCommand(Pricelayer sb) throws ModelException {
        serverHandler.sendAddCommand(sb);
    }

    public void sendUpdateCommand(Pricelayer sb) throws ModelException {
        serverHandler.sendUpdateCommand(sb);
    }

    public void sendRemoveCommand(String id) throws ModelException {
        serverHandler.sendRemoveCommand(id);
    }

    public void sendLoadCommand() throws ModelException {
        serverHandler.sendLoadCommand();
    }

    public void sendExportCommand() throws ModelException {
        serverHandler.sendExportCommand();
    }

    public void sendImportCommand() throws ModelException {
        serverHandler.sendImportCommand();
    }

    public void sendClearCommand() throws ModelException {
        serverHandler.sendClearCommand();
    }

    public String getTemplateText(String tag) {
        return bundle.getString(tag);
    }
}
