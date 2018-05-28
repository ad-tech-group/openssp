package com.atg.openssp.common.demand;

import com.atg.openssp.common.cache.dto.VideoAd;

/**
 * Optimized for handling VideoAd impressions with the behaviour of very individual requirements of the tag handler which binds to the SSP.
 * 
 * Use this class as data holder for the request params. Change the fields as you require.
 * 
 * @author André Schmer
 *
 */
public class VideoObjectParamValue extends ParamValue {

    private VideoAd videoad;
    /*
    private List<String> mimes;
    private Integer w;
    private Integer h;
    private Integer minDuration;
    private Integer maxDuration;
    private Integer startDelay;
    private List<Integer> protocols;
    private List<Integer> battr;
    private Integer linearity;
    private List<Banner> companionad;
    private List<Integer> api;
    private Object ext;
    */

    public VideoAd getVideoad() {
        return videoad;
    }

    public void setVideoad(VideoAd videoad) {
        this.videoad = videoad;
    }

    /*
    public void setMimes(List<String> mimes) {
        this.mimes = mimes;
    }

    public List<String> getMimes() {
        return mimes;
    }

    public Integer getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public List<Integer> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<Integer> protocols) {
        this.protocols = protocols;
    }

    public List<Integer> getBattr() {
        return battr;
    }

    public void setBattr(List<Integer> battr) {
        this.battr = battr;
    }

    public Integer getLinearity() {
        return linearity;
    }

    public void setLinearity(Integer linearity) {
        this.linearity = linearity;
    }

    public List<Banner> getCompanionad() {
        return companionad;
    }

    public void setCompanionad(List<Banner> companionad) {
        this.companionad = companionad;
    }

    public List<Integer> getApi() {
        return api;
    }

    public void setApi(List<Integer> api) {
        this.api = api;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }
    */
}
