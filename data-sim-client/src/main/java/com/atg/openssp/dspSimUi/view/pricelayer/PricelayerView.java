package com.atg.openssp.dspSimUi.view.pricelayer;

import com.atg.openssp.dspSimUi.model.pricelayer.PricelayerModel;
import com.atg.openssp.dspSimUi.pricelayer.PricelayerServerHandler;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brian Sorensen
 */
public class PricelayerView {
    private final PricelayerModel model;
    private final JFrame frame;

    public PricelayerView(PricelayerModel pricelayerModel) {
        this.model = pricelayerModel;
        frame = new JFrame("Pricelayer Maintenance - "+model.lookupProperty(PricelayerServerHandler.SITE_HOST)+":"+model.lookupProperty(PricelayerServerHandler.SITE_PORT));
        JTabbedPane tabs = new JTabbedPane();
        frame.setContentPane(tabs);
        tabs.addTab("Pricelayer Maintenance", new PricelayerMaintenancePanel(pricelayerModel));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
