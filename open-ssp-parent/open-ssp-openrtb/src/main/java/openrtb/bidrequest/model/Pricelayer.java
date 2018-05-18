package openrtb.bidrequest.model;

import com.google.gson.GsonBuilder;

/**
 * @author André Schmer
 *
 */
public class Pricelayer {

	private String siteid;

	private float bidfloor;

	private String currency;

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(final String siteid) {
		this.siteid = siteid;
	}

	public float getBidfloor() {
		return bidfloor;
	}

	public void setBidfloor(final float bidfloor) {
		this.bidfloor = bidfloor;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(final String currency) {
		this.currency = currency;
	}

	public static void populateTypeAdapters(GsonBuilder builder) {
	}

	@Override
    public String toString() {
	    return siteid+"-"+currency+":"+bidfloor;
    }
}
