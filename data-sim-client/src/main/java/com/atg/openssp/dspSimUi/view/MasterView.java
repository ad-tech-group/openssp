package com.atg.openssp.dspSimUi.view;

import com.atg.openssp.dspSimUi.model.ad.AdModel;
import com.atg.openssp.dspSimUi.model.ad.banner.BannerAdModel;
import com.atg.openssp.dspSimUi.model.ad.video.VideoAdModel;
import com.atg.openssp.dspSimUi.model.currency.CurrencyModel;
import com.atg.openssp.dspSimUi.model.dsp.DspModel;
import com.atg.openssp.dspSimUi.model.pricelayer.PricelayerModel;
import com.atg.openssp.dspSimUi.model.site.SiteModel;
import com.atg.openssp.dspSimUi.model.supplier.SupplierModel;
import com.atg.openssp.dspSimUi.videoAd.VideoAdServerHandler;
import com.atg.openssp.dspSimUi.view.bannerAd.BannerAdMaintenancePanel;
import com.atg.openssp.dspSimUi.view.currency.CurrencyMaintenancePanel;
import com.atg.openssp.dspSimUi.view.currency.CurrencyView;
import com.atg.openssp.dspSimUi.view.dsp.SimBidderPanel;
import com.atg.openssp.dspSimUi.view.pricelayer.PricelayerMaintenancePanel;
import com.atg.openssp.dspSimUi.view.site.SiteMaintenancePanel;
import com.atg.openssp.dspSimUi.view.site.SiteView;
import com.atg.openssp.dspSimUi.view.supplier.SupplierMaintenancePanel;
import com.atg.openssp.dspSimUi.view.supplier.SupplierView;
import com.atg.openssp.dspSimUi.view.videoAd.VideoAdMaintenancePanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brian Sorensen
 */
public class MasterView {
    private final VideoAdModel model;
    private final JFrame frame;

    public MasterView(DspModel dspModel, AdModel adModel, SiteModel siteModel, SupplierModel supplierModel, CurrencyModel currencyModel, PricelayerModel pricelayerModel, BannerAdModel bannerAdModel, VideoAdModel videoAdModel) {
        this.model = videoAdModel;
        frame = new JFrame("MasterView - "+model.lookupProperty(VideoAdServerHandler.SITE_HOST)+":"+model.lookupProperty(VideoAdServerHandler.SITE_PORT));
        JTabbedPane tabs = new JTabbedPane();
        frame.setContentPane(tabs);

        tabs.addTab("Sim", new SimBidderPanel(dspModel));
        tabs.addTab("Site", new SiteMaintenancePanel(siteModel));
        tabs.addTab("Supplier", new SupplierMaintenancePanel(supplierModel));
        tabs.addTab("Currency", new CurrencyMaintenancePanel(currencyModel));
        tabs.addTab("Pricelayer", new PricelayerMaintenancePanel(pricelayerModel));
        tabs.addTab("BannerAd", new BannerAdMaintenancePanel(bannerAdModel));
        tabs.addTab("VideoAd", new VideoAdMaintenancePanel(videoAdModel));


        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
