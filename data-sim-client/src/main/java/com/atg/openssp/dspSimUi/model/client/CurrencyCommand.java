package com.atg.openssp.dspSimUi.model.client;


import com.atg.openssp.common.core.broker.dto.CurrencyDto;
import com.atg.openssp.common.model.EurRef;

/**
 * @author Brian Sorensen
 */
public class CurrencyCommand {
    private CurrencyCommandType command;
    private CurrencyDto currency;


    public CurrencyCommandType getCommand() {
        return command;
    }

    public void setCommand(CurrencyCommandType command) {
        this.command = command;
    }

    public CurrencyDto getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDto currency) {
        this.currency = currency;
    }

}
