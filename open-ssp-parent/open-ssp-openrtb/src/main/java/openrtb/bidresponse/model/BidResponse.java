package openrtb.bidresponse.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Since;

/**
 * @author André Schmer
 * @see OpenRTB-API-Specification #section {@code Bid Response} Object
 * @version 2.1, 2.2, 2.3, 2.4
 * 
 */
public final class BidResponse {

	// required
	// corresponds to requestid {@see BidRequest.id}
	private String id;
	private List<SeatBid> seatbid;
	private String bidid;
	private String cur = "USD";

	// optional
	private String customdata;
	private Object ext;

	@Since(2.2)
	private int nbr = -1;// Reason for not bidding, -1 all ok

	// working field
	private transient SeatBid winningSeat;

	public BidResponse() {
		seatbid = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	protected void setId(final String id) {
		this.id = id;
	}

	public String getCur() {
		return cur;
	}

	protected void setCur(final String cur) {
		this.cur = cur;
	}

	public List<SeatBid> getSeatbid() {
		return seatbid;
	}

	protected void setSeatbid(final List<SeatBid> seatbid) {
		this.seatbid = seatbid;
	}

	protected void addSeatBid(final SeatBid seatBid) {
		seatbid.add(seatBid);
	}

	public String getBidid() {
		return bidid;
	}

	protected void setBidid(final String bidid) {
		this.bidid = bidid;
	}

	public String getCustomdata() {
		return customdata;
	}

	protected void setCustomdata(final String customdata) {
		this.customdata = customdata;
	}

	public Object getExt() {
		return ext;
	}

	protected void setExt(final Object ext) {
		this.ext = ext;
	}

	public SeatBid getWinningSeat() {
		return winningSeat;
	}

	protected void setWinningSeat(final SeatBid bid) {
		winningSeat = bid;
	}

	public int getNbr() {
		return nbr;
	}

	protected void setNbr(final int nbr) {
		this.nbr = nbr;
	}

	@Override
	public String toString() {
		return String.format("BidResponse [id=%s, seatbid=%s, bidid=%s, customdata=%s, ext=%s, cur=%s, nbr=%s]", id, seatbid, bidid, customdata, ext, cur, nbr);
	}

	/**
	 * Returns a new Builder Object with a copy of this BidResponse Object.
	 * 
	 * @return a new BidResponse.Builder
	 */
	public Builder getBuilder() {
		return new Builder(this);
	}

	public static class Builder {

		private final BidResponse bidResponse;

		private final List<SeatBid.Builder> seatBidBuilderList = new ArrayList<>();

		public Builder() {
			bidResponse = new BidResponse();
		}

		public Builder(final BidResponse bidResponse) {
			for (final SeatBid seatBid : bidResponse.seatbid) {
				seatBidBuilderList.add(seatBid.getBuilder());
			}
			this.bidResponse = bidResponse;
		}

		public Builder addSeatbidBuilder(final SeatBid.Builder seatBidBuilder) {
			seatBidBuilderList.add(seatBidBuilder);
			return this;
		}

		public List<SeatBid.Builder> getSeatbidBuilderList() {
			return seatBidBuilderList;
		}

		public Builder setId(final String id) {
			bidResponse.setId(id);
			return this;
		}

		public Builder setSeatbid(final List<SeatBid> seatbid) {
			bidResponse.setSeatbid(seatbid);
			return this;
		}

		public Builder setCur(final String cur) {
			bidResponse.setCur(cur);
			return this;
		}

		public Builder setNbr(final int nbr) {
			bidResponse.setNbr(nbr);
			return this;
		}

		public Builder setBidid(final String bidid) {
			bidResponse.setBidid(bidid);
			return this;
		}

		public Builder setCustomdata(final String customdata) {
			bidResponse.setCustomdata(customdata);
			return this;
		}

		public Builder setExt(final Object ext) {
			bidResponse.setExt(ext);
			return this;
		}

		public BidResponse build() {
			seatBidBuilderList.forEach((b) -> bidResponse.addSeatBid(b.build()));
			return bidResponse;
		}
	}

}
