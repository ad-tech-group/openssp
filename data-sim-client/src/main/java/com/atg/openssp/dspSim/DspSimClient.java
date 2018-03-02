package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.ad.AdModel;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.atg.openssp.dspSim.view.dsp.DspView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DspSimClient {
    private static final Logger log = LoggerFactory.getLogger(DspSimClient.class);
    private final DspView dspView;
    private DspModel dspModel;
    private AdModel adModel;

    public DspSimClient() throws ModelException {
        dspModel = new DspModel();
        dspView = new DspView(dspModel);
        adModel = new AdModel();
    }

    public void start() {
        dspView.start();
        dspModel.start();
    }

    public static void main(String[] args) {
        try {
            DspSimClient sim = new DspSimClient();
            sim.start();
            while(true) {
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}