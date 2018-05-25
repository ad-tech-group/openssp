package openrtb.bidrequest.model;

import openrtb.tables.PrivacyPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author André Schmer
 *
 */
public final class App implements Cloneable {

	private String id;

	private String name;

	private String bundle;

	private String domain;

	private String storeurl;

	// see product taxonomy -> "http://www.google.com/basepages/producttype/taxonomy.en-US.txt"
	private List<String> cat;

	private List<String> sectioncat;

	private List<String> pagecat;

	private String ver;

	private int privacypolicy = PrivacyPolicy.NO.getValue();

	private int paid;

	private String keywords;

	/*
	// see product taxonomy -> "http://www.google.com/basepages/producttype/taxonomy.en-US.txt"

	private String page;

	// see product taxonomy -> "http://www.google.com/basepages/producttype/taxonomy.en-US.txt"
	private List<String> cat;

	private Publisher publisher;
	*/
	private Object ext;

	public App() {
		cat = new ArrayList<>();
		sectioncat = new ArrayList<>();
		pagecat = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getBundle() {
		return bundle;
	}

	public void setBundle(final String bundle) {
		this.bundle = bundle;
	}

	public void setDomain(final String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public void setStoreurl(final String storeurl) {
		this.storeurl = storeurl;
	}

	public String getStoreurl() {
		return storeurl;
	}

	public void setPaid(final int paid) {
		this.paid = paid;
	}

	public int getPaid() {
		return paid;
	}

	public void setKeywords(final String keywords) {
		this.keywords = keywords;
	}

	public String getKeywords() {
		return keywords;
	}

	public List<String> getCat() {
		return cat;
	}

	public void setCat(final List<String> cat) {
		this.cat = cat;
	}

	public void addCat(final String cat) {
		this.cat.add(cat);
	}

	public List<String> getSectionCat() {
		return sectioncat;
	}

	public void setSectionCat(final List<String> sectioncat) {
		this.sectioncat = sectioncat;
	}

	public void addSectionCat(final String sectioncat) {
		this.sectioncat.add(sectioncat);
	}

	public List<String> getPageCat() {
		return pagecat;
	}

	public void setPageCat(final List<String> pagecat) {
		this.pagecat = pagecat;
	}

	public void addPageCat(final String pagecat) {
		this.pagecat.add(pagecat);
	}

	/*
	public String getPage() {
		return page;
	}

	public void setPage(final String page) {
		this.page = page;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	public void setDomain(final String domain) {
		this.domain = domain;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(final Publisher publisher) {
		this.publisher = publisher;
	}
	*/

	@Override
	public App clone() {
		try {
			final App clone = (App) super.clone();
			return clone;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class Builder {

		private final App site;

		public Builder() {
			site = new App();
		}

		public Builder setId(final String id) {
			site.setId(id);
			return this;
		}

		public Builder setName(final String name) {
			site.setName(name);
			return this;
		}

		public Builder setBundle(final String bundle) {
			site.setBundle(bundle);
			return this;
		}

		public Builder setDomain(final String domain) {
			site.setDomain(domain);
			return this;
		}

		public Builder setStoreurl(final String storeurl) {
			site.setStoreurl(storeurl);
			return this;
		}

		public Builder setPaid(final int paid) {
			site.setPaid(paid);
			return this;
		}

		public Builder setKeywords(final String keywords) {
			site.setKeywords(keywords);
			return this;
		}

		public Builder addCat(final String cat) {
			site.addCat(cat);
			return this;
		}

		public Builder addSectionCat(final String sectioncat) {
			site.addSectionCat(sectioncat);
			return this;
		}

		public Builder addPageCat(final String pagecat) {
			site.addPageCat(pagecat);
			return this;
		}

		/*
		public Builder setPage(final String page) {
			site.setPage(page);
			return this;
		}

		public Builder setExtension(final Object ext) {
			site.setExt(ext);
			return this;
		}

		public Builder setPublisher(final Publisher publisher) {
			site.setPublisher(publisher);
			return this;
		}
		*/

		public Builder addCats(final List<Integer> cats) {
			cats.forEach(c -> site.addCat(String.valueOf(c)));
			return this;
		}

		public Builder addSectionCat(final List<Integer> sectioncats) {
			sectioncats.forEach(c -> site.addSectionCat(String.valueOf(c)));
			return this;
		}

		public Builder addPagecat(final List<Integer> pagecat) {
			pagecat.forEach(c -> site.addPageCat(String.valueOf(c)));
			return this;
		}

		public App build() {
			return site;
		}

	}

	@Override
	public String toString() {
		return String.format("App [id=%s, name=%s, bundle=%s, domain=%s, cat=%s, ext=%s]", id, name, bundle, domain, cat, ext);
	}

}
