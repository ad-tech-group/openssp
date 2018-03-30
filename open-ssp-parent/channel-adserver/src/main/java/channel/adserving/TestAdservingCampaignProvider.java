package channel.adserving;

import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.provider.AdProviderReader;
import com.atg.openssp.common.provider.AdProviderWriter;

/**
 * 
 * @author André Schmer
 *
 */
public class TestAdservingCampaignProvider extends AdservingCampaignProvider {

	private static final String currency = "EUR";

	private float cpm;

	private int adid;// sent by adserver

	private String vasturl;

	@Override
	public float getPrice() {
		return cpm;
	}

	@Override
	public void setPrice(final float bidPrice) {
		cpm = bidPrice;
	}

	@Override
	public float getExchangedCurrencyPrice() {
		return cpm * 1;
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
			return "Adserving_" + adid;
		}
		return null;
	}

	@Override
	public void setExchangedCurrencyPrice(final float priceEur) {
		cpm = priceEur;
	}

	@Override
	public String getAdid() {
		return String.valueOf(adid);
	}

	@Override
	public String toString() {
		return "AdservingCampaignProvider [isValid=" + isValid() + ", currency=" + currency + ", cpm=" + cpm + ", adid=" + adid + ", vasturl=" + vasturl + "]";
	}

}
