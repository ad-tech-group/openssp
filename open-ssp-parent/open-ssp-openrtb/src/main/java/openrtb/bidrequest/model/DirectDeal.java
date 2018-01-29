package openrtb.bidrequest.model;

/**
 * @author André Schmer
 *
 */
public class DirectDeal implements Cloneable {

	private String id;
	private float bidfloor = 0f;
	private String bidfloorcur = "USD";
	private String[] wseat;
	private String[] wadomain;
	private int at = 2;
	private Object ext;

	public DirectDeal() {}

	public DirectDeal(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public float getBidFloorprice() {
		return bidfloor;
	}

	public void setBidfloor(final float bidfloor) {
		this.bidfloor = bidfloor;
	}

	public String getBidFloorcurrrency() {
		return bidfloorcur;
	}

	public void setBidfloorcur(final String bidfloorcur) {
		this.bidfloorcur = bidfloorcur;
	}

	public String[] getWseat() {
		return wseat;
	}

	public void setWseat(final String[] wseat) {
		this.wseat = wseat;
	}

	public String[] getWadomain() {
		return wadomain;
	}

	public void setWadomain(final String[] wadomain) {
		this.wadomain = wadomain;
	}

	public int getAt() {
		return at;
	}

	public void setAt(final int at) {
		this.at = at;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	@Override
	public DirectDeal clone() {
		DirectDeal clone = null;
		try {
			clone = (DirectDeal) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new AssertionError("clone operation failed. " + getClass());
		}
		return clone;
	}

	public static class Builder {

		private final DirectDeal directDeal;

		public Builder() {
			directDeal = new DirectDeal();
		}

		public Builder setBidfloor(final float bidfloor) {
			directDeal.setBidfloor(bidfloor);
			return this;
		}

		public Builder setId(final String id) {
			directDeal.setId(id);
			return this;
		}

		public Builder setBidfloorcur(final String cur) {
			directDeal.setBidfloorcur(cur);
			return this;
		}

		public DirectDeal build() {
			return directDeal;
		}
	}

}
