package com.atg.openssp.core.entry.video;

import com.atg.openssp.common.cache.dto.VideoAd;

import java.util.ArrayList;
import java.util.List;

public class VideoBiddingRequest {
    private ArrayList<VideoAd> adUnitsToBidUpon = new ArrayList<>();
    private String id;
    private String site;
    private String app;
    private String page;
    private String hash;
    private String loc;
    private String uid;
    private String sid;

    /*
	private List<String> mimes;
	// required
	private Integer minduration;
	// required
	private Integer maxduration;
	// required
	private int w;
	// required
	private int h;
	// required
	private int startdelay = 0;

	// required
    @Until(2.2)
	private int protocol;

	@Since(2.2)
	private List<Integer> protocols;

	private List<Integer> battr;

	// required
	private Integer linearity;

	private List<Banner> companionad;

	// required
	private List<Integer> api;

	private Object ext;
     */

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



    public void setVideoAdsToBidUpon(List<VideoAd> adUnitsToBidUpon) {
        this.adUnitsToBidUpon.clear();
        if (adUnitsToBidUpon != null) {
            this.adUnitsToBidUpon.addAll(adUnitsToBidUpon);
        }
    }

    public List<VideoAd> getVideoAdsToBidUpon() {
        ArrayList<VideoAd> list = new ArrayList();
        list.addAll(adUnitsToBidUpon);
        return list;
    }

    public void addVideoAdToBidUpon(VideoAd ad) {
        adUnitsToBidUpon.add(ad);
    }
}
