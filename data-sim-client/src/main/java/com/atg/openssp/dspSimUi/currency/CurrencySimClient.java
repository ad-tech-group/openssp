package com.atg.openssp.dspSimUi.currency;

import com.atg.openssp.dspSimUi.model.ModelException;
import com.atg.openssp.dspSimUi.model.currency.CurrencyModel;
import com.atg.openssp.dspSimUi.view.currency.CurrencyView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Brian Sorensen
 */
public class CurrencySimClient {
    private static final Logger log = LoggerFactory.getLogger(CurrencySimClient.class);
    private final CurrencyView currencyView;
    private CurrencyModel currencyModel;

    public CurrencySimClient() throws ModelException {
        currencyModel = new CurrencyModel();
        currencyView = new CurrencyView(currencyModel);
    }

    public void start() {
        currencyView.start();
        currencyModel.start();
    }

    public static void main(String[] args) {
        try {
            CurrencySimClient sim = new CurrencySimClient();
            sim.start();
        } catch (ModelException e) {
            log.error(e.getMessage(), e);
        }
    }

}