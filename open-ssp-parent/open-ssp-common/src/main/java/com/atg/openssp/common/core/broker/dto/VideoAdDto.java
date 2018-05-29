package com.atg.openssp.common.core.broker.dto;

import com.atg.openssp.common.cache.dto.VideoAd;

import java.io.Serializable;
import java.util.List;

/**
 * @author Brian Sorensen
 *
 */
public class VideoAdDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<VideoAd> videoAd;

    public VideoAdDto() {}

    public List<VideoAd> getVideoAd() {
        return videoAd;
    }

    public void setVideoAd(final List<VideoAd> videoAd) {
        this.videoAd = videoAd;
    }

    @Override
    public String toString() {
        return String.format("VideoAdsDto [videoAd=%s]", videoAd);
    }

}
