package com.atg.openssp.common.cache.dto;

import com.google.gson.annotations.SerializedName;
import openrtb.bidrequest.model.Banner;
import openrtb.tables.CreativeAttribute;
import openrtb.tables.ApiFramework;
import openrtb.tables.VideoBidResponseProtocol;
import openrtb.tables.VideoLinearity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        mimes = new ArrayList<>();
        protocols = new ArrayList<>();
        battr = new ArrayList<>();
        companionad = new ArrayList<>();
        api = new ArrayList<>();
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

    public List<VideoBidResponseProtocol> getProtocols() {
        ArrayList<VideoBidResponseProtocol> list = new ArrayList();
        for (int p : protocols) {
            list.add(VideoBidResponseProtocol.convert(p));
        }
        return list;
    }

    public void setProtocols(List<VideoBidResponseProtocol> protocols) {
        this.protocols.clear();
        if (protocols != null) {
            for (VideoBidResponseProtocol p : protocols) {
                this.protocols.add(p.getValue());
            }
        }
    }

    public List<CreativeAttribute> getBattr() {
        ArrayList<CreativeAttribute> list = new ArrayList();
        for (int p : battr) {
            list.add(CreativeAttribute.convertValue(p));
        }
        return list;
    }

    public void setBattr(List<CreativeAttribute> battr) {
        this.battr.clear();
        if (battr != null) {
            for (CreativeAttribute p : battr) {
                this.battr.add(p.getValue());
            }
        }
    }

    public VideoLinearity getLinearity() {
        if (linearity == null) {
            return null;
        } else {
            return VideoLinearity.convertValue(linearity);
        }
    }

    public void setLinearity(VideoLinearity linearity) {
        if (linearity == null) {
            this.linearity = null;
        } else {
            this.linearity = linearity.getValue();
        }
    }

    public List<Banner> getCompanionad() {
        if (companionad.size() == 0) {
            return null;
        } else {
            ArrayList<Banner> list = new ArrayList();
            list.addAll(companionad);
            return list;
        }
    }

    public void setCompanionad(List<Banner> companionad) {
        this.companionad.clear();
        if (companionad != null) {
            this.companionad.addAll(companionad);
        }
    }

    public void addApi(ApiFramework api) {
        if (api != null) {
            this.api.add(api.getValue());
        }
    }

    public List<ApiFramework> getApi() {
        ArrayList<ApiFramework> list = new ArrayList<>();
        for (int p : api) {
            list.add(ApiFramework.convertValue(p));
        }
        return list;
    }

    public void setApi(List<ApiFramework> api) {
        this.api.clear();
        if (api != null) {
            for (ApiFramework p : api) {
                this.api.add(p.getValue());
            }
        }
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

}
