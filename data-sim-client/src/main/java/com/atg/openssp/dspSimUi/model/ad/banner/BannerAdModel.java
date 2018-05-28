package com.atg.openssp.dspSimUi.model.ad.banner;

import com.atg.openssp.common.cache.ListCache;
import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.dspSimUi.bannerAd.BannerAdServerHandler;
import com.atg.openssp.dspSimUi.model.BaseModel;
import com.atg.openssp.dspSimUi.model.ModelException;
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
public class BannerAdModel extends BaseModel {
    private static final Logger log = LoggerFactory.getLogger(BannerAdModel.class);
    private ArrayList<BannerListener> bannerListeners = new ArrayList<>();
    private final ResourceBundle bundle;
    private final Properties props = new Properties();
    private DefaultListModel<BannerAd> mBannerAds = new DefaultListModel<>();
    private final BannerAdServerHandler serverHandler;

    public BannerAdModel() throws ModelException {
        bundle = ResourceBundle.getBundle("BannerAdDisplayTemplate");
        loadProperties();
        serverHandler = new BannerAdServerHandler(this);
    }

    private void loadProperties() {
        try {
            File file = new File("banner-ad-client.properties");
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

    private HashMap<String, BannerAd> getBannerAds() {
        HashMap<String, BannerAd> map = new HashMap<String, BannerAd>();
        for (int i = 0; i < mBannerAds.size(); i++) {
            map.put(mBannerAds.get(i).getId(), mBannerAds.get(i));
        }
        return map;
    }

    public DefaultListModel<BannerAd> getBannerAdModel() {
        return mBannerAds;
    }

    public void start() {
        serverHandler.start();
    }

    public void handleList(List<BannerAd> bannerAds) {
        HashMap<String, BannerAd> map = getBannerAds();
        for (BannerAd bannerAd : bannerAds) {
            BannerAd check = map.get(bannerAd.getId());
            if (check == null) {
                mBannerAds.addElement(bannerAd);
                notifyBannerListenersOfAdd(bannerAd);
            } else {
                map.remove(bannerAd.getId());

                check.setId(bannerAd.getId());
                check.setPlacementId(bannerAd.getPlacementId());
                check.setBidfloorPrice(bannerAd.getBidfloorPrice());
                check.setBidfloorCurrency(bannerAd.getBidfloorCurrency());
                check.setW(bannerAd.getW());
                check.setH(bannerAd.getH());
                check.setWmin(bannerAd.getWmin());
                check.setWmax(bannerAd.getWmax());
                check.setHmin(bannerAd.getHmin());
                check.setHmax(bannerAd.getHmax());
                check.setAdUnitCode(bannerAd.getAdUnitCode());
                check.setSize(bannerAd.getSize());
                check.setPromoSizes(bannerAd.getPromoSizes());
                check.setMimes(bannerAd.getMimes());
                check.setBtypes(bannerAd.getBtypes());
                check.setBattrs(bannerAd.getBattrs());
                check.setTopframe(bannerAd.getTopframe());
                check.setExpdir(bannerAd.getExpdir());
                check.setApi(bannerAd.getApi());
                check.setExt(bannerAd.getExt());
            }
        }
        // if any are left, remove them as extras
        for (Map.Entry<String, BannerAd> tBannerAd : map.entrySet()) {
            mBannerAds.removeElement(tBannerAd.getValue());
            notifyBannerListenersOfRemove(tBannerAd.getValue());
        }
    }

    public void sendAddCommand(BannerAd sb) throws ModelException {
        serverHandler.sendAddCommand(sb);
    }

    public void sendUpdateCommand(BannerAd sb) throws ModelException {
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


    public void addBannerListener(BannerListener lis) {
        Set<Map.Entry<String, BannerAd>> e = getBannerAds().entrySet();
        for (Map.Entry<String, BannerAd> ee : getBannerAds().entrySet()) {
            lis.added(ee.getValue());
        }
        bannerListeners.add(lis);
    }

    public void removeBannerListener(BannerListener lis) {
        bannerListeners.remove(lis);
    }

    private void notifyBannerListenersOfAdd(BannerAd bannerAd) {
        for (BannerListener lis : bannerListeners) {
            lis.added(bannerAd);
        }
    }

    private void notifyBannerListenersOfRemove(BannerAd bannerAd) {
        for (BannerListener lis : bannerListeners) {
            lis.removed(bannerAd);
        }
    }


}
