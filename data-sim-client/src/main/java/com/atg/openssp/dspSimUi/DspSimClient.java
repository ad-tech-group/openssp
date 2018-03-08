package com.atg.openssp.dspSimUi;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.AdModel;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.atg.openssp.dspSimUi.view.dsp.DspView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
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
            File file = new File("DspSimClient.properties");
            InputStream is;
            if (file.exists()) {
                is = new FileInputStream(file);
            } else {
                is = getClass().getClassLoader().getSystemResourceAsStream("DspSimClient.properties");
            }
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
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}