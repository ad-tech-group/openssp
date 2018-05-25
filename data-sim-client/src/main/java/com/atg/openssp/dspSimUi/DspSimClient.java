package com.atg.openssp.dspSimUi;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.AdModel;
import com.atg.openssp.dspSimUi.model.currency.CurrencyModel;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.atg.openssp.dspSimUi.model.pricelayer.PricelayerModel;
import com.atg.openssp.dspSimUi.model.site.SiteModel;
import com.atg.openssp.dspSimUi.model.supplier.SupplierModel;
import com.atg.openssp.dspSimUi.view.currency.CurrencyView;
import com.atg.openssp.dspSimUi.view.dsp.DspView;
import com.atg.openssp.dspSimUi.view.pricelayer.PricelayerView;
import com.atg.openssp.dspSimUi.view.site.SiteView;
import com.atg.openssp.dspSimUi.view.supplier.SupplierView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class DspSimClient {
    private static final Logger log = LoggerFactory.getLogger(DspSimClient.class);
    private final DspView dspView;
    private final SiteView siteMaintView;
    private final SiteModel siteModel;
    private final SupplierModel supplierModel;
    private final SupplierView supplierMaintView;
    private final CurrencyModel currencyModel;
    private final CurrencyView currencyMaintView;
    private final PricelayerModel pricelayerModel;
    private final PricelayerView pricelayerMaintView;
    private DspModel dspModel;
    private AdModel adModel;

    public DspSimClient() throws ModelException {
        dspModel = new DspModel();
        dspView = new DspView(dspModel);

        adModel = new AdModel(dspModel.getProperties());

        siteModel = new SiteModel();
        siteMaintView = new SiteView(siteModel);

        supplierModel = new SupplierModel();
        supplierMaintView = new SupplierView(supplierModel);

        currencyModel = new CurrencyModel();
        currencyMaintView = new CurrencyView(currencyModel);

        pricelayerModel = new PricelayerModel();
        pricelayerMaintView = new PricelayerView(pricelayerModel);
    }

    public void start() {
        dspView.start();
        dspModel.start();
        siteMaintView.start();
        siteModel.start();
        supplierMaintView.start();
        supplierModel.start();
        currencyMaintView.start();
        currencyModel.start();
        pricelayerMaintView.start();
        pricelayerModel.start();
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