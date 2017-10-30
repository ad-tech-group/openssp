package com.atg.openssp.common.cache.dto;

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
		return websiteId;
	}

	public void setWebsiteId(final int websiteId) {
		this.websiteId = websiteId;
	}

	public Zone[] getZones() {
		return zones;
	}

	public void setZones(final Zone[] zones) {
		this.zones = zones;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public String[] getBcat() {
		return bcat;
	}

	public void setBcat(final String[] bcat) {
		this.bcat = bcat;
	}

	public String[] getBadv() {
		return badv;
	}

	public void setBadv(final String[] badv) {
		this.badv = badv;
	}

	public String[] getCat() {
		return cat;
	}

	public void setCat(final String[] cat) {
		this.cat = cat;
	}

	@Override
	public String toString() {
		return "Website [websiteId=" + websiteId + ", zones=" + Arrays.toString(zones) + ", currency=" + currency + ", bcat=" + Arrays.toString(bcat) + ", badv=" + Arrays.toString(
		        badv) + ", cat=" + Arrays.toString(cat) + "]";
	}

}
