package com.atg.openssp.dspSim.model.dsp;

import openrtb.tables.ContentCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Sorensen
 */
public class SimBidder {
    private final String id;
    private float price;
    private String adid;
//    private String impid;
//    private String nurl;
    private String adm;
    private List<String> adomain = new ArrayList<String>();
    private String iurl;
    private String cid;
    private String crid;
    private List<String> cat = new ArrayList<String>();

    public SimBidder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

//    public void setImpId(String impId) {
//        this.impId = impId;
//    }

//    public String getImpId() {
//        return impId;
//    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setAdid(String adid) {
        this.adid = adid;
    }

    public String getAdid() {
        return adid;
    }

//    public void setNUrl(String nUrl) {
//        this.nUrl = nUrl;
//    }

//    public String getNUrl() {
//        return nUrl;
//    }

    public void setAdm(String adm) {
        this.adm = adm;
    }

    public String getAdm() {
        return adm;
    }

//    public void setAdomain(List<String> adomain) {
//        this.adomain.clear();
//        this.adomain.addAll(adomain);
//    }

    public List<String> getAdomain() {
        return adomain;
    }

    public void addAdomain(String adomain) {
        this.adomain.add(adomain);
    }

    @Override
    public String toString() {
//        return id + ":"+impId+":"+adId+" - ("+price+")";
        return id + ":"+adid+" - ("+price+")";
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
    }

    public String getIurl() {
        return iurl;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setCrId(String crid) {
        this.crid = crid;
    }

    public String getCrid() {
        return crid;
    }

//    public void setCats(List<ContentCategory> cat) {
//        this.cat.clear();
//        if (cat != null) {
//            cat.forEach(c -> this.cat.add(c.getValue()));
//        }
//    }

//    public List<ContentCategory> getCats() {
//        ArrayList<ContentCategory> list = new ArrayList();
//        cat.forEach(c->list.add(ContentCategory.convertValue(c)));
//        return list;
//    }

    public void addCat(ContentCategory cat) {
        this.cat.add(cat.getValue());
    }

    public void populate(SimBidder simBidder) {
//        impId = simBidder.impId;
        price = simBidder.price;
        adid = simBidder.adid;
//        nurl = simBidder.nurl;
        adm = simBidder.adm;
        adomain.clear();
        adomain.addAll(simBidder.adomain);
        iurl = simBidder.iurl;
        cid = simBidder.cid;
        crid = simBidder.crid;
        cat.clear();
        cat.addAll(simBidder.cat);
    }
}
