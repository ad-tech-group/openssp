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

//	private Publisher publisher;
    private App app;
    private String callback;
    private String callbackUid;
    private String psa;
    private String id;
    private String size;
    private String promoSizes;
    private String referrer;

//    public Publisher getPublisher() {
//		return publisher;
//	}

//	public void setPublisher(final Publisher publisher) {
//		this.publisher = publisher;
//	}

    public void setApp(App app) {
        this.app = app;
    }

    public App getAppX() {
        return app;
    }

	@Override
	public String toString() {
//        return super.toString()+String.format("[publisher=%sm app=%s]", publisher, app);
		return super.toString()+String.format("[app=%s]", app);
	}

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallbackUid(String callbackUid) {
        this.callbackUid = callbackUid;
    }

    public String getCallbackUid() {
        return callbackUid;
    }

    public void setPsa(String psa) {
        this.psa = psa;
    }

    public String getPsa() {
        return psa;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setPromoSizes(String promoSizes) {
        this.promoSizes = promoSizes;
    }

    public String getPromoSizes() {
        return promoSizes;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getReferrer() {
        return referrer;
    }
}
