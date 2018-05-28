package com.atg.openssp.dspSimUi.view.bannerAd;

import com.atg.openssp.dspSimUi.model.ad.banner.BannerAdModel;
import com.atg.openssp.dspSimUi.bannerAd.BannerAdServerHandler;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brian Sorensen
 */
public class BannerAdMaintenanceView {
    private final BannerAdModel model;
    private final JFrame frame;

    public BannerAdMaintenanceView(BannerAdModel bannerAdModel) {
        this.model = bannerAdModel;
        frame = new JFrame("BannerAd Maintenance - "+model.lookupProperty(BannerAdServerHandler.SITE_HOST)+":"+model.lookupProperty(BannerAdServerHandler.SITE_PORT));
        JTabbedPane tabs = new JTabbedPane();
        frame.setContentPane(tabs);
        tabs.addTab("BannerAd Maintenance", new BannerAdMaintenancePanel(bannerAdModel));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
