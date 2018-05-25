package com.atg.openssp.core.entry.header;

import com.atg.openssp.common.cache.dto.BannerAd;
import com.atg.openssp.common.cache.dto.VideoAd;

import java.util.ArrayList;
import java.util.List;

public class HeaderBiddingRequest {
    private ArrayList<BannerAd> adUnitsToBidUpon = new ArrayList<>();
    private ArrayList<VideoAd> adVideoUnitsToBidUpon = new ArrayList<>();
    private String id;
    private String site;
    private String app;
    private String page;
    private String hash;
    private String loc;
    private String uid;
    private String sid;

    public final void setId(String id) {
        this.id = id;
    }

    public final String getId() {
        return id;
    }

    public final void setSite(String site) {
        this.site = site;
    }

    public final String getSite() {
        return site;
    }

    public final void setApp(String app) {
        this.app = app;
    }

    public final String getApp() {
        return app;
    }

    public final void setPage(String page) {
        this.page = page;
    }

    public final String getPage() {
        return page;
    }

    public final void setHash(String hash) {
        this.hash = hash;
    }

    public final String getHash() {
        return hash;
    }

    public final void setLoc(String loc) {
        this.loc = loc;
    }

    public final String getLoc() {
        return loc;
    }

    public final void setUid(String uid) {
        this.uid = uid;
    }

    public final String getUid() {
        return uid;
    }

    public final void setSid(String sid) {
        this.sid = sid;
    }

    public final String getSid() {
        return sid;
    }

    public void setAdsToBidUpon(List<BannerAd> adUnitsToBidUpon) {
        this.adUnitsToBidUpon.clear();
        if (adUnitsToBidUpon != null) {
            this.adUnitsToBidUpon.addAll(adUnitsToBidUpon);
        }
    }

    public void setVideoAdsToBidUpon(List<VideoAd> adUnitsToBidUpon) {
        this.adVideoUnitsToBidUpon.clear();
        if (adUnitsToBidUpon != null) {
            this.adVideoUnitsToBidUpon.addAll(adUnitsToBidUpon);
        }
    }

    public List<BannerAd> getAdsToBidUpon() {
        ArrayList<BannerAd> list = new ArrayList();
        list.addAll(adUnitsToBidUpon);
        return list;
    }

    public List<VideoAd> getVideoAdsToBidUpon() {
        ArrayList<VideoAd> list = new ArrayList();
        list.addAll(adVideoUnitsToBidUpon);
        return list;
    }

    public void addHeaderBiddingAdToBidUpon(BannerAd ba) {
        adUnitsToBidUpon.add(ba);
    }

    public void addHeaderBiddingAdToBidUpon(VideoAd va) {
        adVideoUnitsToBidUpon.add(va);
    }
}
