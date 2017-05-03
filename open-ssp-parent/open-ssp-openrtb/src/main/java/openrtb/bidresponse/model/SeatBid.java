package openrtb.bidresponse.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author André Schmer
 * @see OpenRTB-API-Specification #section Seatbid
 * @version 2.1, 2.2, 2.3
 */
public class SeatBid implements Comparable<SeatBid>, SeatBidOrBuilder {

	private List<Bid> bid;

	// optional
	private String seat;
	private int group = 0;// “1” means impressions must be won-lost as a group;
	// default is “0”.
	private Object ext;

	private transient Bid bestBid;

	public SeatBid() {
		bid = new ArrayList<>();
	}

	public SeatBid(final List<Bid> bid) {
		this.bid = bid;
	}

	public List<Bid> getBid() {
		return bid;
	}

	public void setBid(final List<Bid> bid) {
		this.bid = bid;
	}

	@Override
	public String getSeat() {
		return seat;
	}

	public void setSeat(final String seat) {
		this.seat = seat;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(final int group) {
		this.group = group;
	}

	@Override
	public int compareTo(final SeatBid o) {
		if (o != null && (o.bestBid().getPrice() > bestBid().getPrice())) {
			return 1;
		}
		return -1;
	}

	/**
	 * Returns the best bid.
	 * <p>
	 * Are there more than one bid exists, the best bid will be choosen via sorting the bids by there price.
	 * 
	 * @return a Bid
	 */
	public Bid bestBid() {
		if (bestBid == null) {
			if (bid.size() > 1) {
				Collections.sort(bid);
			}
			bestBid = bid.get(0);
		}
		return bestBid;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	public Builder getBuilder() {
		return new Builder(this);
	}

	public static class Builder implements SeatBidOrBuilder {
		private final SeatBid seatBid;
		private final List<Bid.Builder> bidBuilderList = new ArrayList<>();

		public Builder() {
			seatBid = new SeatBid();
		}

		public Builder(final SeatBid seatBid) {
			for (final Bid bid : seatBid.bid) {
				bidBuilderList.add(bid.getBuilder());
			}
			this.seatBid = seatBid;
		}

		public Builder addBid(final Bid.Builder bidBuilder) {
			bidBuilderList.add(bidBuilder);
			seatBid.getBid().add(bidBuilder.build());
			return this;
		}

		public List<Bid.Builder> getBidBuilderList() {
			return bidBuilderList;
		}

		public SeatBid build() {
			return seatBid;
		}

		@Override
		public boolean hasSeat() {
			return seatBid.getSeat() != null;
		}

		@Override
		public String getSeat() {
			return seatBid.getSeat();
		}

		public void clearBid() {
			seatBid.bid.clear();
			bidBuilderList.clear();
		}
	}

	@Override
	public boolean hasSeat() {
		return false;
	}

	@Override
	public String toString() {
		return String.format("SeatBid [bid=%s, seat=%s, group=%s, ext=%s]", bid, seat, group, ext);
	}

}
