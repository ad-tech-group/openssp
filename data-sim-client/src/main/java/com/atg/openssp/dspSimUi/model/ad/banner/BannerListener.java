package com.atg.openssp.dspSimUi.model.ad.banner;

import com.atg.openssp.common.cache.dto.BannerAd;

public interface BannerListener {
    void added(BannerAd value);

    void removed(BannerAd bannerAd);
}
