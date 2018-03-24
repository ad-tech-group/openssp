package openrtb.bidrequest.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Since;
import openrtb.tables.AuctionType;
import openrtb.tables.BooleanInt;
import openrtb.tables.ContentCategory;

/**
 * 
 * @author André Schmer
 *
 */
public final class BidRequest implements Cloneable {
	// required fields
	private String id;

	private Site site;

	private App app;

	private List<Impression> imp;

	private Device device;

	private User user;

	private List<String> cur;

	private List<String> badv;

	private List<String> bcat;

	@Since(2.3)
	private int test = BooleanInt.FALSE.getValue(); // default

	private int at = AuctionType.SECOND_PRICE.getValue(); // default

	private Integer tmax;

	private Object ext;

	public BidRequest() {
        cur = new ArrayList<>();
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

	public App getApp() {
		return app;
	}

	public void setApp(final App app) {
		this.app = app;
	}

	public List<Impression> getImp() {
		return imp;
	}

	public void setImp(final List<Impression> imp) {
	    this.imp.clear();
	    if (imp != null) {
            imp.forEach(i -> this.imp.add(i));
        }
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

	public List<String> getCur() {
		return cur;
	}

	public void setCur(final List<String> cur) {
	    this.cur.clear();
	    if (cur != null) {
	        cur.forEach(c -> this.cur.add(c));
        }
	}

    public void addCur(final String cur) {
        this.cur.add(cur);
    }

    public void setBadv(final List<String> badv) {
	    this.badv.clear();
	    if (badv != null) {
	        badv.forEach(b -> this.badv.add(b));
        }
	}

    public List<String> getBadv() {
        return badv;
    }

    public void addBadv(final String bad) {
		badv.add(bad);
	}

	public void setBcat(final List<ContentCategory> bcat) {
		this.bcat.clear();
		bcat.forEach(c -> this.bcat.add(c.getValue()));
	}

	public void addBcat(final ContentCategory bcat) {
		this.bcat.add(bcat.getValue());
	}

	// see product taxonomy -> "http://www.google.com/basepages/producttype/taxonomy.en-US.txt"
	public List<ContentCategory> getBcat() {
		ArrayList<ContentCategory> list = new ArrayList();
		bcat.forEach(c -> list.add(ContentCategory.convertValue(c)));
		return list;
	}

	public BooleanInt getTest() {
		return BooleanInt.convertValue(test);
	}

	public void setTest(final BooleanInt test) {
		this.test = test.getValue();
	}

	public AuctionType getAt() {
		return AuctionType.convertValue(at);
	}

	public void setAt(final AuctionType at) {
		this.at = at.getValue();
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
            if (cur != null) {
                final List<String> cloneCurList = new ArrayList<>(cur);
                clone.setCur(cloneCurList);
            }
            if (badv != null) {
                final List<String> cloneBadvList = new ArrayList<>(badv);
                clone.setBadv(cloneBadvList);
            }
            if (bcat != null) {
                clone.bcat = new ArrayList<>(bcat);
            }

			if (site!= null) {
				clone.setSite(site.clone());
			}
			if (app != null) {
				clone.setApp(app.clone());
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
		String siteString;
		if (site != null) {
			siteString = ", site="+site;
		} else {
			siteString = "";
		}
		String appString;
		if (app != null) {
			appString = ", app="+app;
		} else {
			appString = "";
		}
		return "BidRequest [id=" + id + siteString + appString + ", imp=" + imp + ", device=" + device + ", user=" + user + ", cur=" + cur + ", badv=" + badv + ", bcat=" + bcat + ", test=" + test
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

		public Builder setApp(final App app) {
			bidRequest.setApp(app);
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

		public Builder setTest(final BooleanInt test) {
			bidRequest.setTest(test);
			return this;
		}

		public Builder addCur(final String cur) {
			bidRequest.addCur(cur);
			return this;
		}

		public Builder setTmax(final int tmax) {
			bidRequest.setTmax(tmax);
			return this;
		}

		public Builder setAt(final AuctionType at) {
			bidRequest.setAt(at);
			return this;
		}

		public Builder addAllBadv(final List<String> allBadv) {
			bidRequest.setBadv(allBadv);
			return this;
		}

		public Builder addAllBcat(final List<ContentCategory> allBcat) {
			bidRequest.setBcat(allBcat);
			return this;
		}

		public Builder addBcat(final ContentCategory bcat) {
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
