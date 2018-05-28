package com.atg.openssp.common.core.broker.dto;

import com.atg.openssp.common.cache.dto.BannerAd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brian Sorensen
 *
 */
public class BannerAdDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<BannerAd> bannerAds = new ArrayList<>();

    public BannerAdDto() {
    }

    public List<BannerAd> getBannerAds() {
        ArrayList<BannerAd> list = new ArrayList();
        list.addAll(bannerAds);
        return list;
    }

    public void setBannerAds(final List<BannerAd> bannerAds) {
        this.bannerAds.clear();
        if (bannerAds != null) {
            this.bannerAds.addAll(bannerAds);
    }
    }

    @Override
	public String toString() {
        return String.format("BannerAdsDto [bannerAds=%s]", bannerAds);
    }

}