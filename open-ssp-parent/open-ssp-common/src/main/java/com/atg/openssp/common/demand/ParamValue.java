package com.atg.openssp.common.demand;

import com.atg.openssp.common.cache.dto.VideoAd;
import openrtb.bidrequest.model.App;
import openrtb.bidrequest.model.Publisher;
import openrtb.bidrequest.model.Site;

/**
 * Optimized for handling VideoAd impressions with the behaviour of very individual requirements of the tag handler which binds to the SSP.
 * 
 * Use this class as data holder for the request params. Change the fields as you require.
 * 
 * @author André Schmer
 *
 */
public abstract class ParamValue {

	// private Zone zone;
	private Site site;
	private App app;
	 private VideoAd videoad;
	// private String w;
	// private String h;
	// private List<String> mimes;
	// private String domain;
	// private String page;
	// private List<Integer> protocols;
	// private int startdelay;
	private String ipAddress;
	private String browserUserAgentString;

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

	// public String getW() {
	// return w;
	// }

	// public void setW(final String w) {
	// this.w = w;
	// }

	// public String getH() {
	// return h;
	// }

	// public void setH(final String h) {
	// this.h = h;
	// }

	// public List<String> getMimes() {
	// return mimes;
	// }

	// public void setMimes(final List<String> mimes) {
	// this.mimes = mimes;
	// }

	// public String getDomain() {
	// return domain;
	// }

	// public void setDomain(final String domain) {
	// this.domain = domain;
	// }

	// public String getPage() {
	// return page;
	// }

	// public void setPage(final String page) {
	// this.page = page;
	// }

	// public void setProtocols(final List<Integer> list) {
	// protocols = list;
	//
	// }

	// public List<Integer> getProtocols() {
	// return protocols;
	// }

	// public void setStartdelay(final int startdelay) {
	// this.startdelay = startdelay;
	// }

	// public int getStartdelay() {
	// return startdelay;
	// }

	public String getBrowserUserAgentString() {
		return browserUserAgentString;
	}

	public void setBrowserUserAgentString(String browserUserAgentString) {
		this.browserUserAgentString = browserUserAgentString;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
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
