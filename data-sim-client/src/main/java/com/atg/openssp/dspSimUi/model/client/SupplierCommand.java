package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.dspSimUi.model.dsp.SimBidder;

/**
 * @author Brian Sorensen
 */
public class SupplierCommand {
    private ServerCommandType type;
    private long id;
    private Supplier sb;


    public void setType(ServerCommandType type) {
        this.type = type;
    }

    public ServerCommandType getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setSupplier(Supplier sb) {
        this.sb = sb;
    }

    public Supplier getSupplier() {
        return sb;
    }

}
