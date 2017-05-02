package channel.adserving;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.provider.AdProviderReader;
import com.atg.openssp.common.provider.AdProviderWriter;

/**
 * 
 * @author André Schmer
 *
 */
public class AdservingCampaignProvider implements AdProviderReader, AdProviderWriter {

	private boolean isValid = Boolean.TRUE;

	private final String currency = "EUR";

	private float cpm;

	private int adid;// sent by adserver

	private String vasturl;

	public AdservingCampaignProvider() {}

	@Override
	public float getPrice() {
		return cpm;
	}

	@Override
	public void setPrice(final float bidPrice) {
		cpm = bidPrice;
	}

	@Override
	public void setIsValid(final boolean valid) {
		isValid = valid;
	}

	@Override
	public boolean isValid() {
		return isValid;
	}

	@Override
	public float getPriceEur() {
		return cpm;
	}

	@Override
	public String getCurrrency() {
		return currency;
	}

	@Override
	public void perform(final SessionAgent agent) {
		// nothing to implement yet
	}

	@Override
	public String buildResponse() {
		return vasturl;
	}

	@Override
	public String getVendorId() {
		if (adid > 0) {
			return "Adserving_" + String.valueOf(adid);
		}
		return null;
	}

	public void setVendorId(final String vendorid) {}

	@Override
	public void setPriceEur(final float priceEur) {
		cpm = priceEur;
	}

	@Override
	public String getAdid() {
		return String.valueOf(adid);
	}

	@Override
	public String toString() {
		return "AdservingCampaignProvider [isValid=" + isValid + ", currency=" + currency + ", cpm=" + cpm + ", adid=" + adid + ", vasturl=" + vasturl + "]";
	}

}
