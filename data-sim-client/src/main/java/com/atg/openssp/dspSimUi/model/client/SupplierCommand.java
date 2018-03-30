package com.atg.openssp.dspSimUi.model.client;

import com.atg.openssp.common.demand.Supplier;

/**
 * @author Brian Sorensen
 */
public class SupplierCommand {
    private SupplierCommandType command;
    private Supplier supplier;


    public SupplierCommandType getCommand() {
        return command;
    }

    public void setCommand(SupplierCommandType command) {
        this.command = command;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

}
