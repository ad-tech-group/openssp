package com.atg.openssp.dspSimUi.supplier;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.supplier.SupplierModel;
import com.atg.openssp.dspSimUi.view.supplier.SupplierView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class SupplierSimClient {
    private static final Logger log = LoggerFactory.getLogger(SupplierSimClient.class);
    private final SupplierView supplierView;
    private SupplierModel supplierModel;

    public SupplierSimClient() throws ModelException {
        supplierModel = new SupplierModel();
        supplierView = new SupplierView(supplierModel);
    }

    public void start() {
        supplierView.start();
        supplierModel.start();
    }

    public static void main(String[] args) {
        try {
            SupplierSimClient sim = new SupplierSimClient();
            sim.start();
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}