package com.atg.openssp.dataprovider.provider.dto;

import com.atg.openssp.common.cache.dto.BannerAd;

/**
 * @author Brian Sorensen
 */
public class BannerAdMaintenanceDto {
    private MaintenanceCommand command;
    private BannerAd bannerAd;

    public MaintenanceCommand getCommand() {
        return command;
    }

    public void setCommand(MaintenanceCommand command) {
        this.command = command;
    }

    public BannerAd getBannerAd() {
        return bannerAd;
    }

    public void setBannerAd(BannerAd bannerAd) {
        this.bannerAd = bannerAd;
    }

}
