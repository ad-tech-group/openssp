package com.atg.openssp.common.cache.dto;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @author André Schmer
 *
 */
public class VideoAd implements Serializable {

	private static final long serialVersionUID = 2035631518654057068L;

	@SerializedName("videoad_id")
	private int videoadId;

	@SerializedName("bidfloor_currency")
	private String bidfloorCurrency = "EUR";

	@SerializedName("bidfloor_price")
	private float bidfloorPrice;

	@SerializedName("min_duration")
	private int minDuration;

	@SerializedName("max_duration")
	private int maxDuration;

	public VideoAd() {}

	public int getVideoadId() {
		return videoadId;
	}

	public void setVideoadId(final int videoadId) {
		this.videoadId = videoadId;
	}

	public String getBidfloorCurrency() {
		return bidfloorCurrency;
	}

	public void setBidfloorCurrency(final String bidfloorCurrency) {
		this.bidfloorCurrency = bidfloorCurrency;
	}

	public float getBidfloorPrice() {
		return bidfloorPrice;
	}

	public void setBidfloorPrice(final float bidfloorPrice) {
		this.bidfloorPrice = bidfloorPrice;
	}

	public int getMinDuration() {
		return minDuration;
	}

	public void setMinDuration(final int minDuration) {
		this.minDuration = minDuration;
	}

	public int getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(final int maxDuration) {
		this.maxDuration = maxDuration;
	}

	@Override
	public String toString() {
		return String.format("VideoAd [videoadId=%s, bidfloorCurrency=%s, bidfloorPrice=%s, minDuration=%s, maxDuration=%s]", videoadId, bidfloorCurrency, bidfloorPrice,
		        minDuration, maxDuration);
	}

}
