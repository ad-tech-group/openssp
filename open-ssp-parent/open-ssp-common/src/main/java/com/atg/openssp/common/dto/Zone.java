package com.atg.openssp.common.dto;

import java.io.Serializable;
import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

/**
 * @author André Schmer
 *
 */
public class Zone implements Serializable {

	private static final long serialVersionUID = -1326925356645010127L;

	@SerializedName("zone_id")
	private int zoneId;

	// @SerializedName("website_id")
	private int websiteId;

	@SerializedName("site_id")
	private String siteId;

	@SerializedName("site_name")
	private String siteName;

	@SerializedName("iab_categories")
	private String[] categories;

	public Zone() {}

	public int getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(final int zoneId) {
		this.zoneId = zoneId;
	}

	public int getWebsiteId() {
		return this.websiteId;
	}

	public void setWebsiteId(final int websiteId) {
		this.websiteId = websiteId;
	}

	public String getSiteId() {
		return this.siteId;
	}

	public void setSiteId(final String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(final String siteName) {
		this.siteName = siteName;
	}

	public String[] getCategories() {
		return this.categories;
	}

	public void setCategories(final String[] categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return String.format("Zone [zoneId=%s, siteId=%s, siteName=%s, categories=%s]", this.zoneId, this.siteId, this.siteName, Arrays.toString(this.categories));
	}

}
