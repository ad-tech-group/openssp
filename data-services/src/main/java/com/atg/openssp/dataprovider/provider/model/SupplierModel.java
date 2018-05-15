package com.atg.openssp.dataprovider.provider.model;

import com.atg.openssp.common.core.broker.dto.SupplierDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.demand.Supplier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.ProjectProperty;

import javax.xml.bind.PropertyException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SupplierModel {
    private static final Logger log = LoggerFactory.getLogger(SupplierModel.class);
    private static SupplierModel singleton;
    private final SupplierDto supplierDto = new SupplierDto();

    private SupplierModel() {
        initSuppliers();
    }

    private void initSuppliers() {
        synchronized (supplierDto) {
            supplierDto.setSupplier(DataStore.getInstance().lookupSuppliers().getSupplier());
        }
    }

    public void exportSuppliers() {

    }

    public void importSuppliers() {
        if (LocalContext.isSupplierDataServiceEnabled()) {
            synchronized (supplierDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation()+"/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location="";
                    }
                    GsonBuilder builder = new GsonBuilder();
                    Supplier.populateTypeAdapters(builder);
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + "supplier_db.json")), StandardCharsets.UTF_8);
                    SupplierDto newData = gson.fromJson(content, SupplierDto.class);
                    DataStore.getInstance().clearSupplier();
                    for (Supplier s : newData.getSupplier()) {
                        DataStore.getInstance().insert(s);
                    }
                    initSuppliers();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void resetWith(Iterable<Supplier> suppliers) {
        synchronized (supplierDto) {
            DataStore.getInstance().clearSupplier();
            for (Supplier s : suppliers) {
                DataStore.getInstance().insert(s);
            }
            importSuppliers();
        }
    }

    public SupplierDto lookupSuppliers() {
        synchronized (supplierDto) {
            SupplierDto dto = new SupplierDto();
            dto.setSupplier(supplierDto.getSupplier());
            return dto;
        }
    }

    public void insert(Supplier s) {
        synchronized (supplierDto) {
            DataStore.getInstance().insert(s);
            importSuppliers();
        }
    }

    public void remove(Supplier s) {
        synchronized (supplierDto) {
            DataStore.getInstance().remove(s);
            importSuppliers();
        }
    }

    public void update(Supplier s) {
        synchronized (supplierDto) {
            DataStore.getInstance().update(s);
            importSuppliers();
        }
    }

    public void clear() {
        synchronized (supplierDto) {
            DataStore.getInstance().clearSupplier();
            importSuppliers();
        }
    }

    public synchronized static SupplierModel getInstance() {
        if (singleton == null) {
            singleton = new SupplierModel();
        }
        return singleton;
    }

}
