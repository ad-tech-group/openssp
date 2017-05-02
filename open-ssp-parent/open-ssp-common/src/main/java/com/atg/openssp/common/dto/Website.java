package com.atg.openssp.common.dto;

import java.io.Serializable;
import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

/**
 * @author André Schmer
 *
 */
public class Website implements Serializable {

	private static final long serialVersionUID = -392487707893201639L;

	@SerializedName("website_id")
	private int websiteId;

	private Zone[] zones;

	private String currency = "EUR";

	@SerializedName("blacklist_category")
	private String[] bcat;

	@SerializedName("blocklist_advertiser_domain")
	private String[] badv;

	@SerializedName("iab_categories")
	private String[] cat;

	public Website() {}

	public int getWebsiteId() {
		return this.websiteId;
	}

	public void setWebsiteId(final int websiteId) {
		this.websiteId = websiteId;
	}

	public Zone[] getZones() {
		return this.zones;
	}

	public void setZones(final Zone[] zones) {
		this.zones = zones;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public String[] getBcat() {
		return this.bcat;
	}

	public void setBcat(final String[] bcat) {
		this.bcat = bcat;
	}

	public String[] getBadv() {
		return this.badv;
	}

	public void setBadv(final String[] badv) {
		this.badv = badv;
	}

	public String[] getCat() {
		return this.cat;
	}

	public void setCat(final String[] cat) {
		this.cat = cat;
	}

	@Override
	public String toString() {
		return "Website [websiteId=" + this.websiteId + ", zones=" + Arrays.toString(this.zones) + ", currency=" + this.currency + ", bcat=" + Arrays.toString(this.bcat)
		        + ", badv=" + Arrays.toString(this.badv) + ", cat=" + Arrays.toString(this.cat) + "]";
	}

}
