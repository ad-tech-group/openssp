package com.atg.openssp.dataprovider.provider.dto;

import com.atg.openssp.common.cache.dto.VideoAd;

/**
 * @author Brian Sorensen
 */
public class VideoAdMaintenanceDto {
    private MaintenanceCommand command;
    private VideoAd videoAd;

    public MaintenanceCommand getCommand() {
        return command;
    }

    public void setCommand(MaintenanceCommand command) {
        this.command = command;
    }

    public VideoAd getVideoAd() {
        return videoAd;
    }

    public void setVideoAd(VideoAd videoAd) {
        this.videoAd = videoAd;
    }

}
