package com.atg.openssp.core.entry.header;

public class AdUnit {
    private String id;
    private String adUnitCode;
    private String size;
    private String promo_sizes;
    private String placementId;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAdUnitCode(String adUnitCode) {
        this.adUnitCode = adUnitCode;
    }

    public String getAdUnitCode() {
        return adUnitCode;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setPromoSizes(String promoSizes) {
        this.promo_sizes = promoSizes;
    }

    public String getPromoSizes() {
        return promo_sizes;
    }

    public void setPlacementId(String placementId) {
        this.placementId = placementId;
    }

    public String getPlacementId() {
        return placementId;
    }
}
