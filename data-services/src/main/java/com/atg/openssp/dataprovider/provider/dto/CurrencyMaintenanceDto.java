package com.atg.openssp.dataprovider.provider.dto;

import com.atg.openssp.common.core.broker.dto.CurrencyDto;

/**
 * @author Brian Sorensen
 */
public class CurrencyMaintenanceDto {
    private MaintenanceCommand command;
    private CurrencyDto dto;

    public MaintenanceCommand getCommand() {
        return command;
    }

    public void setCommand(MaintenanceCommand command) {
        this.command = command;
    }

    public CurrencyDto getDto() {
        return dto;
    }

    public void setDto(CurrencyDto dto) {
        this.dto = dto;
    }

}
