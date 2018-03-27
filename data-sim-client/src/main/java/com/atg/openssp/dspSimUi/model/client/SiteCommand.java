package com.atg.openssp.dspSimUi.model.client;

import openrtb.bidrequest.model.Site;

/**
 * @author Brian Sorensen
 */
public class SiteCommand {
    private ServerCommandType type;
    private String id;
    private Site sb;


    public void setType(ServerCommandType type) {
        this.type = type;
    }

    public ServerCommandType getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSite(Site sb) {
        this.sb = sb;
    }

    public Site getSite() {
        return sb;
    }

}
