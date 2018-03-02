package com.atg.openssp.dspSim;

import com.atg.openssp.dspSim.model.ModelException;
import com.atg.openssp.dspSim.model.ad.AdModel;
import com.atg.openssp.dspSim.model.dsp.DspModel;
import com.atg.openssp.dspSim.view.dsp.DspView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Brian Sorensen
 */
public class DspSimClient {
    private static final Logger log = LoggerFactory.getLogger(DspSimClient.class);
    private final Properties props = new Properties();
    private final DspView dspView;
    private DspModel dspModel;
    private AdModel adModel;

    public DspSimClient() throws ModelException {
        load(props);
        dspModel = new DspModel(props);
        dspView = new DspView(dspModel);
        adModel = new AdModel(props);
    }

    private void load(Properties p) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("DspSimClient.properties");
            p.load(is);
            is.close();
        } catch (IOException e) {
            log.warn("Could not load properties file.", e);
        }
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