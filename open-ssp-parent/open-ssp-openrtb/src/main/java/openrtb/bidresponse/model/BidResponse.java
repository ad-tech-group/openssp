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
public class BidResponse {

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

	public BidResponse(final Builder builder) {
		this();
		id = builder.id;
		cur = builder.currency;
		// ext = builder.extension;
		bidid = builder.bidid;
		customdata = builder.customdata;
		nbr = builder.nbr;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getCur() {
		return cur;
	}

	public void setCur(final String cur) {
		this.cur = cur;
	}

	public List<SeatBid> getSeatbid() {
		return seatbid;
	}

	public void setSeatbid(final List<SeatBid> seatbid) {
		this.seatbid = seatbid;
	}

	public void addSeatBid(final SeatBid seatBid) {
		seatbid.add(seatBid);
	}

	public String getBidid() {
		return bidid;
	}

	public void setBidid(final String bidid) {
		this.bidid = bidid;
	}

	public String getCustomdata() {
		return customdata;
	}

	public void setCustomdata(final String customdata) {
		this.customdata = customdata;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	public SeatBid getWinningSeat() {
		return winningSeat;
	}

	public void setWinningSeat(final SeatBid bid) {
		winningSeat = bid;
	}

	public int getNbr() {
		return nbr;
	}

	public void setNbr(final int nbr) {
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

	public static final class Builder {

		private BidResponse bidResponse;

		private final List<SeatBid.Builder> seatBidBuilderList = new ArrayList<>();

		private String id;

		private String currency;

		private String bidid;

		private Object extension;

		private String customdata;

		private int nbr = -1;

		public Builder() {}

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
			this.id = id;
			return this;
		}

		public Builder setCur(final String cur) {
			currency = cur;
			return this;
		}

		public Builder setNbr(final int nbr) {
			this.nbr = nbr;
			return this;
		}

		public Builder setBidid(final String bidid) {
			this.bidid = bidid;
			return this;
		}

		public Builder setCustomdata(final String customdata) {
			this.customdata = customdata;
			return this;
		}

		public Builder setExt(final Object ext) {
			extension = ext;
			return this;
		}

		public BidResponse build() {
			if (bidResponse == null) {
				bidResponse = new BidResponse(this);
			} else {

				if (id != null) {
					bidResponse.setId(id);
				}

				if (bidid != null) {
					bidResponse.setBidid(bidid);
				}

				if (currency != null) {
					bidResponse.setCur(currency);
				}

				if (extension != null) {
					bidResponse.setExt(extension);
				}

				if (customdata != null) {
					bidResponse.setCustomdata(customdata);
				}

				if (nbr != -1) {
					bidResponse.setNbr(nbr);
				}

				bidResponse.getSeatbid().clear();
			}
			seatBidBuilderList.forEach((b) -> bidResponse.addSeatBid(b.build()));

			return bidResponse;
		}
	}

}
