package io.freestar.ssp.common.demand;

import com.atg.openssp.common.demand.ParamValue;
import openrtb.bidrequest.model.App;
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
public class FreestarParamValue extends ParamValue {

	private Publisher publisher;
    private App app;

    public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}

    public void setApp(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
    }

	@Override
	public String toString() {
		return super.toString()+String.format("[publisher=%sm app=%s]", publisher, app);
	}

}
