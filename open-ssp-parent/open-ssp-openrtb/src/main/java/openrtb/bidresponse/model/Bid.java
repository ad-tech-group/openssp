package openrtb.bidresponse.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Since;
import openrtb.tables.ContentCategory;

/**
 * @author André Schmer
 * @see //OpenRTB-API-Specification #section Bid Object
 * @version 2.1, 2.2, 2.3, 2.4
 * 
 */
public final class Bid implements Comparable<Bid> {

	// required
	private String id;
	private String impid;// corresponding impressionid of bidrequest
	private float price;
	private String adid;
	private String nurl;
	private String adm;

	// Advertiser domain for block list checking (e.g., “ford.com”).
	// This can be an array of for the case of rotating
	// creatives. Exchanges can mandate that only one domain is allowed
	private List<String> adomain;
	private String iurl;
	private String cid;
	private String crid;

	private List<Integer> attr;

	private int api;
	private int protocol;

	@Since(2.2)
	private String dealid;
	private int w;
	private int h;

	@Since(2.3)
	// private String bundle;
	private List<String> cat;


	private Object ext;

	public Bid() {
		adomain = new ArrayList<>();
		cat = new ArrayList<>();
		attr = new ArrayList<>();
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getImpid() {
		return impid;
	}

	public void setImpid(final String impid) {
		this.impid = impid;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(final float price) {
		this.price = price;
	}

	public String getNurl() {
		return nurl;
	}

	public void setNurl(final String nurl) {
		this.nurl = nurl;
	}

	public String getAdid() {
		return adid;
	}

	public void setAdid(final String adid) {
		this.adid = adid;
	}

	public String getAdm() {
		return adm;
	}

	public void setAdm(final String adm) {
		this.adm = adm;
	}

	public List<String> getAdomain() {
		return adomain;
	}

	public void setAdomain(final List<String> adomain) {
		this.adomain = adomain;
	}

	public void addAdomain(final String adomain) {
		this.adomain.add(adomain);
	}

	public String getIurl() {
		return iurl;
	}

	public void setIurl(final String iurl) {
		this.iurl = iurl;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(final String cid) {
		this.cid = cid;
	}

	public String getCrid() {
		return crid;
	}

	public void setCrid(final String crid) {
		this.crid = crid;
	}

	public List<Integer> getAttr() {
		return attr;
	}

	public void setAttr(final List<Integer> attr) {
		this.attr.clear();
		this.attr.addAll(attr);
	}

	public void addAttr(final Integer attr) {
		this.attr.add(attr);
	}

	public String getDealid() {
		return dealid;
	}

	public void setDealid(final String dealid) {
		this.dealid = dealid;
	}

	public int getH() {
		return h;
	}

	public void setH(final int h) {
		this.h = h;
	}

	public int getW() {
		return w;
	}

	public void setW(final int w) {
		this.w = w;
	}

	public List<ContentCategory> getCat() {
		ArrayList<ContentCategory> list = new ArrayList();
		cat.forEach(c -> list.add(ContentCategory.convertValue(c)));
		return list;
	}

	public void addAllCats(List<ContentCategory> cat) {
		cat.forEach(c -> addCat(c));
	}

	public void setCat(final List<ContentCategory> cat) {
		this.cat.clear();
		if (cat != null) {
			cat.forEach(c -> this.cat.add(c.getValue()));
		}
	}

	public void addCat(final ContentCategory cat) {
		if (cat != null) {
			this.cat.add(cat.getValue());
		}
	}

	public int getApi() {
		return api;
	}

	public void setApi(final int api) {
		this.api = api;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(final int protocol) {
		this.protocol = protocol;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	public List<String> getAdomainList() {
		return adomain;
	}

	public List<ContentCategory> getCatList() {
		ArrayList<ContentCategory> list = new ArrayList();
		cat.forEach(c -> list.add(ContentCategory.convertValue(c)));
		return list;
	}

	public List<Integer> getAttrList() {
		return attr;
	}

	public boolean hasId() {
		return id != null && id.length() != 0;
	}

	public boolean hasAdid() {
		return adid != null && adid.length() != 0;
	}

	public Builder getBuilder() {
		return new Builder(this);
	}

	@Override
	public int compareTo(final Bid o) {
		if (o != null && (o.getPrice() > getPrice())) {
			return 1;
		}
		return -1;
	}

	@Override
	public String toString() {
		return String.format("Bid [id=%s, impid=%s, price=%s, adid=%s, nurl=%s, adm=%s, adomain=%s, iurl=%s, cid=%s, crid=%s, attr=%s, dealid=%s, h=%s, w=%s, cat=%s, ext=%s]", id,
		        impid, price, adid, nurl, adm, adomain, iurl, cid, crid, attr, dealid, h, w, cat, ext);
	}

	public static class Builder {

		private final Bid bid;

		public Builder() {
			bid = new Bid();
		}

		public Builder(final Bid bid) {
			this.bid = bid;
		}

		public Builder setId(final String id) {
			bid.setId(id);
			return this;
		}

		public Builder setImpid(final String impId) {
			bid.setImpid(impId);
			return this;
		}

		public Builder setPrice(final float d) {
			bid.setPrice(d);
			return this;
		}

		public Builder addAllAdomain(final List<String> allDomain) {
			bid.setAdomain(allDomain);
			return this;
		}

		public Builder addAdomain(final String adomain) {
			bid.addAdomain(adomain);
			return this;
		}

		public Builder addAllAttr(final List<Integer> allAttr) {
			bid.setAttr(allAttr);
			return this;
		}

		public Builder setDealid(final String dealid) {
			bid.setDealid(dealid);
			return this;
		}

		public Builder addAllCat(final List<ContentCategory> allCat) {
			bid.setCat(allCat);
			return this;
		}

		public Builder addCat(final ContentCategory cat) {
			bid.addCat(cat);
			return this;
		}

		public Builder setAdm(final String adm) {
			bid.setAdm(adm);
			return this;
		}

		public Builder setNurl(final String nurl) {
			bid.setNurl(nurl);
			return this;
		}

		public String getImpid() {
			return bid.getImpid();
		}

		public String getId() {
			return bid.getId();
		}

		public List<String> getAdomainList() {
			return bid.getAdomainList();
		}

		public List<ContentCategory> getCatList() {
			return bid.getCatList();
		}

		public List<Integer> getAttrList() {
			return bid.getAttrList();
		}

		public boolean hasId() {
			return bid.hasId();
		}

		public boolean hasAdid() {
			return bid.hasAdid();
		}

		public Object getAdid() {
			return bid.getAdid();
		}

		public Bid build() {
			return bid;
		}

	}

}
