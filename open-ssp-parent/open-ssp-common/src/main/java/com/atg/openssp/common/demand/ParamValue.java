package com.atg.openssp.common.demand;

import openrtb.bidrequest.model.App;
import openrtb.bidrequest.model.Site;

/**
 * Optimized for handling impressions with the behaviour of very individual requirements of the tag handler which binds to the SSP.
 * 
 * Use this class as data holder for the request params. Change the fields as you require.
 * 
 * @author André Schmer
 *
 */
public abstract class ParamValue {

	private Site site;
	private App app;
    private String requestId;
    private String uid;
	private String ipAddress;
	private String browserUserAgentString;
	private String psa;
	private String hash;
	private String sid;
	private String loc;
    private String referrer;
    private Float overrideBidFloor;


    public final Site getSite() {
		return site;
	}

	public final void setSite(final Site site) {
		this.site = site;
	}

	public final App getApp() {
		return app;
	}

	public final void setApp(final App app) {
		this.app = app;
	}


    public final void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public final String getRequestId() {
        return requestId;
    }

    public final void setUid(String uid) {
		this.uid = uid;
	}

	public final String getUid() {
		return uid;
	}


	public final String getBrowserUserAgentString() {
		return browserUserAgentString;
	}

	public final void setBrowserUserAgentString(String browserUserAgentString) {
		this.browserUserAgentString = browserUserAgentString;
	}

	public final void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public final String getIpAddress() {
		return ipAddress;
	}

	public final void setPsa(String psa) {
		this.psa = psa;
	}

	public final String getPsa() {
		return psa;
	}

	public final void setHash(String hash) {
		this.hash = hash;
	}

	public final String getHash() {
		return hash;
	}

	public final void setSid(String sid) {
		this.sid = sid;
	}

	public final String getSid() {
		return sid;
	}

	public final void setLoc(String loc) {
		this.loc = loc;
	}

	public final String getLoc() {
		return loc;
	}

    public final void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public final String getReferrer() {
        return referrer;
    }

    public final void setOverrideBidFloor(Float overrideBidFloor) {
		this.overrideBidFloor = overrideBidFloor;
	}

	public final Float getOverrideBidFloor() {
		return overrideBidFloor;
	}

	@Override
	public String toString() {
		String siteString;
		if (site != null) {
			siteString = "site=%s";
		} else {
			siteString = "";
		}
		String appString;
		if (app != null) {
			appString = "app=%s";
		} else {
			appString = "";
		}
		return "ParamValue ["+siteString+appString+"]";
	}

}
