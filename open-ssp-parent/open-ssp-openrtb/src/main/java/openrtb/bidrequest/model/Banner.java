package openrtb.bidrequest.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.CRC32;

import com.google.gson.annotations.Since;
import openrtb.tables.*;

/**
 * @author André Schmer
 * @see OpenRTB-API-Specification #section Banner Object
 * @version 2.1, 2.2, 2.3
 */
public final class Banner implements Cloneable {
	// required
	private int w;
	private int h;

	private String id;
	// optional
	private Integer pos;
	private List<Integer> btype;// blocked creative types
	private List<Integer> battr;
	private String[] mimes;// commaseparated list
	private int topframe = 0;
	private List<Integer> expdir; // expandable directions 1-6
	private List<Integer> api;
	private Object ext;

	@Since(2.2)
	private int wmax;

	@Since(2.2)
	private int hmax;

	@Since(2.2)
	private int wmin;

	@Since(2.2)
	private int hmin;
	private List<Object> format;

	public Banner() {
		btype = new ArrayList<>();
		battr = new ArrayList<>();
		expdir = new ArrayList<>();
		api = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public int getW() {
		return w;
	}

	public void setW(final int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(final int h) {
		this.h = h;
	}

	public String[] getMimes() {
		return mimes;
	}

	public void setMimes(final String[] mimes) {
		this.mimes = mimes;
	}

    public void setMimes(final List<String> mimes) {
        this.mimes = new String[mimes.size()];
        for (int i=0; i<mimes.size(); i++) {
            this.mimes[i] = mimes.get(i);
        }
    }

    public void setId(final String id) {
		this.id = id;
	}

	public void addExpdir(final ExpandableDirectionType expdir) {
		if (expdir != null) {
			this.expdir.add(expdir.getValue());
		}
	}

	public void setAllExpdir(final ExpandableDirectionType[] expdir) {
		this.expdir.clear();
		if (expdir != null) {
			for (ExpandableDirectionType t : expdir) {
				this.expdir.add(t.getValue());
			}
		}
	}

	public void setAllExpdir(final List<ExpandableDirectionType> expdir) {
		this.expdir.clear();
		if (expdir != null) {
			for (ExpandableDirectionType t : expdir) {
				this.expdir.add(t.getValue());
			}
		}
	}

	public AddPosition getPos() {
		return AddPosition.convertValue(pos);
	}

	public void setPos(final AddPosition pos) {
		this.pos = pos.getValue();
	}

	public void addBtype(final BannerAdType btype) {
        if (btype != null) {
            this.btype.add(btype.getValue());
        }
	}

    public void setAllBtype(final BannerAdType[] btype) {
        this.btype.clear();
        if (btype != null) {
            for (BannerAdType t : btype) {
                this.btype.add(t.getValue());
            }
        }
    }

    public void setAllBtype(final List<BannerAdType> btype) {
        this.btype.clear();
        if (btype != null) {
            for (BannerAdType t : btype) {
                this.btype.add(t.getValue());
            }
        }
    }

    public void addBattr(final CreativeAttribute battr) {
	    if (battr != null) {
	        this.battr.add(battr.getValue());
        }
	}

    public void setAllBattr(final CreativeAttribute[] battr) {
        this.battr.clear();
        if (battr != null) {
            for (CreativeAttribute a : battr) {
                this.battr.add(a.getValue());
            }
        }
    }

    public void setAllBattr(final List<CreativeAttribute> battr) {
        this.battr.clear();
        if (battr != null) {
            for (CreativeAttribute a : battr) {
                this.battr.add(a.getValue());
            }
        }
    }

    public int getTopframe() {
		return topframe;
	}

	public void setTopframe(final int topframe) {
		this.topframe = topframe;
	}

	public int getWmax() {
		return wmax;
	}

	public void setWmax(final int wmax) {
		this.wmax = wmax;
	}

	public int getHmax() {
		return hmax;
	}

	public void setHmax(final int hmax) {
		this.hmax = hmax;
	}

	public int getWmin() {
		return wmin;
	}

	public void setWmin(final int wmin) {
		this.wmin = wmin;
	}

	public int getHmin() {
		return hmin;
	}

	public void setHmin(final int hmin) {
		this.hmin = hmin;
	}

	public List<ApiFramework> getApis() {
		ArrayList<ApiFramework> list = new ArrayList();
		this.api.forEach(a -> list.add(ApiFramework.convertValue(a)));
		return list;
	}

	/*
	public void setApis(final int[] apis) {
		this.api = api;
	}
	*/

	public void setApis(final List<ApiFramework> apis) {
		this.api.clear();
		if (apis != null) {
			apis.forEach(a -> this.api.add(a.getValue()));
		}
	}

	public void addApi(final ApiFramework api) {
		this.api.add(api.getValue());
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	@Override
	public Banner clone() {
		try {
			final Banner clone = (Banner) super.clone();
			return clone;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addFormat(final Object format) {
		if (format != null) {
		    if (this.format == null) {
		        this.format = new ArrayList();
            }
			this.format.add(format);
		}
	}

	public void setFormat(final Object[] format) {
	    if (this.format == null) {
	        this.format = new ArrayList();
        } else {
            this.format.clear();
        }
		if (format != null) {
			for (Object a : format) {
				this.format.add(a);
			}
		}
	}

	public void setFormat(final List<Object> format) {
        if (this.format == null) {
            this.format = new ArrayList();
        } else {
            this.format.clear();
        }
		if (format != null) {
			this.format.addAll(format);
		}
	}

	public List<Object> getFormat() {
	    if (format.size() == 0) {
	        return null;
        } else {
            ArrayList<Object> list = new ArrayList();
            list.addAll(format);
            return list;
        }
	}

    public static class Builder {

		private final Banner banner;

		public Builder() {
			banner = new Banner();
		}

		public Builder setId(final String id) {
			banner.setId(id);
			return this;
		}

		public Builder setW(int w) {
			banner.setW(w);
			return this;
		}

		public Builder setH(int h) {
			banner.setH(h);
			return this;
		}

        public Builder addBtype(final BannerAdType btype) {
            banner.addBtype(btype);
            return this;
        }

        public Builder addBattr(final CreativeAttribute battr) {
            banner.addBattr(battr);
            return this;
        }

        public Builder setPos(final AddPosition pos) {
            banner.setPos(pos);
            return this;
        }

        public Builder setMimes(final String[] mimes) {
            banner.setMimes(mimes);
            return this;
        }

		public Builder addApi(final ApiFramework api) {
			banner.addApi(api);
			return this;
		}

		public Banner build() {
			return banner;
		}

    }

	public static class BannerSize implements Comparable<BannerSize> {
		private int w;
		private int h;

		public BannerSize(String constructString) {
			int index = constructString.indexOf('x');
			w = Integer.parseInt(constructString.substring(0, index));
			h = Integer.parseInt(constructString.substring(index+1));

		}

		public int getW() {
			return w;
		}

		public void setW(int w) {
			this.w = w;
		}

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
		}

		@Override
		public String toString()
		{
			return w+"x"+h;
		}

		@Override
		public int compareTo(BannerSize o) {
			return Comparator.comparing(BannerSize::getW)
					.thenComparing(BannerSize::getH)
					.compare(this, o);
		}
	}
}