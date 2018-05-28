package com.atg.openssp.dspSimUi.model.ad.video;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.cache.dto.VideoAd;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerAdModel;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerListener;
import com.atg.openssp.dspSimUi.videoAd.VideoAdServerHandler;
import com.atg.openssp.dspSimUi.model.BaseModel;
import com.atg.openssp.dspSimUi.model.ModelException;
import com.google.gson.annotations.SerializedName;
import openrtb.bidrequest.model.Banner;
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
public class VideoAdModel extends BaseModel {
    private static final Logger log = LoggerFactory.getLogger(VideoAdModel.class);
    private final ResourceBundle bundle;
    private final Properties props = new Properties();
    private final BannerAdModel bannerModel;
    private DefaultListModel<VideoAd> mVideoAds = new DefaultListModel<>();
    private final VideoAdServerHandler serverHandler;

    public VideoAdModel(BannerAdModel bannerModel) throws ModelException {
        this.bannerModel = bannerModel;
        bundle = ResourceBundle.getBundle("VideoAdDisplayTemplate");
        loadProperties();
        serverHandler = new VideoAdServerHandler(this);
    }

    private void loadProperties() {
        try {
            File file = new File("video-ad-client.properties");
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

    private HashMap<String, VideoAd> getVideoAds() {
        HashMap<String, VideoAd> map = new HashMap<String, VideoAd>();
        for (int i = 0; i < mVideoAds.size(); i++) {
            map.put(mVideoAds.get(i).getId(), mVideoAds.get(i));
        }
        return map;
    }

    public DefaultListModel<VideoAd> getVideoAdModel() {
        return mVideoAds;
    }

    public void start() {
        serverHandler.start();
    }

    public void handleList(List<VideoAd> videoAds) {
        HashMap<String, VideoAd> map = getVideoAds();
        for (VideoAd videoAd : videoAds) {
            VideoAd check = map.get(videoAd.getId());
            if (check == null) {
                mVideoAds.addElement(videoAd);
            } else {
                map.remove(videoAd.getId());

                check.setId(videoAd.getId());
                check.setVidId(videoAd.getVidId());
                check.setBidfloorPrice(videoAd.getBidfloorPrice());
                check.setBidfloorCurrency(videoAd.getBidfloorCurrency());
                check.setW(videoAd.getW());
                check.setH(videoAd.getH());
                check.setMinDuration(videoAd.getMinDuration());
                check.setMaxDuration(videoAd.getMaxDuration());
                check.setStartDelay(videoAd.getStartDelay());
                check.setProtocols(videoAd.getProtocols());
                check.setBattr(videoAd.getBattr());
                check.setLinearity(videoAd.getLinearity());
                check.setCompanionad(videoAd.getCompanionad());
                check.setMimes(videoAd.getMimes());
                check.setApi(videoAd.getApi());
                check.setExt(videoAd.getExt());
            }
        }
        // if any are left, remove them as extras
        for (Map.Entry<String, VideoAd> tVideoAd : map.entrySet()) {
            mVideoAds.removeElement(tVideoAd.getValue());
        }
    }

    public void sendAddCommand(VideoAd sb) throws ModelException {
        serverHandler.sendAddCommand(sb);
    }

    public void sendUpdateCommand(VideoAd sb) throws ModelException {
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
        bannerModel.addBannerListener(lis);
    }

    public void removeBannerListener(BannerListener lis) {
        bannerModel.removeBannerListener(lis);
    }

}
