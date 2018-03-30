package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.dspSimUi.model.dsp.SimBidder;

/**
 * @author Brian Sorensen
 */
public class ServerCommand {
    private ServerCommandType type;
    private String id;
    private SimBidder sb;


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

    public void setSimBidder(SimBidder sb) {
        this.sb = sb;
    }

    public SimBidder getSimBidder() {
        return sb;
    }

}
