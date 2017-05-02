package com.atg.openssp.common.demand;

import java.util.List;

import com.atg.openssp.common.dto.VideoAd;
import com.atg.openssp.common.dto.Website;
import com.atg.openssp.common.dto.Zone;

/**
 * Optimized for handling VideoAd impressions with the behaviour of very individual requirements of the tag handler which binds to the SSP.
 * 
 * Use this class as data holder for the request params. Change the fields as you require.
 * 
 * @author André Schmer
 *
 */
public class ParamValue {

	private Zone zone;
	private Website website;
	private VideoAd videoad;
	private String w;
	private String h;
	private String publisherid;
	private List<String> mimes;
	private String domain;
	private String page;
	private List<Integer> protocols;
	private int startdelay;

	public Zone getZone() {
		return zone;
	}

	public void setZone(final Zone zone) {
		this.zone = zone;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(final Website website) {
		this.website = website;
	}

	public VideoAd getVideoad() {
		return videoad;
	}

	public void setVideoad(final VideoAd videad) {
		videoad = videad;
	}

	public String getW() {
		return w;
	}

	public void setW(final String w) {
		this.w = w;
	}

	public String getH() {
		return h;
	}

	public void setH(final String h) {
		this.h = h;
	}

	public String getPublisherid() {
		return publisherid;
	}

	public void setPublisherid(final String publisherid) {
		this.publisherid = publisherid;
	}

	public List<String> getMimes() {
		return mimes;
	}

	public void setMimes(final List<String> mimes) {
		this.mimes = mimes;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(final String domain) {
		this.domain = domain;
	}

	public String getPage() {
		return page;
	}

	public void setPage(final String page) {
		this.page = page;
	}

	public void setProtocols(final List<Integer> list) {
		protocols = list;

	}

	public List<Integer> getProtocols() {
		return protocols;
	}

	public void setStartdelay(final int startdelay) {
		this.startdelay = startdelay;
	}

	public int getStartdelay() {
		return startdelay;
	}

	@Override
	public String toString() {
		return "ParamValue [zone=" + zone + ", website=" + website + ", videoad=" + videoad + ", w=" + w + ", h=" + h + ", publisherid=" + publisherid + ", mimes=" + mimes + ", domain=" + domain + ", page=" + page + ", protocols=" + protocols + ", startdelay=" + startdelay + "]";
	}

}
