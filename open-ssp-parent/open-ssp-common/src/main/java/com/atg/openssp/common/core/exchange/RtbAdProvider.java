package com.atg.openssp.common.core.exchange;

import com.atg.openssp.common.cache.CurrencyCache;
import com.atg.openssp.common.core.entry.SessionAgent;
import com.atg.openssp.common.demand.Supplier;
import com.atg.openssp.common.provider.AdProviderReader;
import com.atg.openssp.common.provider.AdProviderWriter;
import com.atg.openssp.common.provider.WinningNotifier;
import openrtb.bidresponse.model.SeatBid;

/**
 * @author André Schmer
 *
 */
public class RtbAdProvider implements AdProviderReader, AdProviderWriter {

	// the price in origin currency
	private float price;

	private boolean isValid = Boolean.FALSE;

	private String currency = "EUR";

	private SeatBid winningSeat;

	private Supplier supplier;

	private String dealid;

	private float exchangedCurrencyPrice;

	@Override
	public void perform(final SessionAgent agent) {
		new Thread(new WinningNotifier(winningSeat.bestBid().getNurl(), price, agent), "Winningnotifier").start();
	}

	@Override
	public float getPrice() {
		return price;
	}

	@Override
	public void setPrice(final float bidPrice) {
		price = bidPrice;
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
	public String buildResponse() {
		// TODO: implement
		// final String parsedMarkup =
		// supplier.getSupplierProperties().getParser()
		// .parseMarkup(winningSeat.getBid()[0].getAdm(), price);

		// final String content = new
		// ContentBuilderFacade().buildAllContent(parsedMarkup, agent,
		// ad.getGlobalID());
		// sb.append(content);

		return supplier.getShortName() + " " + exchangedCurrencyPrice + " "+ CurrencyCache.instance.getBaseCurrency();
	}

	@Override
	public String getVendorId() {
		return "RTB_" + supplier.getShortName();
	}

	public void setCurrency(final String cur) {
		currency = cur;
	}

	@Override
	public String getCurrrency() {
		return currency;
	}

	public void setWinningSeat(final SeatBid winningBid) {
		winningSeat = winningBid;
	}

	public SeatBid getWinningSeat() {
		return winningSeat;
	}

	public void setSupplier(final Supplier supplier) {
		this.supplier = supplier;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public String getDealId() {
		return dealid;
	}

	public void setDealId(final String dealId) {
		dealid = dealId;
	}

	@Override
	public void setExchangedCurrencyPrice(final float exchangedCurrencyPrice) {
		this.exchangedCurrencyPrice = exchangedCurrencyPrice;
	}

	@Override
	public float getExchangedCurrencyPrice() {
		return exchangedCurrencyPrice;
	}

	@Override
	public String getAdid() {
		return null;
	}

	public Builder getBuilder() {
		return new Builder();
	}

	static class Builder {
		private final RtbAdProvider rtbAdProvider;

		public Builder() {
			rtbAdProvider = new RtbAdProvider();
		}

		public Builder setIsValid(final boolean isValid) {
			rtbAdProvider.setIsValid(isValid);
			return this;
		}

		public Builder setPrice(final float bidPrice) {
			rtbAdProvider.setPrice(bidPrice);
			return this;
		}

		public Builder setExchangedCurrencyPrice(final float price) {
			rtbAdProvider.setExchangedCurrencyPrice(price);
			return this;
		}

		public Builder setSupplier(final Supplier supplier) {
			rtbAdProvider.setSupplier(supplier);
			return this;
		}

		public Builder setWinningSeat(final SeatBid seat) {
			rtbAdProvider.setWinningSeat(seat);
			return this;
		}

		public Builder setCurrency(final String currency) {
			rtbAdProvider.setCurrency(currency);
			return this;
		}

		public Builder setDealId(final String dealId) {
			rtbAdProvider.setDealId(dealId);
			return this;
		}

		RtbAdProvider build() {
			return rtbAdProvider;
		}
	}

}
