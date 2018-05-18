package com.atg.openssp.dspSimUi.model.client;

import openrtb.bidrequest.model.Pricelayer;

/**
 * @author Brian Sorensen
 */
public class PricelayerCommand {
    private PricelayerCommandType command;
    private Pricelayer pricelayer;


    public PricelayerCommandType getCommand() {
        return command;
    }

    public void setCommand(PricelayerCommandType command) {
        this.command = command;
    }

    public Pricelayer getPricelayer() {
        return pricelayer;
    }

    public void setPricelayer(Pricelayer pricelayer) {
        this.pricelayer = pricelayer;
    }

}
