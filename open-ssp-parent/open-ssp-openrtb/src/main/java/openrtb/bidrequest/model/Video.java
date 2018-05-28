package openrtb.bidrequest.model;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import openrtb.tables.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author André Schmer
 *
 */
public final class Video implements Cloneable {

	// required
	private List<String> mimes;
	// required
	private Integer minduration;
	// required
	private Integer maxduration;
	// required
	private int w;
	// required
	private int h;
	// required
	private int startdelay = 0;

	// required
    @Until(2.2)
	private int protocol;

	@Since(2.2)
	private List<Integer> protocols;

	private List<Integer> battr;

	// required
	private Integer linearity;

	private List<Banner> companionad;

	// required
	private List<Integer> api;

	private Object ext;

	public Video() {
		mimes = new ArrayList<>();
		battr = new ArrayList<>();
		companionad = new ArrayList<>();
        protocols = new ArrayList<>();
		api = new ArrayList<>();
	}

	public List<String> getMimes() {
		return mimes;
	}

	public void setMimes(final List<String> mimes) {
	    this.mimes.clear();
	    if (mimes != null) {
            this.mimes.addAll(mimes);
        }
	}

	public void addMime(final String mime) {
		this.mimes.add(mime);
	}

	public int getMinduration() {
		return minduration;
	}

	public void setMinduration(final int minduration) {
		this.minduration = minduration;
	}

	public int getMaxduration() {
		return maxduration;
	}

	public void setMaxduration(final int maxduration) {
		this.maxduration = maxduration;
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

	public int getStartdelay() {
		return startdelay;
	}

	public void setStartdelay(final int startdelay) {
		this.startdelay = startdelay;
	}

    @Deprecated
	public void setProtocol(final VideoBidResponseProtocol protocol) {
		this.protocol = protocol.getValue();
	}

    public List<VideoBidResponseProtocol> getProtocols() {
        ArrayList<VideoBidResponseProtocol> list = new ArrayList();
        this.protocols.forEach(a -> list.add(VideoBidResponseProtocol.convert(a)));
        return list;
    }

    public void setProtocols(final List<VideoBidResponseProtocol> protocols) {
        this.protocols.clear();
        if (protocols != null) {
            protocols.forEach(a -> this.protocols.add(a.getValue()));
        }
    }

    public void addToProtocols(final VideoBidResponseProtocol protocol) {
        this.protocols.add(protocol.getValue());
    }

    public void setBattr(final List<CreativeAttribute> battr) {
        this.battr.clear();
        if (battr != null) {
            for (CreativeAttribute a : battr) {
                this.battr.add(a.getValue());
            }
        }
    }

    public void addBattr(final CreativeAttribute battr) {
        this.battr.add(battr.getValue());
    }

    public void setLinearity(final VideoLinearity linearity) {
		this.linearity = linearity.getValue();
	}

	public VideoLinearity getLinearity() {
		return VideoLinearity.convertValue(linearity);
	}

    public List<Banner> getCompanionad() {
        return companionad;
    }

    public void setCompanionad(final List<Banner> companionad) {
        this.companionad.clear();
        if (companionad != null) {
            this.companionad.addAll(companionad);
        }
    }

    public void addCompanionad(final Banner companionad) {
        this.companionad.add(companionad);
    }

	public List<ApiFramework> getApis() {
		ArrayList<ApiFramework> list = new ArrayList();
		this.api.forEach(a -> list.add(ApiFramework.convertValue(a)));
		return list;
	}

	public void setApi(final List<ApiFramework> apis) {
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
	public Video clone() {
		try {
			final Video clone = (Video) super.clone();
			return clone;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;

	}

    public static class Builder {

		private final Video video;

		private final List<Banner.Builder> listOfCompaonionAdBuilder = new ArrayList<>();

		public Builder() {
			video = new Video();
		}

		public Builder addMime(final String mime) {
			video.addMime(mime);
			return this;
		}

		public Builder setMinduration(final int minduration) {
			video.setMinduration(minduration);
			return this;
		}

		public Builder setMaxduration(final int maxduration) {
			video.setMaxduration(maxduration);
			return this;
		}

		public Builder setW(final int w) {
			video.setW(w);
			return this;
		}

		public Builder setH(final int h) {
			video.setH(h);
			return this;
		}

		public Builder setStartdelay(final int startdelay) {
			video.setStartdelay(startdelay);
			return this;
		}

		@Deprecated
        public Builder setProtocol(VideoBidResponseProtocol protocol) {
            video.setProtocol(protocol);
            return this;
        }

		public Builder addToProtocols(final VideoBidResponseProtocol protocol) {
			video.addToProtocols(protocol);
			return this;
		}

		public Builder addBattr(final CreativeAttribute battr) {
			video.addBattr(battr);
			return this;
		}

		public Builder setExtension(final Object ext) {
			video.setExt(ext);
			return this;
		}

		public Builder setLinearity(final VideoLinearity linearity) {
			video.setLinearity(linearity);
			return this;
		}

		public Builder addAllBattr(final List<CreativeAttribute> battrList) {
			video.setBattr(battrList);
			return this;
		}

		public Builder addCompanionad(final Banner.Builder companionAd) {
			listOfCompaonionAdBuilder.add(companionAd);
			video.getCompanionad().add(companionAd.build());
			return this;
		}

		public Builder addApi(final ApiFramework api) {
			video.addApi(api);
			return this;
		}

		public Video build() {
            return video;
        }

    }

	@Override
	public String toString() {
		return String.format("Video [mimes=%s, minduration=%s, maxduration=%s, w=%s, h=%s, startdelay=%s, protocols=%s, battr=%s, ext=%s]", mimes, minduration, maxduration, w, h,
		        startdelay, protocols, battr, ext);
	}

}
