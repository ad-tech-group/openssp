package com.atg.openssp.dspSimUi.view.videoAd;

import com.atg.openssp.dspSimUi.videoAd.VideoAdServerHandler;
import com.atg.openssp.dspSimUi.model.ad.video.VideoAdModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brian Sorensen
 */
public class VideoAdMaintenanceView {
    private final VideoAdModel model;
    private final JFrame frame;

    public VideoAdMaintenanceView(VideoAdModel videoAdModel) {
        this.model = videoAdModel;
        frame = new JFrame("VideoAd Maintenance - "+model.lookupProperty(VideoAdServerHandler.SITE_HOST)+":"+model.lookupProperty(VideoAdServerHandler.SITE_PORT));
        JTabbedPane tabs = new JTabbedPane();
        frame.setContentPane(tabs);
        tabs.addTab("VideoAd Maintenance", new VideoAdMaintenancePanel(videoAdModel));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
