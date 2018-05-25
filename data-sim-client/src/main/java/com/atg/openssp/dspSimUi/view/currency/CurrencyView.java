package com.atg.openssp.dspSimUi.view.currency;

import com.atg.openssp.dspSimUi.model.currency.CurrencyModel;
import com.atg.openssp.dspSimUi.currency.CurrencyServerHandler;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brian Sorensen
 */
public class CurrencyView {
    private final CurrencyModel model;
    private final JFrame frame;

    public CurrencyView(CurrencyModel currencyModel) {
        this.model = currencyModel;
        frame = new JFrame("Currency Maintenance - "+model.lookupProperty(CurrencyServerHandler.SITE_HOST)+":"+model.lookupProperty(CurrencyServerHandler.SITE_PORT));
        JTabbedPane tabs = new JTabbedPane();
        frame.setContentPane(tabs);
        tabs.addTab("Currency Maintenance", new CurrencyMaintenancePanel(currencyModel));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
