package com.atg.openssp.dataprovider.provider.model;

import com.atg.openssp.common.cache.dto.VideoAd;
import com.atg.openssp.common.core.broker.dto.VideoAdDto;
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

/**
 *
 */
public class VideoAdModel {
    private static final Logger log = LoggerFactory.getLogger(VideoAdModel.class);
    private static VideoAdModel singleton;
    private final VideoAdDto videoAdDto = new VideoAdDto();
    private final GsonBuilder builder;

    private VideoAdModel() {
        builder = new GsonBuilder();
        initVideoAds();
    }

    private void initVideoAds() {
        synchronized (videoAdDto) {
            videoAdDto.setVideoAds(DataStore.getInstance().lookupVideoAds().getVideoAds());
        }
    }

    public void exportVideoAds(String exportName) {
        if (LocalContext.isAdservingChannelEnabled()) {
            synchronized (videoAdDto) {
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
                    pw.println(gson.toJson(videoAdDto));
                    pw.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void importVideoAds(String importName) {
        if (LocalContext.isAdservingChannelEnabled()) {
            synchronized (videoAdDto) {
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
                    VideoAdDto newData = gson.fromJson(content, VideoAdDto.class);
                    DataStore.getInstance().clearVideoAds();
                    for (VideoAd s : newData.getVideoAds()) {
                        DataStore.getInstance().insert(s);
                    }
                    initVideoAds();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void loadVideoAds() {
        if (LocalContext.isVideoAdDataServiceEnabled()) {
            synchronized (videoAdDto) {
                try {
                    String location;
                    try {
                        location = ProjectProperty.getPropertiesResourceLocation()+"/";
                    } catch (PropertyException e) {
                        log.warn("property file not found.");
                        location="";
                    }
                    Gson gson = builder.create();
                    String content = new String(Files.readAllBytes(Paths.get(location + "video_ad_db.json")), StandardCharsets.UTF_8);
                    VideoAdDto newData = gson.fromJson(content, VideoAdDto.class);
                    DataStore.getInstance().clearVideoAds();
                    for (VideoAd s : newData.getVideoAds()) {
                        DataStore.getInstance().insert(s);
                    }
                    initVideoAds();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void resetWith(Iterable<VideoAd> videos) {
        synchronized (videoAdDto) {
            DataStore.getInstance().clearVideoAds();
            for (VideoAd s : videos) {
                DataStore.getInstance().insert(s);
            }
            initVideoAds();
        }
    }

    public VideoAdDto lookupDto() {
        synchronized (videoAdDto) {
            VideoAdDto dto = new VideoAdDto();
            dto.setVideoAds(videoAdDto.getVideoAds());
            return dto;
        }
    }

    public void insert(VideoAd s) {
        synchronized (videoAdDto) {
            DataStore.getInstance().insert(s);
            initVideoAds();
        }
    }

    public void remove(VideoAd s) {
        synchronized (videoAdDto) {
            DataStore.getInstance().remove(s);
            initVideoAds();
        }
    }

    public void update(VideoAd s) {
        synchronized (videoAdDto) {
            DataStore.getInstance().update(s);
            initVideoAds();
        }
    }

    public void load() {
        synchronized (videoAdDto) {
            DataStore.getInstance().clearVideoAds();
            initVideoAds();
        }
    }

    public void clear() {
        synchronized (videoAdDto) {
            DataStore.getInstance().clearVideoAds();
            initVideoAds();
        }
    }

    public synchronized static VideoAdModel getInstance() {
        if (singleton == null) {
            singleton = new VideoAdModel();
        }
        return singleton;
    }

}
