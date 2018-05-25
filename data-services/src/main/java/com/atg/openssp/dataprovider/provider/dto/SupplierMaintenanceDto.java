package com.atg.openssp.dataprovider.provider.dto;

import com.atg.openssp.common.demand.Supplier;

/**
 * @author Brian Sorensen
 */
public class SupplierMaintenanceDto {
    private MaintenanceCommand command;
    private Supplier supplier;

    public MaintenanceCommand getCommand() {
        return command;
    }

    public void setCommand(MaintenanceCommand command) {
        this.command = command;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

}
