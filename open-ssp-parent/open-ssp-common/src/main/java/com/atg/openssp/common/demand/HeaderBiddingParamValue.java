package com.atg.openssp.common.demand;

/**
 * Optimized for handling Header Bidding impression.
 *
 * {
 "id": "N4is0wjhbcButCG",
 "adUnitsToBidUpon": [{
 "id": "15d2c37bf293e74",
 "adUnitCode": "WebDesignLedger_728x90_468x60_320x50_300x100x_300x50_125x125",
 "size": "468x60",
 "promo_sizes": "728x90",
 "bidFloor": 0.4165
 }, {
 "id": "1640c8bcdd7ccec",
 "adUnitCode": "WebDesignLedger_300x1050_300x600_300x250_300x100_2",
 "size": "300x250",
 "promo_sizes": "300x600",
 "bidFloor": 0.4165
 }, {
 "id": "171f8d5d9cde201",
 "adUnitCode": "WebDesignLedger_300x1050_300x600_300x250_300x100_1",
 "size": "300x250",
 "promo_sizes": "300x600",
 "bidFloor": 0.4165
 }],
 "site": 131,
 "page": "/george-cibu-modesty-key-dotw2/",
 "_fshash": "d165d6a050",
 "_fsuid": "41624435-5e3e-47c6-b382-a938abb79283",
 "_fsloc": "?i=US&c=TG9zIEFuZ2VsZXM=",
 "_fssid": "ae19417b-986d-4d49-b680-3b7bf937ae74"
 }
 * @author Brian Sorensen
 *
 */
public class HeaderBiddingParamValue extends ParamValue {

    private String requestId;
    private String psa;
    private String id;
    private String fsHash;
    private String fsSid;
    private String fsLoc;
    private String fsUid;
    private String size;
    private String promoSizes;

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

	@Override
	public String toString() {
        return super.toString()+String.format("[requestId=%s id=%s]", requestId, id);
	}

    public void setPsa(String psa) {
        this.psa = psa;
    }

    public String getPsa() {
        return psa;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFsHash(String fsHash) {
        this.fsHash = fsHash;
    }

    public String getFsHash() {
        return fsHash;
    }

    public void setFsSid(String fsSid) {
        this.fsSid = fsSid;
    }

    public String getFsSid() {
        return fsSid;
    }

    public void setFsLoc(String fsLoc) {
        this.fsLoc = fsLoc;
    }

    public String getFsLoc() {
        return fsLoc;
    }

    public void setFsUid(String fsUid) {
        this.fsUid = fsUid;
    }

    public String getFsUid() {
        return fsUid;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setPromoSizes(String promoSizes) {
        this.promoSizes = promoSizes;
    }

    public String getPromoSizes() {
        return promoSizes;
    }

}
