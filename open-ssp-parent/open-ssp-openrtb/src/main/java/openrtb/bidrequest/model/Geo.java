package openrtb.bidrequest.model;

import com.google.gson.annotations.Since;
import openrtb.tables.GeoType;
import openrtb.tables.IpServiceType;

/**
 * @author André Schmer
 *
 */
public final class Geo implements Cloneable {

	@Since(2.0)
	private Float lat;

	@Since(2.0)
	private Float lon;

	@Since(2.0)
	private int type = GeoType.GPS.getValue(); // default

	// Estimated locaton accuracy in meters; dericed from a device's location service (i.e type-1)
	@Since(2.4)
	private Integer accuracy;

	// number of seconds since this geolocation fix was established
	@Since(2.4)
	private Integer lastfix;

	// for type 2 - list 5.21
	@Since(2.4)
	private Integer ipservice;

	// ISO-3166-1 Alpha-3
	@Since(2.0)
	private String country;

	// ISO 3166-2
	@Since(2.0)
	private String region;

	// FIPS 10-4 notaion (OpenRTB supported but withdrawn from NIST in 2008)
	@Since(2.0)
	private String regionfips104;

	@Since(2.0)
	private String metro;

	@Since(2.0)
	private String city;

	@Since(2.0)
	private String zip;

	// local time as the number +/- minutes from UTC
	@Since(2.3)
	private Integer utcoffset;

	@Since(2.1)
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

	public GeoType getType() {
		return GeoType.convertValue(type);
	}

	public void setType(final GeoType type) {
		this.type = type.getValue();
	}

	public int getUtcOffset() {
		return utcoffset;
	}

	public void setUtcOffset(final int utcoffset) {
		this.utcoffset = utcoffset;
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

	public IpServiceType getIpServiceType() {
		return IpServiceType.convertValue(ipservice);
	}

	public void setIpServiceType(final IpServiceType ipservice) {
		if (ipservice != null) {
			this.ipservice = ipservice.getValue();
		} else {
			this.ipservice = null;
		}
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

		public Builder setType(final GeoType type) {
			geo.setType(type);
			return this;
		}

		public Builder setIpServiceType(final IpServiceType ipservice) {
			geo.setIpServiceType(ipservice);
			return this;
		}

		public Builder setUtcOffset(final int utcoffset) {
			geo.setUtcOffset(utcoffset);
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
