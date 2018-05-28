package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.common.cache.dto.BannerAd;

import java.util.List;

/**
 * @author Brian Sorensen
 */
public class BannerAdResponse {
    private ResponseStatus status;
    private String reason="";
    private List<BannerAd> bannerAds;

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
    
    public void setBannerAds(List<BannerAd> bannerAds) {
        this.bannerAds = bannerAds;
    }

    public List<BannerAd> getBannerAds() {
        return bannerAds;
    }

}
