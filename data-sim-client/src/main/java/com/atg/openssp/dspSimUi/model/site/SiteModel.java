package com.atg.openssp.dspSimUi.model.site;

import com.atg.openssp.dspSimUi.site.SiteServerHandler;
import com.atg.openssp.dspSimUi.model.BaseModel;
import com.atg.openssp.dspSimUi.model.ModelException;
import openrtb.bidrequest.model.Site;
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
public class SiteModel extends BaseModel {
    private static final Logger log = LoggerFactory.getLogger(SiteModel.class);
    private final ResourceBundle bundle;
    private final Properties props = new Properties();
    private DefaultListModel<Site> mSites = new DefaultListModel<Site>();
    private final SiteServerHandler serverHandler;

    public SiteModel() throws ModelException {
        bundle = ResourceBundle.getBundle("SiteDisplayTemplate");
        loadProperties();
        serverHandler = new SiteServerHandler(this);
    }

    private void loadProperties() {
        try {
            File file = new File("site-client.properties");
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

    private HashMap<String, Site> getSites() {
        HashMap<String, Site> map = new HashMap<String, Site>();
        for (int i = 0; i < mSites.size(); i++) {
            map.put(mSites.get(i).getId(), mSites.get(i));
        }
        return map;
    }

    public DefaultListModel<Site> getSiteModel() {
        return mSites;
    }

    public void start() {
        serverHandler.start();
    }

    public void handleList(List<Site> sites) {
        HashMap<String, Site> map = getSites();
        for (Site site : sites) {
            Site check = map.get(site.getId());
            if (check == null) {
                mSites.addElement(site);
            } else {
                map.remove(site.getId());

                check.setPublisher(site.getPublisher());
                check.setCat(site.getCat());
                check.setDomain(site.getDomain());
                check.setPagecat(site.getPagecat());
                check.setPage(site.getPage());
                check.setSectioncat(site.getSectioncat());
                check.setName(site.getName());
                check.setRef(site.getRef());
                check.setExt(site.getExt());
            }
        }
        // if any are left, remove them as extras
        for (Map.Entry<String, Site> tSite : map.entrySet()) {
            mSites.removeElement(tSite.getValue());
        }
    }

    public void sendAddCommand(Site sb) throws ModelException {
        serverHandler.sendAddCommand(sb);
    }

    public void sendUpdateCommand(Site sb) throws ModelException {
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
