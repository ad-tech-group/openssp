package com.atg.openssp.dataprovider.provider.dto;

import openrtb.bidrequest.model.Site;

/**
 * @author Brian Sorensen
 */
public class SiteMaintenanceDto {
    private MaintenanceCommand command;
    private Site site;

    public MaintenanceCommand getCommand() {
        return command;
    }

    public void setCommand(MaintenanceCommand command) {
        this.command = command;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

}
