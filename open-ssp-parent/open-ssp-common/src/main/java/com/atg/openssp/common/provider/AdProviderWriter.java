package com.atg.openssp.common.provider;

/**
 * @author André Schmer
 *
 */
public interface AdProviderWriter {

	void setPrice(float price);

	void setExchangedCurrencyPrice(float price);

	void setIsValid(boolean valid);

}
