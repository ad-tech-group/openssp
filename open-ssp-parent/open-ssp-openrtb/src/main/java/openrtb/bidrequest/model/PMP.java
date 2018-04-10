package openrtb.bidrequest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author André Schmer
 *
 */
public class PMP implements Cloneable {

	private int private_auction=0;
	private List<DirectDeal> deals;
	private Object ext;

	public PMP() {}

	public int getPrivate_auction() {
		return private_auction;
	}

	public void setPrivate_auction(final int private_auction) {
		this.private_auction = private_auction;
	}

	public List<DirectDeal> getDeals() {
		return deals;
	}

	public void setDeals(final List<DirectDeal> deals) {
		this.deals = deals;
	}

	public void addDirectDeal(final DirectDeal directDeal) {
		if (deals == null) {
			deals = new ArrayList<>();
		}
		deals.add(directDeal);
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	@Override
	public PMP clone() {
		try {
			final PMP clone = (PMP) super.clone();
			if (deals != null) {
				final List<DirectDeal> cloneArray = new ArrayList<>(deals.size());
				deals.forEach(d -> cloneArray.add(d.clone()));
				clone.setDeals(cloneArray);
			}
			return clone;
		} catch (final CloneNotSupportedException e) {
			throw new AssertionError("clone operation failed. " + getClass());
		}
	}

	public static class Builder {
		private final PMP pmp;

		public Builder() {
			pmp = new PMP();
		}

		public Builder addDirectDeal(final DirectDeal.Builder directDeal) {
			pmp.addDirectDeal(directDeal.build());
			return this;
		}

		public PMP build() {
			return pmp;
		}

		public Builder setPrivateAuction(final int privateAuction) {
			pmp.setPrivate_auction(privateAuction);
			return this;
		}
	}

}
