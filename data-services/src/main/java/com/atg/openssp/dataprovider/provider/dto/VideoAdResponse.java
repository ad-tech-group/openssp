package com.atg.openssp.dataprovider.provider.dto;

import com.atg.openssp.common.cache.dto.VideoAd;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class VideoAdResponse {
    private ResponseStatus status;
    private String reason="";
    private List<VideoAd> videoAds;

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
    
    public void setVideoAds(List<VideoAd> videoAds) {
        this.videoAds = videoAds;
    }

    public List<VideoAd> getVideoAds() {
        return videoAds;
    }

}
