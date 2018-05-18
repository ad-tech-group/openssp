package com.atg.openssp.dataprovider.provider.model;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.atg.openssp.common.model.EurRef;
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

/*
<pre>
{
  "currency": "EUR",
  "data": [
    {
      "currency": "USD",
      "rate": 1.25
    }
  ]
}
 -- or --
{
  "currency": "USD",
  "data": [
    {
      "currency": "EUR",
      "rate": 0.80
    }
  ]
}
</pre>
 */

/**
 *
 */
public class CurrencyModel {
    private static final Logger log = LoggerFactory.getLogger(CurrencyModel.class);
    private static CurrencyModel singleton;
    private final CurrencyDto currencyDto = new CurrencyDto();

    private CurrencyModel() {
        initCurrency();
    }

    private void initCurrency() {
        synchronized (currencyDto) {
            CurrencyDto c = DataStore.getInstance().lookupCurrency();
            currencyDto.setCurrency(c.getCurrency());
            currencyDto.setData(c.getData());
        }
    }

    public void exportCurrency(String exportName) {
        if (LocalContext.isCurrencyDataServiceEnabled()) {
            synchronized (currencyDto) {
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
                    pw.println(gson.toJson(currencyDto));
                    pw.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void importCurrency(String importName) {
        if (LocalContext.isCurrencyDataServiceEnabled()) {
            synchronized (currencyDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation() + "/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location = "";
                    }
                    GsonBuilder builder = new GsonBuilder();
                    EurRef.populateTypeAdapters(builder);
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + importName+".json")), StandardCharsets.UTF_8);
                    CurrencyDto newData = gson.fromJson(content, CurrencyDto.class);
                    DataStore.getInstance().clearCurrency();
                    DataStore.getInstance().insert(newData);
                    initCurrency();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void loadCurrency() {
        if (LocalContext.isCurrencyDataServiceEnabled()) {
            synchronized (currencyDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation() + "/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location = "";
                    }
                    GsonBuilder builder = new GsonBuilder();
                    EurRef.populateTypeAdapters(builder);
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + "currency_db.json")), StandardCharsets.UTF_8);
                    CurrencyDto newData = gson.fromJson(content, CurrencyDto.class);
                    DataStore.getInstance().clearCurrency();
                    DataStore.getInstance().insert(newData);
                    initCurrency();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void resetWith(Iterable<CurrencyDto> currencies) {
        synchronized (currencyDto) {
            DataStore.getInstance().clearCurrency();
            for (CurrencyDto s : currencies) {
                DataStore.getInstance().insert(s);
            }
            initCurrency();
        }
    }

    public CurrencyDto lookupCurrency() {
        synchronized (currencyDto) {
            CurrencyDto dto = new CurrencyDto();
            dto.setCurrency(currencyDto.getCurrency());
            dto.setData(currencyDto.getData());
            return dto;
        }
    }

    public void insert(CurrencyDto s) {
        synchronized (currencyDto) {
            DataStore.getInstance().insert(s);
            initCurrency();
        }
    }

    public void remove(CurrencyDto s) {
        synchronized (currencyDto) {
            DataStore.getInstance().remove(s);
            initCurrency();
        }
    }

    public void update(CurrencyDto s) {
        synchronized (currencyDto) {
            DataStore.getInstance().update(s);
            initCurrency();
        }
    }

    public void load() {
        synchronized (currencyDto) {
            DataStore.getInstance().clearCurrency();
            initCurrency();
        }
    }

    public void clear() {
        synchronized (currencyDto) {
            DataStore.getInstance().clearCurrency();
            initCurrency();
        }
    }

    public synchronized static CurrencyModel getInstance() {
        if (singleton == null) {
            singleton = new CurrencyModel();
        }
        return singleton;
    }

}
