package com.atg.openssp.dspSim.view.dsp;

import com.atg.openssp.dspSim.model.dsp.DspModel;

import javax.swing.*;
import java.awt.*;

public class DspView {
    private final DspModel model;
    private final JFrame frame;

    public DspView(DspModel model) {
        this.model = model;
        frame = new JFrame("DSP Sim");
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Bidders", new SimBidderPanel(model));
        frame.setContentPane(tabs);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}
