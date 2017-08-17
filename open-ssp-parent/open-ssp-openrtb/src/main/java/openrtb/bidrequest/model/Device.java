package openrtb.bidrequest.model;

import java.io.Serializable;

/**
 * @author André Schmer
 *
 */
public final class Device implements Cloneable, Serializable {

	private static final long serialVersionUID = 2458174019726387405L;

	// normally recommended, but not available?
	// private String ua;

	private Geo geo;

	private int dnt = 1;// default, don't track

	private int lmt = 1;// default, don't track

	private String ip;

	private String ipv6;

	private int devicetype = 6; // out-of-home (suggestion)

	private String language;

	private Object ext;

	public Device() {}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(final Geo geo) {
		this.geo = geo;
	}

	public int getDnt() {
		return dnt;
	}

	public void setDnt(final int dnt) {
		this.dnt = dnt;
	}

	public int getLmt() {
		return lmt;
	}

	public void setLmt(final int lmt) {
		this.lmt = lmt;
	}

	public int getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(final int devicetype) {
		this.devicetype = devicetype;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(final String ip) {
		this.ip = ip;
	}

	public void setIpv6(final String ipv6) {
		this.ipv6 = ipv6;
	}

	public String getIpv6() {
		return ipv6;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	@Override
	public Device clone() {
		try {
			final Device device = (Device) super.clone();
			if (geo != null) {
				device.setGeo(geo.clone());
			}
			return device;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class Builder {

		private final Device device;

		public Builder() {
			device = new Device();
		}

		public Builder setGeo(final Geo geo) {
			device.setGeo(geo);
			return this;
		}

		public Builder setDnt(final int dnt) {
			device.setDnt(dnt);
			return this;
		}

		public Builder setIp(final String ip) {
			System.out.println("ip: " + ip);
			if (ip != null) {
				if (ip.contains(":")) {
					device.setIpv6(ip);
				} else {
					device.setIp(ip);
				}
			}
			return this;
		}

		public Builder setIpv6(final String ipv6) {
			device.setIpv6(ipv6);
			return this;
		}

		public Builder setLmt(final int lmt) {
			device.setLmt(lmt);
			return this;
		}

		public Builder setLanguage(final String iso_alpha_2) {
			device.setLanguage(iso_alpha_2);
			return this;
		}

		public Builder setExtension(final Object ext) {
			device.setExt(ext);
			return this;
		}

		public Builder setDeviceType(final int deviceType) {
			device.setDevicetype(deviceType);
			return this;
		}

		public Device build() {
			return device;
		}
	}

	@Override
	public String toString() {
		return String.format("Device [geo=%s, ext=%s]", geo, ext);
	}

}
