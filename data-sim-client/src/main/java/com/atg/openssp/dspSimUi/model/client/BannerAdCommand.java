package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.common.cache.dto.BannerAd;

/**
 * @author Brian Sorensen
 */
public class BannerAdCommand {
    private BannerAdCommandType command;
    private BannerAd bannerAd;


    public BannerAdCommandType getCommand() {
        return command;
    }

    public void setCommand(BannerAdCommandType command) {
        this.command = command;
    }

    public BannerAd getBannerAd() {
        return bannerAd;
    }

    public void setBannerAd(BannerAd bannerAd) {
        this.bannerAd = bannerAd;
    }

}
