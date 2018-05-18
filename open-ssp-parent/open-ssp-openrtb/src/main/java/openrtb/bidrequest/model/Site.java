package openrtb.bidrequest.model;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import openrtb.tables.ContentCategory;

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

    private String ref;

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

	public List<ContentCategory> getCat() {
		ArrayList<ContentCategory> list = new ArrayList();
		for (String c : cat) {
			list.add(ContentCategory.convertValue(c));
		}
		return list;
	}

	public List<ContentCategory> getPagecat() {
		ArrayList<ContentCategory> list = new ArrayList();
		for (String c : pagecat) {
			list.add(ContentCategory.convertValue(c));
		}
		return list;
	}

	public List<ContentCategory> getSectioncat() {
		ArrayList<ContentCategory> list = new ArrayList();
		for (String c : sectioncat) {
			list.add(ContentCategory.convertValue(c));
		}
		return list;
	}

	public void setCat(final List<ContentCategory> cat) {
		this.cat.clear();
		if (cat != null) {
			for (ContentCategory c : cat) {
				if (c == null) {
					continue;
				}
				this.cat.add(c.getValue());
			}
		}
	}

	public void setPagecat(final List<ContentCategory> pagecat) {
        this.pagecat.clear();
        if (pagecat != null) {
            for (ContentCategory c : pagecat) {
				if (c == null) {
					continue;
				}
                this.pagecat.add(c.getValue());
            }
        }
	}

	public void setSectioncat(final List<ContentCategory> sectioncat) {
        this.sectioncat.clear();
        if (sectioncat != null) {
            for (ContentCategory c : sectioncat) {
				if (c == null) {
					continue;
				}
                this.sectioncat.add(c.getValue());
            }
        }
	}

	public void addCat(final ContentCategory cat) {
		if (cat != null) {
			this.cat.add(cat.getValue());
		}
	}

	public void addPagecat(final ContentCategory pagecat) {
		if (pagecat != null) {
			this.pagecat.add(pagecat.getValue());
		}
	}

	public void addSectioncat(final ContentCategory sectioncat) {
		if (sectioncat != null) {
			this.sectioncat.add(sectioncat.getValue());
		}
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

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
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

	/**
	 * This method is used to add type adapters for use in Gson.  If we want to have an enum index in the json but have the code use the enum, for example.
	 * Currently we store the "String" value we want in the object, and the methods do the conversion, but when we change them to hold the enum instead, we need an adapter
	 * to handle the conversion for us.
	 * @param builder
	 */
    public static void populateTypeAdapters(GsonBuilder builder) {
//        builder.registerTypeAdapter(ContentCategory.class, (JsonDeserializer<ContentCategory>) (json, typeOfT, context) -> ContentCategory.valueOf(json.getAsString()));
//TODO: BKS
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

		public Builder addCat(final ContentCategory cat) {
			site.addCat(cat);
			return this;
		}

		public Builder addPagecat(final ContentCategory pagecat) {
			site.addPagecat(pagecat);
			return this;
		}

		public Builder addSectioncat(final ContentCategory sectioncat) {
			site.addSectioncat(sectioncat);
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

		public Builder addCats(final List<ContentCategory> cats) {
			cats.forEach(c -> site.addCat(c));
			return this;
		}

		public Builder addPagecats(final List<ContentCategory> pagecats) {
            pagecats.forEach(c -> site.addPagecat(c));
			return this;
		}

		public Builder addSectioncats(final List<ContentCategory> sectioncats) {
            sectioncats.forEach(c -> site.addSectioncat(c));
			return this;
		}

		public Site build() {
			return site;
		}

	}

	@Override
	public String toString() {
		return String.format("Site [id=%s, name=%s, domain=%s, cat=%s, pagecat=%s, sectioncat=%s, page=%s, ext=%s]", id, name, domain, cat, pagecat, sectioncat, page, ext);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
