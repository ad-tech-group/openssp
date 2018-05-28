package com.atg.openssp.dspSimUi.videoAd;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerAdModel;
import com.atg.openssp.dspSimUi.model.ad.video.VideoAdModel;
import com.atg.openssp.dspSimUi.view.videoAd.VideoAdMaintenanceView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class VideoAdSimClient {
    private static final Logger log = LoggerFactory.getLogger(VideoAdSimClient.class);
    private final VideoAdMaintenanceView videoAdView;
    private VideoAdModel videoAdModel;

    public VideoAdSimClient() throws ModelException {
        BannerAdModel bannerAdModel = new BannerAdModel();
        videoAdModel = new VideoAdModel(bannerAdModel);
        videoAdView = new VideoAdMaintenanceView(videoAdModel);
    }

    public void start() {
        videoAdView.start();
        videoAdModel.start();
    }

    public static void main(String[] args) {
        try {
            VideoAdSimClient sim = new VideoAdSimClient();
            sim.start();
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}