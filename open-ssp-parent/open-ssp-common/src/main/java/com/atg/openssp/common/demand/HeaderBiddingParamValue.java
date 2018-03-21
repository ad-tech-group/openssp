package com.atg.openssp.common.demand;

import openrtb.bidrequest.model.App;

/**
 * Optimized for handling VideoAd impressions with the behaviour of very individual requirements of the tag handler which binds to the SSP.
 * 
 * Use this class as data holder for the request params. Change the fields as you require.
 * 
 * @author André Schmer
 *
 */
public class HeaderBiddingParamValue extends ParamValue {

//	private Publisher publisher;
    private String requestId;
    private String callback;
    private String callbackUid;
    private String psa;
    private String id;
    private String fsHash;
    private String fsSid;
    private String fsLoc;
    private String fsUid;
    private String size;
    private String promoSizes;

//    public Publisher getPublisher() {
//		return publisher;
//	}

//	public void setPublisher(final Publisher publisher) {
//		this.publisher = publisher;
//	}

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

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallbackUid(String callbackUid) {
        this.callbackUid = callbackUid;
    }

    public String getCallbackUid() {
        return callbackUid;
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
