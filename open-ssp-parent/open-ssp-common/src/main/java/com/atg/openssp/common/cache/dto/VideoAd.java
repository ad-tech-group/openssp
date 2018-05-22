package com.atg.openssp.common.cache.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import openrtb.bidrequest.model.Banner;

/**
 * @author André Schmer
 *
 */
public class VideoAd implements Serializable {
	private static final long serialVersionUID = 2035631518654057068L;

    @SerializedName("id")
    private String id;

    @SerializedName("bidfloor_currency")
    private String bidfloorCurrency = "EUR";

    @SerializedName("bidfloor_price")
    private float bidfloorPrice;

    private List<String> mimes;
    private Integer w;
    private Integer h;

    @SerializedName("min_duration")
    private int minDuration;

    @SerializedName("max_duration")
    private int maxDuration;
    @SerializedName("start_delay")
    private Integer startDelay;
    private List<Integer> protocols;
    private List<Integer> battr;
    private Integer linearity;
    private List<Banner> companionad;
    private List<Integer> api;
    private Object ext;

    @SerializedName("videoad_id")
    private String vidId;

    public VideoAd() {
        mimes = new ArrayList<String>();
    }

    public final String getId() {
        return id;
    }

    public final void setId(final String id) {
        this.id = id;
    }

    public String getBidfloorCurrency() {
        return bidfloorCurrency;
    }

    public final void setBidfloorCurrency(final String bidfloorCurrency) {
        this.bidfloorCurrency = bidfloorCurrency;
    }

    public final float getBidfloorPrice() {
        return bidfloorPrice;
    }

    public final void setBidfloorPrice(final float bidfloorPrice) {
        this.bidfloorPrice = bidfloorPrice;
    }

    public List<String> getMimes() {
        ArrayList list = new ArrayList();
        list.addAll(mimes);
        return list;
    }

    public void setMimes(List<String> mimes) {
        this.mimes.clear();
        if (mimes != null) {
            this.mimes.addAll(mimes);
        }
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

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(Integer startDelay) {
        this.startDelay = startDelay;
    }

    public List<Integer> getProtocols() {
        return protocols;
    }

    public void setProtocols(List<Integer> protocols) {
        this.protocols = protocols;
    }

    public List<Integer> getBattr() {
        return battr;
    }

    public void setBattr(List<Integer> battr) {
        this.battr = battr;
    }

    public Integer getLinearity() {
        return linearity;
    }

    public void setLinearity(Integer linearity) {
        this.linearity = linearity;
    }

    public List<Banner> getCompanionad() {
        return companionad;
    }

    public void setCompanionad(List<Banner> companionad) {
        this.companionad = companionad;
    }

    public List<Integer> getApi() {
        return api;
    }

    public void setApi(List<Integer> api) {
        this.api = api;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    public void setVidId(String vidId) {
        this.vidId = vidId;
    }

    public String getVidId() {
        return vidId;
    }

    @Override
	public String toString() {
		return String.format("VideoAd [id=%s, videoadId=%s, bidfloorCurrency=%s, bidfloorPrice=%s, minDuration=%s, maxDuration=%s]", id, vidId, bidfloorCurrency, bidfloorPrice,
		        minDuration, maxDuration);
	}

    /**
     * This method is used to add type adapters for use in Gson.  If we want to have an enum index in the json but have the code use the enum, for example.
     * Currently we store the "String" value we want in the object, and the methods do the conversion, but when we change them to hold the enum instead, we need an adapter
     * to handle the conversion for us.
     * @param builder
     */
    public static void populateTypeAdapters(GsonBuilder builder) {
//        builder.registerTypeAdapter(ContentCategory.class, (JsonDeserializer<ContentCategory>) (json, typeOfT, context) -> ContentCategory.valueOf(json.getAsString()));
    }

}
