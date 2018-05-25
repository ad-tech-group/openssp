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
import java.io.FileWriter;
import java.io.PrintWriter;
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

    public void exportSuppliers(String exportName) {
        if (LocalContext.isSiteDataServiceEnabled()) {
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
                    Site.populateTypeAdapters(builder);
                    Gson gson = builder.create();

                    PrintWriter pw = new PrintWriter(new FileWriter(location+exportName+".json"));
                    pw.println(gson.toJson(supplierDto));
                    pw.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void importSuppliers(String importName) {
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
                    String content = new String(Files.readAllBytes(Paths.get(location + importName+".json")), StandardCharsets.UTF_8);
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

    public void loadSuppliers() {
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
            initSuppliers();
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
            initSuppliers();
        }
    }

    public void remove(Supplier s) {
        synchronized (supplierDto) {
            DataStore.getInstance().remove(s);
            initSuppliers();
        }
    }

    public void update(Supplier s) {
        synchronized (supplierDto) {
            DataStore.getInstance().update(s);
            initSuppliers();
        }
    }

    public void load() {
        synchronized (supplierDto) {
            DataStore.getInstance().clearSupplier();
            initSuppliers();
        }
    }

    public void clear() {
        synchronized (supplierDto) {
            DataStore.getInstance().clearSupplier();
            initSuppliers();
        }
    }

    public synchronized static SupplierModel getInstance() {
        if (singleton == null) {
            singleton = new SupplierModel();
        }
        return singleton;
    }

}
