package com.atg.openssp.common.provider;

import com.atg.openssp.common.core.entry.SessionAgent;

/**
 * 
 * @author André Schmer
 *
 */
public interface AdProviderReader {

	float getPrice();

	float getNormalizedPrice();

	String getCurrrency();

	void perform(SessionAgent agent);

	String buildResponse();

	String getVendorId();

	boolean isValid();

	String getAdid();

}
