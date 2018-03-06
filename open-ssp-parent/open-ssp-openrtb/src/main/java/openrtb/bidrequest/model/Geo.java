package openrtb.bidrequest.model;

/**
 * @author André Schmer
 *
 */
public final class Geo implements Cloneable {

	public static final int TYPE_GPS = 1;
	public static final int TYPE_IP = 2;
	public static final int TYPE_USER = 3;
	private Float lat;
	private Float lon;

	// ISO-3166-1 Alpha-3
	private String country;

	// ISO 3166-2
	private String region;

	private String city;

	private String zip;

	private String metro;

	private int type = TYPE_GPS;// default

	private Object ext;

	public Geo() {}

	public Float getLat() {
		return lat;
	}

	public void setLat(final Float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(final float lon) {
		this.lon = lon;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(final String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(final String zip) {
		this.zip = zip;
	}

	public String getMetro() {
		return metro;
	}

	public void setMetro(final String metro) {
		this.metro = metro;
	}

	public int getType() {
		return type;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public Object getExt() {
		return ext;
	}

	public Geo setExt(final Object ext) {
		this.ext = ext;
		return this;
	}

	@Override
	public Geo clone() {
		try {
			return (Geo) super.clone();
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class Builder {

		private final Geo geo;

		public Builder() {
			geo = new Geo();
		}

		public Builder setLat(final float lat) {
			geo.setLat(lat);
			return this;
		}

		public Builder setLon(final float lon) {
			geo.setLon(lon);
			return this;
		}

		public Builder setCountry(final String country) {
			geo.setCountry(country);
			return this;
		}

		public Builder setRegion(final String region) {
			geo.setRegion(region);
			return this;
		}

		public Builder setCity(final String city) {
			geo.setCity(city);
			return this;
		}

		public Builder setZip(final String zip) {
			geo.setZip(zip);
			return this;
		}

		public Builder setMetro(final String metro) {
			geo.setMetro(metro);
			return this;
		}

		public Builder setExtension(final Object ext) {
			geo.setExt(ext);
			return this;
		}

		public Builder setType(final int type) {
			geo.setType(type);
			return this;
		}

		public Geo build() {
			return geo;
		}

	}

	@Override
	public String toString() {
		return String.format("Geo [lat=%s, lon=%s, country=%s, region=%s, city=%s, zip=%s, metro=%s, ext=%s]", lat, lon, country, region, city, zip, metro, ext);
	}

}
