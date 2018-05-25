package com.atg.openssp.core.entry.banner;

import com.atg.openssp.common.cache.dto.BannerAd;

import java.util.ArrayList;
import java.util.List;

public class BannerBiddingRequest {
    private ArrayList<BannerAd> adUnitsToBidUpon = new ArrayList<>();
    private String id;
    private String site;
    private String app;
    private String page;
    private String hash;
    private String loc;
    private String uid;
    private String sid;


    /*
	private int w;
	private int h;

	private String id;
	// optional
	private Integer pos;
	private List<Integer> btype;// blocked creative types
	private List<Integer> battr;
	private String[] mimes;// commaseparated list
	private int topframe = 0;
	private int[] expdir; // expandable directions 1-6
	private int[] api;
	private Object ext;

	@Since(2.2)
	private int wmax;

	@Since(2.2)
	private int hmax;

	@Since(2.2)
	private int wmin;

	@Since(2.2)
	private int hmin;
	private Object[] format;
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

    public void setBannerAdsToBidUpon(List<BannerAd> adUnitsToBidUpon) {
        this.adUnitsToBidUpon.clear();
        if (adUnitsToBidUpon != null) {
            this.adUnitsToBidUpon.addAll(adUnitsToBidUpon);
        }
    }

    public List<BannerAd> getBannerAdsToBidUpon() {
        ArrayList<BannerAd> list = new ArrayList();
        list.addAll(adUnitsToBidUpon);
        return list;
    }

    public void addBannerAdToBidUpon(BannerAd ba) {
        adUnitsToBidUpon.add(ba);
    }
}
