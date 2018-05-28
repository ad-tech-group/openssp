package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.common.cache.dto.VideoAd;

/**
 * @author Brian Sorensen
 */
public class VideoAdCommand {
    private VideoAdCommandType command;
    private VideoAd videoAd;


    public VideoAdCommandType getCommand() {
        return command;
    }

    public void setCommand(VideoAdCommandType command) {
        this.command = command;
    }

    public VideoAd getVideoAd() {
        return videoAd;
    }

    public void setVideoAd(VideoAd videoAd) {
        this.videoAd = videoAd;
    }

}
