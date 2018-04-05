package openrtb.bidrequest.model;

import com.google.gson.annotations.Since;
import openrtb.tables.DeviceType;
import openrtb.tables.JavascriptSupport;
import openrtb.tables.NetworkConnectionType;

/**
 * @author André Schmer
 *
 */
public final class Device implements Cloneable {

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
	private int js = JavascriptSupport.YES.getValue();

	@Since(2.4)
	private int geofetch;

	@Since(2.0)
	private int connectiontype = NetworkConnectionType.UNKNOWN.getValue();

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

    public void setDidsha1(String didsha1) {this.didsha1 = didsha1;}

    public void setDidmd5(String didmd5) {this.didmd5 = didmd5;}

    public void setDpidsha1(String dpidsha1) {this.dpidsha1 = dpidsha1;}

    public void setDpidmd5(String dpidmd5) {this.dpidmd5 = dpidmd5;}

    public void setMacsha1(String macsha1) {this.macsha1 = macsha1;}

    public void setMacmd5(String macmd5) {this.macmd5 = macmd5;}

    public void setCarrier(String carrier) {this.carrier = carrier;}

    public void setMake(String make) {this.make = make;}

    public void setModel(String model) {this.model = model;}

    public void setOs(String os) {this.os = os;}

    public void setOsv(String osv) {this.osv = osv;}

    public void setHwv(String hwv) {this.hwv = hwv;}

    public void setH(int h) {this.h = h;}

    public void setW(int w) {this.w = w;}

    public void setPpi(int ppi) {this.ppi = ppi;}

    private void setPxratio(float pxratio) {this.pxratio = pxratio;}

    public void setJs(JavascriptSupport js) {this.js = js.getValue();}

    public void setGeofetch(int geofetch) {this.geofetch = geofetch;}

    public void setConnectiontype(NetworkConnectionType connectiontype) {this.connectiontype = connectiontype.getValue();}

    public void setFlashver(String flashver) {this.flashver = flashver;}

    public void setIfa(String ifa) {this.ifa = ifa;}


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

        public Builder setMake(final String make) {
            device.setMake(make);
            return this;
        }

        public Builder setModel(final String model) {
            device.setModel(model);
            return this;
        }

        public Builder setOs(final String os) {
            device.setOs(os);
            return this;
        }

        public Builder setOsv(final String osv) {
            device.setOsv(osv);
            return this;
        }

        public Builder setHwv(final String hwv) {
            device.setHwv(hwv);
            return this;
        }

        public Builder setW(final int w) {
            device.setW(w);
            return this;
        }

        public Builder setH(final int h) {
            device.setH(h);
            return this;
        }

        public Builder setPpi(final int ppi) {
            device.setPpi(ppi);
            return this;
        }

        public Builder setPxratio(final float pxratio) {
            device.setPxratio(pxratio);
            return this;
        }

        public Builder setJs(final JavascriptSupport js) {
            device.setJs(js);
            return this;
        }

        public Builder setFlashver(final String flashver) {
            device.setFlashver(flashver);
            return this;
        }

        public Builder setCarrier(final String carrier) {
            device.setCarrier(carrier);
            return this;
        }

        public Builder setConnectiontype(final NetworkConnectionType connectiontype) {
            device.setConnectiontype(connectiontype);
            return this;
        }

        public Builder setIfa(final String ifa) {
            device.setIfa(ifa);
            return this;
        }

        public Builder setDidsha1(final String didsha1) {
            device.setDidsha1(didsha1);
            return this;
        }

        public Builder setDidmd5(final String didmd5) {
            device.setDidmd5(didmd5);
            return this;
        }

        public Builder setDpidsha1(final String dpidsha1) {
            device.setDpidsha1(dpidsha1);
            return this;
        }

        public Builder setDpidmd5(final String dpidmd5) {
            device.setDpidmd5(dpidmd5);
            return this;
        }

        public Builder setMacsha1(final String macsha1) {
            device.setMacsha1(macsha1);
            return this;
        }

        public Builder setMacmd5(final String macmd5) {
            device.setMacmd5(macmd5);
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
