package com.atg.openssp.dataprovider.provider.dto;

import openrtb.bidrequest.model.Pricelayer;

/**
 * @author Brian Sorensen
 */
public class PricelayerMaintenanceDto {
    private MaintenanceCommand command;
    private Pricelayer pricelayer;

    public MaintenanceCommand getCommand() {
        return command;
    }

    public void setCommand(MaintenanceCommand command) {
        this.command = command;
    }

    public Pricelayer getPricelayer() {
        return pricelayer;
    }

    public void setPricelayer(Pricelayer pricelayer) {
        this.pricelayer = pricelayer;
    }

}
