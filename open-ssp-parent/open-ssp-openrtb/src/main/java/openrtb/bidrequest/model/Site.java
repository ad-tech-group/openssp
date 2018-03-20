package openrtb.bidrequest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author André Schmer
 *
 */
public final class Site implements Cloneable {

	private String id;

	private String name;

	private String domain;

	private String page;

	// see product taxonomy -> "http://www.google.com/basepages/producttype/taxonomy.en-US.txt"
	private List<String> cat;

	private List<String> pagecat;

	private List<String> sectioncat;

	private Publisher publisher;
	private Object ext;

	public Site() {
		cat = new ArrayList<>();
		pagecat = new ArrayList<>();
		sectioncat = new ArrayList<>();
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

	public String getDomain() {
		return domain;
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

	public List<String> getPageCat() {
		return pagecat;
	}

	public void setPageCat(final List<String> pagecat) {
		this.pagecat = pagecat;
	}

	public void addPageCat(final String pagecat) {
		this.pagecat.add(pagecat);
	}

	public List<String> getSectionCat() {
		return sectioncat;
	}

	public void setSectionCat(final List<String> cat) {
		this.sectioncat = sectioncat;
	}

	public void addSectionCat(final String sectioncat) {
		this.sectioncat.add(sectioncat);
	}

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

	@Override
	public Site clone() {
		try {
			final Site clone = (Site) super.clone();
			return clone;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class Builder {

		private final Site site;

		public Builder() {
			site = new Site();
		}

		public Builder setId(final String id) {
			site.setId(id);
			return this;
		}

		public Builder setName(final String name) {
			site.setName(name);
			return this;
		}

		public Builder addCat(final String cat) {
			site.addCat(cat);
			return this;
		}

		public Builder addPageCat(final String pagecat) {
			site.addPageCat(pagecat);
			return this;
		}

		public Builder addSectionCat(final String sectioncat) {
			site.addSectionCat(sectioncat);
			return this;
		}

		public Builder setPage(final String page) {
			site.setPage(page);
			return this;
		}

		public Builder setExtension(final Object ext) {
			site.setExt(ext);
			return this;
		}

		public Builder setDomain(final String domain) {
			site.setDomain(domain);
			return this;
		}

		public Builder setPublisher(final Publisher publisher) {
			site.setPublisher(publisher);
			return this;
		}

		public Builder addCats(final List<Integer> cats) {
			cats.forEach(c -> site.addCat(String.valueOf(c)));
			return this;
		}

		public Builder addPageCat(final List<Integer> pagecats) {
			pagecats.forEach(c -> site.addPageCat(String.valueOf(c)));
			return this;
		}

		public Builder addSectionCat(final List<Integer> sectioncats) {
			sectioncats.forEach(c -> site.addSectionCat(String.valueOf(c)));
			return this;
		}

		public Site build() {
			return site;
		}

	}

	@Override
	public String toString() {
		return String.format("Site [id=%s, name=%s, domain=%s, cat=%s, page=%s, ext=%s]", id, name, domain, cat, page, ext);
	}

}
