package com.atg.openssp.common.demand;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.cache.dto.VideoAd;

/**
 * Optimized for handling Header Bidding impression.
 *
 * @author Brian Sorensen
 *
 */
public class HeaderBiddingParamValue extends ParamValue {
    private BannerObjectParamValue banner = new BannerObjectParamValue();
    private VideoObjectParamValue video = new VideoObjectParamValue();
    private String id;
    private BannerAd bannerad;
    private VideoAd videoad;

    public final void setId(String id) {
        this.id = id;
    }

    public final String getId() {
        return id;
    }

    @Override
    public String toString() {
        return super.toString()+String.format("[requestId=%s id=%s]", getRequestId(), id);
    }

    public BannerObjectParamValue getBanner() {
        return banner;
    }

    public VideoObjectParamValue getVideo() {
        return video;
    }

    public void setBannerad(BannerAd bannerad) {
        this.bannerad = bannerad;
    }

    public BannerAd getBannerad() {
        return bannerad;
    }

    public void setVideoad(VideoAd bannerad) {
        this.videoad = videoad;
    }

    public VideoAd getVideoad() {
        return videoad;
    }
}
