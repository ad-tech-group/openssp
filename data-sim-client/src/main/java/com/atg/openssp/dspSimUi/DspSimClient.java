package com.atg.openssp.dspSimUi;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.AdModel;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.atg.openssp.dspSimUi.view.dsp.DspView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class DspSimClient {
    private static final Logger log = LoggerFactory.getLogger(DspSimClient.class);
    private final DspView dspView;
    private DspModel dspModel;
    private AdModel adModel;

    public DspSimClient() throws ModelException {
        dspModel = new DspModel();
        dspView = new DspView(dspModel);
        adModel = new AdModel(dspModel.getProperties());
    }

    public void start() {
        dspView.start();
        dspModel.start();
    }

    public static void main(String[] args) {
        try {
            DspSimClient sim = new DspSimClient();
            sim.start();
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}