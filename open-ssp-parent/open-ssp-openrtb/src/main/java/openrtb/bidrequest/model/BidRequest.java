package openrtb.bidrequest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Since;

/**
 * 
 * @author André Schmer
 *
 */
public final class BidRequest implements Cloneable, Serializable {

	private static final long serialVersionUID = -5776508841357482195L;

	// required fields
	private String id;

	private Site site;

	private List<Impression> imp;

	private Device device;

	private User user;

	private List<String> badv;

	private List<String> bcat;

	@Since(2.3)
	private int test = 0;// default

	private int at = 2;// default

	private Integer tmax;

	private Object ext;

	public BidRequest() {
		imp = new ArrayList<>();
		badv = new ArrayList<>();
		bcat = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(final Site site) {
		this.site = site;
	}

	public List<Impression> getImp() {
		return imp;
	}

	public void setImp(final List<Impression> imp) {
		this.imp = imp;
	}

	public void addImp(final Impression impression) {
		imp.add(impression);
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(final Device device) {
		this.device = device;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public void setBadv(final List<String> badv) {
		this.badv = badv;
	}

	public void addBadv(final String bad) {
		badv.add(bad);
	}

	public void setBcat(final List<String> bcat) {
		this.bcat = bcat;
	}

	public void addBcat(final String bcat) {
		this.bcat.add(bcat);
	}

	public List<String> getBadv() {
		return badv;
	}

	// see product taxonomy -> "http://www.google.com/basepages/producttype/taxonomy.en-US.txt"
	public List<String> getBcat() {
		return bcat;
	}

	public int getTest() {
		return test;
	}

	public void setTest(final int test) {
		this.test = test;
	}

	public int getAt() {
		return at;
	}

	public void setAt(final int at) {
		this.at = at;
	}

	public int getTmax() {
		return tmax;
	}

	public void setTmax(final int tmax) {
		this.tmax = tmax;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	public List<Impression> getImpList() {
		return imp;
	}

	@Override
	public BidRequest clone() {
		try {
			final BidRequest clone = (BidRequest) super.clone();
			if (imp != null) {
				final List<Impression> cloneImpList = new ArrayList<>(imp);
				clone.setImp(cloneImpList);
			}

			if (site != null) {
				clone.setSite(site.clone());
			}
			if (device != null) {
				clone.setDevice(device.clone());
			}
			if (user != null) {
				clone.setUser(user.clone());
			}
			return clone;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Builder getBuilder() {
		return new Builder(this);
	}

	@Override
	public String toString() {
		return "BidRequest [id=" + id + ", site=" + site + ", imp=" + imp + ", device=" + device + ", user=" + user + ", badv=" + badv + ", bcat=" + bcat + ", test=" + test
		        + ", at=" + at + ", tmax=" + tmax + ", ext=" + ext + "]";
	}

	public static class Builder {

		private final BidRequest bidRequest;

		public Builder() {
			bidRequest = new BidRequest();
		}

		public Builder(final BidRequest bidRequest) {
			this.bidRequest = bidRequest.clone();
		}

		public Builder setId(final String id) {
			bidRequest.setId(id);
			return this;
		}

		public Builder setSite(final Site site) {
			bidRequest.setSite(site);
			return this;
		}

		public Builder addImp(final Impression imp) {
			bidRequest.addImp(imp);
			return this;
		}

		public Builder setDevice(final Device device) {
			bidRequest.setDevice(device);
			return this;
		}

		public Builder setUser(final User user) {
			bidRequest.setUser(user);
			return this;
		}

		public Builder setExtension(final Object ext) {
			bidRequest.setExt(ext);
			return this;
		}

		public Builder addBadv(final String badv) {
			bidRequest.addBadv(badv);
			return this;
		}

		public Builder setTest(final int test) {
			bidRequest.setTest(test);
			return this;
		}

		public Builder setTmax(final int tmax) {
			bidRequest.setTmax(tmax);
			return this;
		}

		public Builder setAt(final int at) {
			bidRequest.setAt(at);
			return this;
		}

		public Builder addAllBadv(final List<String> allBadv) {
			bidRequest.setBadv(allBadv);
			return this;
		}

		public Builder addAllBcat(final List<String> allBcat) {
			bidRequest.setBcat(allBcat);
			return this;
		}

		public Builder addBcat(final String bcat) {
			bidRequest.addBcat(bcat);
			return this;
		}

		public Builder addImp(final Impression.Builder impressionBuilder) {
			// listOfImpBuilder.add(impressionBuilder);
			bidRequest.getImpList().add(impressionBuilder.build());
			return this;
		}

		public BidRequest build() {
			return bidRequest;
		}

	}

}
