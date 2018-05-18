package com.atg.openssp.dataprovider.provider.model;

import com.atg.openssp.common.core.broker.dto.PricelayerDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import openrtb.bidrequest.model.Pricelayer;
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

public class PricelayerModel {
    private static final Logger log = LoggerFactory.getLogger(PricelayerModel.class);
    private static PricelayerModel singleton;
    private final PricelayerDto pricelayerDto = new PricelayerDto();

    private PricelayerModel() {
        initPricelayers();
    }

    private void initPricelayers() {
        synchronized (pricelayerDto) {
            pricelayerDto.setPricelayer(DataStore.getInstance().lookupPricelayers().getPricelayer());
        }
    }

    public void exportPricelayers(String exportName) {
        if (LocalContext.isSiteDataServiceEnabled()) {
            synchronized (pricelayerDto) {
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
                    pw.println(gson.toJson(pricelayerDto));
                    pw.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void importPricelayers(String importName) {
        if (LocalContext.isPricelayerDataServiceEnabled()) {
            synchronized (pricelayerDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation()+"/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location="";
                    }
                    GsonBuilder builder = new GsonBuilder();
                    Pricelayer.populateTypeAdapters(builder);
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + importName+".json")), StandardCharsets.UTF_8);
                    PricelayerDto newData = gson.fromJson(content, PricelayerDto.class);
                    DataStore.getInstance().clearPricelayer();
                    for (Pricelayer s : newData.getPricelayer()) {
                        DataStore.getInstance().insert(s);
                    }
                    initPricelayers();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void loadPricelayers() {
        if (LocalContext.isPricelayerDataServiceEnabled()) {
            synchronized (pricelayerDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation()+"/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location="";
                    }
                    GsonBuilder builder = new GsonBuilder();
                    Pricelayer.populateTypeAdapters(builder);
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + "price_layer.json")), StandardCharsets.UTF_8);
                    PricelayerDto newData = gson.fromJson(content, PricelayerDto.class);
                    DataStore.getInstance().clearPricelayer();
                    for (Pricelayer s : newData.getPricelayer()) {
                        DataStore.getInstance().insert(s);
                    }
                    initPricelayers();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void resetWith(Iterable<Pricelayer> pricelayers) {
        synchronized (pricelayerDto) {
            DataStore.getInstance().clearPricelayer();
            for (Pricelayer s : pricelayers) {
                DataStore.getInstance().insert(s);
            }
            initPricelayers();
        }
    }

    public PricelayerDto lookupPricelayers() {
        synchronized (pricelayerDto) {
            PricelayerDto dto = new PricelayerDto();
            dto.setPricelayer(pricelayerDto.getPricelayer());
            return dto;
        }
    }

    public void insert(Pricelayer s) {
        synchronized (pricelayerDto) {
            DataStore.getInstance().insert(s);
            initPricelayers();
        }
    }

    public void remove(Pricelayer s) {
        synchronized (pricelayerDto) {
            DataStore.getInstance().remove(s);
            initPricelayers();
        }
    }

    public void update(Pricelayer s) {
        synchronized (pricelayerDto) {
            DataStore.getInstance().update(s);
            initPricelayers();
        }
    }

    public void load() {
        synchronized (pricelayerDto) {
            DataStore.getInstance().clearPricelayer();
            initPricelayers();
        }
    }

    public void clear() {
        synchronized (pricelayerDto) {
            DataStore.getInstance().clearPricelayer();
            initPricelayers();
        }
    }

    public synchronized static PricelayerModel getInstance() {
        if (singleton == null) {
            singleton = new PricelayerModel();
        }
        return singleton;
    }

}
