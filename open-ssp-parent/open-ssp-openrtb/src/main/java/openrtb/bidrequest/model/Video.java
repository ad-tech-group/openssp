package openrtb.bidrequest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author André Schmer
 *
 */
public final class Video implements Cloneable, Serializable {

	private static final long serialVersionUID = 3765223184708363144L;

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
	private List<Integer> protocols;

	private List<Integer> battr;

	// required
	private Integer linearity;

	private List<Banner> companionad;

	// required
	private List<Integer> api;

	private Object ext;

	public Video() {
		this.mimes = new ArrayList<>();
		this.protocols = new ArrayList<>();
		this.battr = new ArrayList<>();
		this.companionad = new ArrayList<>();
		this.api = new ArrayList<>();
	}

	public List<String> getMimes() {
		return this.mimes;
	}

	public void setMimes(final List<String> mimes) {
		this.mimes = mimes;
	}

	public void addMime(final String mime) {
		this.mimes.add(mime);
	}

	public int getMinduration() {
		return this.minduration;
	}

	public void setMinduration(final int minduration) {
		this.minduration = minduration;
	}

	public int getMaxduration() {
		return this.maxduration;
	}

	public void setMaxduration(final int maxduration) {
		this.maxduration = maxduration;
	}

	public int getW() {
		return this.w;
	}

	public void setW(final int w) {
		this.w = w;
	}

	public int getH() {
		return this.h;
	}

	public void setH(final int h) {
		this.h = h;
	}

	public int getStartdelay() {
		return this.startdelay;
	}

	public void setStartdelay(final int startdelay) {
		this.startdelay = startdelay;
	}

	public List<Integer> getProtocols() {
		return this.protocols;
	}

	public void setProtocols(final List<Integer> protocols) {
		this.protocols = protocols;
	}

	public void addProtocol(final Integer protocol) {
		this.protocols.add(protocol);
	}

	public void addApi(final Integer api) {
		this.api.add(api);
	}

	public List<Integer> getBattr() {
		// final List<Integer> reqIndex = new ArrayList<>(battr);
		// if (companionad != null) {
		// for (final Banner banner : companionad) {
		// if (!banner.getBattr().isEmpty()) {
		// reqIndex.addAll(banner.getBattr());
		// }
		// }
		// }
		// return reqIndex;
		return this.battr;
	}

	public void setBattr(final List<Integer> battr) {
		this.battr = battr;
	}

	public void addBattr(final Integer battr) {
		this.battr.add(battr);
	}

	public int getLinearity() {
		return this.linearity;
	}

	public List<Banner> getCompanionad() {
		return this.companionad;
	}

	public void setCompanionad(final List<Banner> companionad) {
		this.companionad = companionad;
	}

	public List<Integer> getApi() {
		return this.api;
	}

	public void setApi(final List<Integer> api) {
		this.api = api;
	}

	public Object getExt() {
		return this.ext;
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
			this.video = new Video();
		}

		public Builder addMime(final String mime) {
			this.video.addMime(mime);
			return this;
		}

		public Builder setMinduration(final int minduration) {
			this.video.setMinduration(minduration);
			return this;
		}

		public Builder setMaxduration(final int maxduration) {
			this.video.setMaxduration(maxduration);
			return this;
		}

		public Builder setW(final int w) {
			this.video.setW(w);
			return this;
		}

		public Builder setH(final int h) {
			this.video.setH(h);
			return this;
		}

		public Builder setStartdelay(final int startdelay) {
			this.video.setStartdelay(startdelay);
			return this;
		}

		public Builder addProtocol(final int protocol) {
			this.video.addProtocol(protocol);
			return this;
		}

		public Builder addApi(final int api) {
			this.video.addApi(api);
			return this;
		}

		public Builder addBattr(final Integer battr) {
			this.video.addBattr(battr);
			return this;
		}

		public Builder setExtension(final Object ext) {
			this.video.setExt(ext);
			return this;
		}

		public Video build() {
			return this.video;
		}

		public Builder setLinearity(final int linearity) {
			this.video.setLinearity(linearity);
			return this;
		}

		public Builder addAllBattr(final List<Integer> battrList) {
			this.video.setBattr(battrList);
			return this;
		}

		public Builder addCompanionad(final Banner.Builder companionAd) {
			this.listOfCompaonionAdBuilder.add(companionAd);
			this.video.getCompanionad().add(companionAd.build());
			return this;
		}
	}

	@Override
	public String toString() {
		return String.format("Video [mimes=%s, minduration=%s, maxduration=%s, w=%s, h=%s, startdelay=%s, protocols=%s, battr=%s, ext=%s]", this.mimes, this.minduration,
		        this.maxduration, this.w, this.h, this.startdelay, this.protocols, this.battr, this.ext);
	}

	public void setLinearity(final int value) {
		this.linearity = value;
	}

}
