package openrtb.bidrequest.model;

import com.google.gson.annotations.Since;
import openrtb.tables.DeviceType;

/**
 * @author André Schmer
 *
 */
public final class Device implements Cloneable {

	// normally recommended, but not available?
	// private String ua;

	@Since(2.0)
	private Geo geo;

	@Since(2.0)
	private int dnt = 1;// default, don't track

	@Since(2.0)
	private String ua;

	@Since(2.3)
	private int lmt = 1;// default, don't track

	@Since(2.0)
	private String ip;

	@Since(2.0)
	private String ipv6;

	@Since(2.0)
	private int devicetype = DeviceType.CONNECTED_DEVICE.getValue();

	@Since(2.0)
	private String language;

	@Since(2.0)
	private String didsha1;

	@Since(2.0)
	private String didmd5;

	@Since(2.0)
	private String dpidsha1;

	@Since(2.0)
	private String dpidmd5;

	@Since(2.3)
	private String macsha1;

	@Since(2.3)
	private String macmd5;

	@Since(2.0)
	private String carrier;

	@Since(2.0)
	private String make;

	@Since(2.0)
	private String model;

	@Since(2.0)
	private String os;

	@Since(2.0)
	private String osv;

	@Since(2.3)
	private String hwv;

	@Since(2.3)
	private int h;

	@Since(2.3)
	private int w;

	@Since(2.3)
	private int ppi;

	@Since(2.3)
	private float pxratio;

	@Since(2.0)
	private String js;

	@Since(2.4)
	private int geofetch;

	@Since(2.0)
	private String connectiontype;

	@Since(2.0)
	private String flashver;

	@Since(2.2)
	private String ifa;

	@Since(2.1)
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

	public String getUa() {
		return ua;
	}

	public void setUa(final String ua) {
		this.ua = ua;
	}

	public int getLmt() {
		return lmt;
	}

	public void setLmtY(final int lmt) {
		this.lmt = lmt;
	}

	public DeviceType getDevicetype() {
		return DeviceType.convertValue(devicetype);
	}

	public void setDevicetype(final DeviceType devicetype) {
		this.devicetype = devicetype.getValue();
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

		public Builder setUa(final String ua) {
			device.setUa(ua);
			return this;
		}

		public Builder setIp(final String ip) {
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

		public Builder setLmty(final int lmt) {
			device.setLmtY(lmt);
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

		public Builder setDeviceType(final DeviceType deviceType) {
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
