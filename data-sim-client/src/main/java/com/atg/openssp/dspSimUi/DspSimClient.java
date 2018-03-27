package com.atg.openssp.dspSimUi;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.AdModel;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.atg.openssp.dspSimUi.model.dsp.SiteModel;
import com.atg.openssp.dspSimUi.model.dsp.SupplierModel;
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
    private final DspView dspView;
    private DspModel dspModel;
    private SiteModel siteModel;
    private SupplierModel supplierModel;
    private AdModel adModel;

    public DspSimClient(int index) throws ModelException {
        dspModel = new DspModel(index);
        siteModel = new SiteModel(index);
        supplierModel = new SupplierModel(index);
        dspView = new DspView(dspModel, siteModel, supplierModel);
        adModel = new AdModel(dspModel.getProperties());
    }

    public void start() {
        dspView.start();
        dspModel.start();
        siteModel.start();
        supplierModel.start();
    }

    public static void main(String[] args) {
        for (int i=0; i<2; i++) {
            try {
                DspSimClient sim = new DspSimClient(i);
                sim.start();
            } catch (ModelException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}