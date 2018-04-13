package com.atg.openssp.dspSimUi.view.supplier;

import com.atg.openssp.dspSimUi.model.supplier.SupplierModel;
import com.atg.openssp.dspSimUi.supplier.SupplierServerHandler;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brian Sorensen
 */
public class SupplierView {
    private final SupplierModel model;
    private final JFrame frame;

    public SupplierView(SupplierModel supplierModel) {
        this.model = supplierModel;
        frame = new JFrame("Supplier Maintenance - "+model.lookupProperty(SupplierServerHandler.SUPPLIER_HOST)+":"+model.lookupProperty(SupplierServerHandler.SUPPLIER_PORT));
        JTabbedPane tabs = new JTabbedPane();
        frame.setContentPane(tabs);

        tabs.addTab("Supplier Maintenance", new SupplierMaintenancePanel(supplierModel));

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(d.width, d.height-40);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void start() {
        frame.setVisible(true);
    }
}
