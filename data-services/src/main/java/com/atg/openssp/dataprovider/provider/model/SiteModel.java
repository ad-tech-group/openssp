package com.atg.openssp.dataprovider.provider.model;

import com.atg.openssp.common.core.broker.dto.SiteDto;
import com.atg.openssp.common.core.system.LocalContext;
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
  "sites": [
    {
      "id": "1",
      "name": "Too Much",
      "domain": "toomuch.com",
      "page": "http://www.toomuch.com/myads.html",
      "cat": [
        "IAB19",
        "IAB19_20",
        "IAB19_24",
        "IAB19_34"
      ],
      "publisher": {
      "id": "5",
        "name": "Big Box of Stuff",
        "cat": [
          "IAB19"
        ],
      "domain": "bbos.com",
        "ext": null
      },
      "ext": null
    }
  ]
}</pre>
*/

/**
 *
 */
public class SiteModel {
    private static final Logger log = LoggerFactory.getLogger(SiteModel.class);
    private static SiteModel singleton;
    private final SiteDto siteDto = new SiteDto();

    private SiteModel() {
        initSites();
    }

    private void initSites() {
        synchronized (siteDto) {
            siteDto.setSite(DataStore.getInstance().lookupSites().getSites());
        }
    }

    public void exportSites(String exportName) {
        if (LocalContext.isSiteDataServiceEnabled()) {
            synchronized (siteDto) {
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
                    pw.println(gson.toJson(siteDto));
                    pw.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void importSites(String importName) {
        if (LocalContext.isSiteDataServiceEnabled()) {
            synchronized (siteDto) {
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
                    String content = new String(Files.readAllBytes(Paths.get(location + importName+".json")), StandardCharsets.UTF_8);
                    SiteDto newData = gson.fromJson(content, SiteDto.class);
                    DataStore.getInstance().clearSites();
                    for (Site s : newData.getSites()) {
                        DataStore.getInstance().insert(s);
                    }
                    initSites();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void loadSites() {
        if (LocalContext.isSiteDataServiceEnabled()) {
            synchronized (siteDto) {
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
                    String content = new String(Files.readAllBytes(Paths.get(location + "site_db.json")), StandardCharsets.UTF_8);
                    SiteDto newData = gson.fromJson(content, SiteDto.class);
                    DataStore.getInstance().clearSites();
                    for (Site s : newData.getSites()) {
                        DataStore.getInstance().insert(s);
                    }
                    initSites();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void resetWith(Iterable<Site> sites) {
        synchronized (siteDto) {
            DataStore.getInstance().clearSites();
            for (Site s : sites) {
                DataStore.getInstance().insert(s);
            }
            initSites();
        }
    }

    public SiteDto lookupSites() {
        synchronized (siteDto) {
            SiteDto dto = new SiteDto();
            dto.setSite(siteDto.getSites());
            return dto;
        }
    }

    public void insert(Site s) {
        synchronized (siteDto) {
            DataStore.getInstance().insert(s);
            initSites();
        }
    }

    public void remove(Site s) {
        synchronized (siteDto) {
            DataStore.getInstance().remove(s);
            initSites();
        }
    }

    public void update(Site s) {
        synchronized (siteDto) {
            DataStore.getInstance().update(s);
            initSites();
        }
    }

    public void load() {
        synchronized (siteDto) {
            DataStore.getInstance().clearSites();
            initSites();
        }
    }

    public void clear() {
        synchronized (siteDto) {
            DataStore.getInstance().clearSites();
            initSites();
        }
    }

    public synchronized static SiteModel getInstance() {
        if (singleton == null) {
            singleton = new SiteModel();
        }
        return singleton;
    }

}
