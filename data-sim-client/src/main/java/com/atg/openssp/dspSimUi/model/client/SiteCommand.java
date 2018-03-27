package com.atg.openssp.dspSimUi.model.client;

import openrtb.bidrequest.model.Site;

/**
 * @author Brian Sorensen
 */
public class SiteCommand {
    private SiteCommandType command;
    private Site site;


    public SiteCommandType getCommand() {
        return command;
    }

    public void setCommand(SiteCommandType command) {
        this.command = command;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

}
