package com.atg.openssp.dataprovider.provider.model;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.core.broker.dto.BannerAdDto;
import com.atg.openssp.common.core.system.LocalContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.properties.ProjectProperty;

import javax.xml.bind.PropertyException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 */
public class BannerAdModel {
    private static final Logger log = LoggerFactory.getLogger(BannerAdModel.class);
    private static BannerAdModel singleton;
    private final BannerAdDto bannerAdDto = new BannerAdDto();
    private final GsonBuilder builder;

    private BannerAdModel() {
        builder = new GsonBuilder();
        initBannerAds();
    }

    private void initBannerAds() {
        synchronized (bannerAdDto) {
            bannerAdDto.setBannerAds(DataStore.getInstance().lookupBannerAds().getBannerAds());
        }
    }

    public void exportBannerAds(String exportName) {
        if (LocalContext.isBannerAdDataServiceEnabled()) {
            synchronized (bannerAdDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation()+"/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location="";
                    }
                    Gson gson = builder.create();

                    PrintWriter pw = new PrintWriter(new FileWriter(location+exportName+".json"));
                    pw.println(gson.toJson(bannerAdDto));
                    pw.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void importBannerAds(String importName) {
        if (LocalContext.isAdservingChannelEnabled()) {
            synchronized (bannerAdDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation()+"/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location="";
                    }
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + importName+".json")), StandardCharsets.UTF_8);
                    BannerAdDto newData = gson.fromJson(content, BannerAdDto.class);
                    DataStore.getInstance().clearBannerAds();
                    for (BannerAd s : newData.getBannerAds()) {
                        DataStore.getInstance().insert(s);
                    }
                    initBannerAds();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void loadBannerAds() {
        if (LocalContext.isAdservingChannelEnabled()) {
            synchronized (bannerAdDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation()+"/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location="";
                    }
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + "banner_ad_db.json")), StandardCharsets.UTF_8);
                    BannerAdDto newData = gson.fromJson(content, BannerAdDto.class);
                    DataStore.getInstance().clearBannerAds();
                    for (BannerAd s : newData.getBannerAds()) {
                        DataStore.getInstance().insert(s);
                    }
                    initBannerAds();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void resetWith(Iterable<BannerAd> banners) {
        synchronized (bannerAdDto) {
            DataStore.getInstance().clearBannerAds();
            for (BannerAd s : banners) {
                DataStore.getInstance().insert(s);
            }
            initBannerAds();
        }
    }

    public BannerAdDto lookupDto() {
        synchronized (bannerAdDto) {
            BannerAdDto dto = new BannerAdDto();
            dto.setBannerAds(bannerAdDto.getBannerAds());
            return dto;
        }
    }

    public void insert(BannerAd s) {
        synchronized (bannerAdDto) {
            DataStore.getInstance().insert(s);
            initBannerAds();
        }
    }

    public void remove(BannerAd s) {
        synchronized (bannerAdDto) {
            DataStore.getInstance().remove(s);
            initBannerAds();
        }
    }

    public void update(BannerAd s) {
        synchronized (bannerAdDto) {
            DataStore.getInstance().update(s);
            initBannerAds();
        }
    }

    public void load() {
        synchronized (bannerAdDto) {
            DataStore.getInstance().clearBannerAds();
            initBannerAds();
        }
    }

    public void clear() {
        synchronized (bannerAdDto) {
            DataStore.getInstance().clearBannerAds();
            initBannerAds();
        }
    }

    public synchronized static BannerAdModel getInstance() {
        if (singleton == null) {
            singleton = new BannerAdModel();
        }
        return singleton;
    }

}
