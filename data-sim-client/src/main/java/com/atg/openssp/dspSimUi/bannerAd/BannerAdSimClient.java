package com.atg.openssp.dspSimUi.bannerAd;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerAdModel;
import com.atg.openssp.dspSimUi.view.bannerAd.BannerAdMaintenanceView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class BannerAdSimClient {
    private static final Logger log = LoggerFactory.getLogger(BannerAdSimClient.class);
    private final BannerAdMaintenanceView bannerAdView;
    private BannerAdModel bannerAdModel;

    public BannerAdSimClient() throws ModelException {
        bannerAdModel = new BannerAdModel();
        bannerAdView = new BannerAdMaintenanceView(bannerAdModel);
    }

    public void start() {
        bannerAdView.start();
        bannerAdModel.start();
    }

    public static void main(String[] args) {
        try {
            BannerAdSimClient sim = new BannerAdSimClient();
            sim.start();
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}