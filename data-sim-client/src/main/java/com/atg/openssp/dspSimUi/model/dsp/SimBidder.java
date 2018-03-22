package com.atg.openssp.dspSimUi.model.dsp;

import openrtb.tables.ContentCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Sorensen
 */
public class SimBidder {
    private final String id;
    private float price;
    private String adId;
//    private String impId;
//    private String nUrl;
    private String adm;
    private List<String> adomain = new ArrayList<String>();
    private String iUrl;
    private String cId;
    private String crId;
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

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdId() {
        return adId;
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

    public void setAdomain(List<String> adomain) {
        this.adomain.clear();
        this.adomain.addAll(adomain);
    }

    public List<String> getAdomain() {
        return adomain;
    }

    public void addAdomain(String adomain) {
        this.adomain.add(adomain);
    }

    @Override
    public String toString() {
//        return id + ":"+impId+":"+adId+" - ("+price+")";
        return id + ":"+adId+" - ("+price+")";
    }

    public void setIUrl(String iUrl) {
        this.iUrl = iUrl;
    }

    public String getIUrl() {
        return iUrl;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    public String getCId() {
        return cId;
    }

    public void setCrId(String crId) {
        this.crId = crId;
    }

    public String getCrId() {
        return crId;
    }

    public void setCats(List<ContentCategory> cat) {
        this.cat.clear();
        if (cat != null) {
            cat.forEach(c -> this.cat.add(c.getValue()));
        }
    }

    public List<ContentCategory> getCats() {
        ArrayList<ContentCategory> list = new ArrayList();
        cat.forEach(c->list.add(ContentCategory.convertValue(c)));
        return list;
    }

    public void addCat(ContentCategory cat) {
        this.cat.add(cat.getValue());
    }

    public void populate(SimBidder simBidder) {
//        impId = simBidder.impId;
        price = simBidder.price;
        adId = simBidder.adId;
//        nUrl = simBidder.nUrl;
        adm = simBidder.adm;
        adomain.clear();
        adomain.addAll(simBidder.adomain);
        iUrl = simBidder.iUrl;
        cId = simBidder.cId;
        crId = simBidder.crId;
        cat.clear();
        cat.addAll(simBidder.cat);
    }
}
