package com.atg.openssp.dspSimUi.view.dsp;

import com.atg.openssp.dspSimUi.ServerHandler;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brian Sorensen
 */
public class DspView {
    private final DspModel model;
    private final JFrame frame;

    public DspView(DspModel model) {
        this.model = model;
        frame = new JFrame("DSP Sim - "+model.lookupProperty(ServerHandler.SERVER_HOST));
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Bidders", new SimBidderPanel(model));
        frame.setContentPane(tabs);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
