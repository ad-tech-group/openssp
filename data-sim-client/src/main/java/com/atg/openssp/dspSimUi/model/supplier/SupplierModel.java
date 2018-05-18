package com.atg.openssp.dspSimUi.model.supplier;

import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.dspSimUi.supplier.SupplierServerHandler;
import com.atg.openssp.dspSimUi.model.BaseModel;
import com.atg.openssp.dspSimUi.model.ModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Brian Sorensen
 */
public class SupplierModel extends BaseModel {
    private static final Logger log = LoggerFactory.getLogger(SupplierModel.class);
    private final ResourceBundle bundle;
    private final Properties props = new Properties();
    private DefaultListModel<Supplier> mSuppliers = new DefaultListModel<Supplier>();
    private final SupplierServerHandler supplierHandler;

    public SupplierModel() throws ModelException {
        bundle = ResourceBundle.getBundle("SupplierDisplayTemplate");
        loadProperties();
        supplierHandler = new SupplierServerHandler(this);
    }

    private void loadProperties() {
        try {
            File file = new File("supplier-client.properties");
            InputStream is;
            if (file.exists()) {
                is = new FileInputStream(file);
            } else {
                is = getClass().getClassLoader().getSystemResourceAsStream(file.getName());
            }
            props.load(is);
            is.close();
        } catch (IOException e) {
            log.warn("Could not load properties file.", e);
        }
    }

    public Properties getProperties() {
        return props;
    }

    public String lookupProperty(String key) {
        return props.getProperty(key);
    }

    public String lookupProperty(String key, String defaultValue) {
        String v = lookupProperty(key);
        if (v == null) {
            return defaultValue;
        } else {
            return v;
        }
    }

    private HashMap<Long, Supplier> getSuppliers() {
        HashMap<Long, Supplier> map = new HashMap<Long, Supplier>();
        for (int i=0; i<mSuppliers.size(); i++) {
            map.put(mSuppliers.get(i).getSupplierId(), mSuppliers.get(i));
        }
        return map;
    }

    public DefaultListModel<Supplier> getSupplierModel() {
        return mSuppliers;
    }

    public void start() {
        supplierHandler.start();
    }

    public void handleList(List<Supplier> suppliers) { HashMap<Long, Supplier> map = getSuppliers();
        for (Supplier supplier : suppliers) {
            Supplier check = map.get(supplier.getSupplierId());
            if (check == null) {
                mSuppliers.addElement(supplier);
            } else {
                map.remove(supplier.getSupplierId());
                check.setShortName(supplier.getShortName());
                check.setAllowedAdPlatforms(supplier.getAllowedAdPlatforms());
                check.setActive(supplier.getActive());
                check.setTmax(supplier.getTmax());
                check.setOpenRtbVersion(supplier.getOpenRtbVersion());
                check.setCurrency(supplier.getCurrency());
                check.setContentType(supplier.getContentType());
                check.setDemandBrokerFilterClassName(supplier.getDemandBrokerFilterClassName());
                check.setEndPoint(supplier.getEndPoint());
                check.setUnderTest(supplier.getUnderTest());
                check.setAcceptEncoding(supplier.getAcceptEncoding());
                check.setAllowedAdFormats(supplier.getAllowedAdFormats());
                check.setContentEncoding(supplier.getContentEncoding());
            }
        }
        // if any are left, remove them as extras
        for (Map.Entry<Long, Supplier> tSupplier : map.entrySet()) {
            mSuppliers.removeElement(tSupplier.getValue());
        }
        setMessage("list updated");
    }

    public void sendAddCommand(Supplier sb) throws ModelException {
        supplierHandler.sendAddCommand(sb);
    }

    public void sendUpdateCommand(Supplier sb) throws ModelException {
        supplierHandler.sendUpdateCommand(sb);
    }

    public void sendRemoveCommand(long id) throws ModelException {
        supplierHandler.sendRemoveCommand(id);
    }

    public void sendListCommand() throws ModelException {
        supplierHandler.sendListCommand();
    }

    public void sendLoadCommand() throws ModelException {
        supplierHandler.sendLoadCommand();
    }

    public void sendExportCommand() throws ModelException {
        supplierHandler.sendExportCommand();
    }

    public void sendImportCommand() throws ModelException {
        supplierHandler.sendImportCommand();
    }

    public void sendClearCommand() throws ModelException {
        supplierHandler.sendClearCommand();
    }

    public String getTemplateText(String tag) {
        return bundle.getString(tag);
    }
}
