package com.atg.openssp.common.provider;

/**
 * @author André Schmer
 *
 */
public interface AdProviderWriter {

	void setPrice(float price);

	void setNormalizedPrice(float priceEur);

	void setIsValid(boolean valid);

}
