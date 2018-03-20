package com.atg.openssp.common.demand;

import openrtb.bidrequest.model.Publisher;
import openrtb.bidrequest.model.Site;

/**
 * Optimized for handling VideoAd impressions with the behaviour of very individual requirements of the tag handler which binds to the SSP.
 * 
 * Use this class as data holder for the request params. Change the fields as you require.
 * 
 * @author André Schmer
 *
 */
public class TestParamValue extends ParamValue {

	private String isTest;

	public String getIsTest() {
		return isTest;
	}

	public void setIsTest(final String isTest) {
		this.isTest = isTest;
	}

	@Override
	public String toString() {
		return super.toString()+String.format("[isTest=%s]", isTest);
	}

}
