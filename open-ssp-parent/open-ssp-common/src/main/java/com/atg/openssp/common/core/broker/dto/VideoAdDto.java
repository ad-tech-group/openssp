package com.atg.openssp.common.core.broker.dto;

import com.atg.openssp.common.cache.dto.VideoAd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Sorensen
 *
 */
public class VideoAdDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<VideoAd> videoAds = new ArrayList<>();

    public VideoAdDto() {
    }

    public List<VideoAd> getVideoAds() {
        ArrayList<VideoAd> list = new ArrayList();
        list.addAll(videoAds);
        return list;
    }

    public void setVideoAds(final List<VideoAd> videoAds) {
        this.videoAds.clear();
        if (videoAds != null) {
            this.videoAds.addAll(videoAds);
        }
    }

    @Override
    public String toString() {
        return String.format("VideoAdsDto [videoAds=%s]", videoAds);
    }

}