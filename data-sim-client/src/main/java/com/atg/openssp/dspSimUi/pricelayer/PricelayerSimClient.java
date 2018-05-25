package com.atg.openssp.dspSimUi.pricelayer;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.pricelayer.PricelayerModel;
import com.atg.openssp.dspSimUi.view.pricelayer.PricelayerView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class PricelayerSimClient {
    private static final Logger log = LoggerFactory.getLogger(PricelayerSimClient.class);
    private final PricelayerView pricelayerView;
    private PricelayerModel pricelayerModel;

    public PricelayerSimClient() throws ModelException {
        pricelayerModel = new PricelayerModel();
        pricelayerView = new PricelayerView(pricelayerModel);
    }

    public void start() {
        pricelayerView.start();
        pricelayerModel.start();
    }

    public static void main(String[] args) {
        try {
            PricelayerSimClient sim = new PricelayerSimClient();
            sim.start();
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}