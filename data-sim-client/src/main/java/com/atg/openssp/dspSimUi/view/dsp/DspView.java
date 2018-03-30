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

    public DspView(DspModel dspModel) {
        this.model = dspModel;
        frame = new JFrame("DSP Sim - "+model.lookupProperty(ServerHandler.SERVER_HOST)+":"+model.lookupProperty(ServerHandler.SERVER_PORT));
        JTabbedPane tabs = new JTabbedPane();
        frame.setContentPane(tabs);

        tabs.addTab("Bidders", new SimBidderPanel(dspModel));

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
