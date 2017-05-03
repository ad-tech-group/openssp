package openrtb.bidrequest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author André Schmer
 *
 */
public class Publisher implements Cloneable, Serializable {

	private static final long serialVersionUID = 3236080531637881159L;

	private String id;

	private String name;

	// see factual categories -> "http://developer.factual.com/working-with-categories/"
	private final List<String> cat;

	private String domain;

	private Object ext;

	public Publisher() {
		cat = new ArrayList<>();
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

	public void setDomain(final String domain) {
		this.domain = domain;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	public List<String> getCat() {
		return cat;
	}

	public void addCat(final String cat) {
		this.cat.add(cat);
	}

	public static class Builder {

		private final Publisher publisher;

		public Builder() {
			publisher = new Publisher();
		}

		public Builder setId(final String id) {
			publisher.setId(id);
			return this;
		}

		public Builder setName(final String name) {
			publisher.setName(name);
			return this;
		}

		public Builder setDomain(final String domain) {
			publisher.setDomain(domain);
			return this;
		}

		public Builder addCats(final List<Integer> cats) {
			cats.forEach(c -> publisher.addCat(String.valueOf(c)));
			return this;
		}

		public Builder setExt(final Object ext) {
			publisher.setExt(ext);
			return this;
		}

		public Publisher build() {
			return publisher;
		}

	}

}
