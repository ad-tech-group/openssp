package com.atg.openssp.dspSimUi;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.ad.AdModel;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerAdModel;
import com.atg.openssp.dspSimUi.model.ad.video.VideoAdModel;
import com.atg.openssp.dspSimUi.model.currency.CurrencyModel;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.atg.openssp.dspSimUi.model.pricelayer.PricelayerModel;
import com.atg.openssp.dspSimUi.model.site.SiteModel;
import com.atg.openssp.dspSimUi.model.supplier.SupplierModel;
import com.atg.openssp.dspSimUi.view.MasterView;
import com.atg.openssp.dspSimUi.view.bannerAd.BannerAdMaintenanceView;
import com.atg.openssp.dspSimUi.view.currency.CurrencyView;
import com.atg.openssp.dspSimUi.view.dsp.DspView;
import com.atg.openssp.dspSimUi.view.pricelayer.PricelayerView;
import com.atg.openssp.dspSimUi.view.site.SiteView;
import com.atg.openssp.dspSimUi.view.supplier.SupplierView;
import com.atg.openssp.dspSimUi.view.videoAd.VideoAdMaintenanceView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class DspSimClient {
    private static final Logger log = LoggerFactory.getLogger(DspSimClient.class);
    private final DspModel dspModel;
    private final AdModel adModel;
    private final SiteModel siteModel;
    private final SupplierModel supplierModel;
    private final CurrencyModel currencyModel;
    private final PricelayerModel pricelayerModel;
    private final BannerAdModel bannerAdModel;
    private final VideoAdModel videoAdModel;
    /*
    private final DspView dspView;
    private final SiteView siteMaintView;
    private final SiteModel siteModel;
    private final SupplierModel supplierModel;
    private final SupplierView supplierMaintView;
    private final CurrencyModel currencyModel;
    private final CurrencyView currencyMaintView;
    private final PricelayerModel pricelayerModel;
    private final PricelayerView pricelayerMaintView;
    private final BannerAdModel bannerAdModel;
    private final BannerAdMaintenanceView bannerAdMaintView;
    private final VideoAdModel videoAdModel;
    private final VideoAdMaintenanceView videoAdMaintView;
    */
    private final MasterView view;

    public DspSimClient() throws ModelException {
        dspModel = new DspModel();
        adModel = new AdModel(dspModel.getProperties());
        supplierModel = new SupplierModel();
        siteModel = new SiteModel();
        currencyModel = new CurrencyModel();
        pricelayerModel = new PricelayerModel();
        bannerAdModel = new BannerAdModel();
        videoAdModel = new VideoAdModel(bannerAdModel);

        /*
        dspView = new DspView(dspModel);
        siteMaintView = new SiteView(siteModel);
        supplierMaintView = new SupplierView(supplierModel);
        currencyMaintView = new CurrencyView(currencyModel);
        pricelayerMaintView = new PricelayerView(pricelayerModel);
        bannerAdMaintView = new BannerAdMaintenanceView(bannerAdModel);
        videoAdMaintView = new VideoAdMaintenanceView(videoAdModel);
        */
        view = new MasterView(
                dspModel,
                adModel,
                siteModel,
                supplierModel,
                currencyModel,
                pricelayerModel,
                bannerAdModel,
                videoAdModel);
    }

    public void start() {
        dspModel.start();
        siteModel.start();
        supplierModel.start();
        currencyModel.start();
        pricelayerModel.start();
        bannerAdModel.start();
        videoAdModel.start();

        /*
        dspView.start();
        siteMaintView.start();
        supplierMaintView.start();
        currencyMaintView.start();
        pricelayerMaintView.start();
        bannerAdMaintView.start();
        videoAdMaintView.start();
        */
        view.start();
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