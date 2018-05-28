package com.atg.openssp.common.demand;

import com.atg.openssp.common.cache.dto.BannerAd;

/**
 * Optimized for handling Banner impressions with the behaviour of very individual requirements of the tag handler which binds to the SSP.
 * 
 * Use this class as data holder for the request params. Change the fields as you require.
 * 
 * @author Brian Sorensen
 *
 */
public class BannerObjectParamValue extends ParamValue {

    private BannerAd bannerad;

    public BannerAd getBannerad() {
        return bannerad;
    }

    public void setBannerad(BannerAd bannerad) {
        this.bannerad = bannerad;
    }

}
