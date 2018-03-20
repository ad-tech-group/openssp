package com.atg.openssp.dspSim.model.dsp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Sorensen
 */
public class SimBidder {
    private final String id;
    private float price;
    private String adId;
//    private String nUrl;
    private String adm;
    private List<String> adomain = new ArrayList<String>();
    private String iUrl;
    private String cId;
    private String crId;
//    private List<String> cat = new ArrayList<String>();

    public SimBidder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

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

//    public void setCat(List<String> cat) {
//        this.cat.clear();
//        this.cat.addAll(cat);
//    }

//    public List<String> getCat() {
//        return cat;
//    }

//    public void addCat(String cat) {
//        this.cat.add(cat);
//    }

    public void populate(SimBidder simBidder) {
        price = simBidder.price;
        adId = simBidder.adId;
//        nUrl = simBidder.nUrl;
        adm = simBidder.adm;
        adomain.clear();
        adomain.addAll(simBidder.adomain);
        iUrl = simBidder.iUrl;
        cId = simBidder.cId;
        crId = simBidder.crId;
//        cat.clear();
//        cat.addAll(simBidder.cat);
    }
}
