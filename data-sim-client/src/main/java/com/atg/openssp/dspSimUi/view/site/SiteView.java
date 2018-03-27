package com.atg.openssp.dspSimUi.view.site;

import com.atg.openssp.dspSimUi.ServerHandler;
import com.atg.openssp.dspSimUi.model.site.SiteModel;
import com.atg.openssp.dspSimUi.site.SiteServerHandler;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brian Sorensen
 */
public class SiteView {
    private final SiteModel model;
    private final JFrame frame;

    public SiteView(SiteModel siteModel) {
        this.model = siteModel;
        frame = new JFrame("Site Maintenance - "+model.lookupProperty(SiteServerHandler.SITE_HOST)+":"+model.lookupProperty(SiteServerHandler.SITE_PORT));
        JTabbedPane tabs = new JTabbedPane();
        frame.setContentPane(tabs);
        tabs.addTab("Site Maintenance", new SiteMaintenancePanel(siteModel));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
