package openrtb.bidrequest.model;

import openrtb.tables.VideoApiFramework;
import openrtb.tables.VideoBidResponseProtocol;
import openrtb.tables.VideoLinearity;

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
	private List<Integer> protocols = new ArrayList<>();

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
		api = new ArrayList<>();
	}

	public List<String> getMimes() {
		return mimes;
	}

	public void setMimes(final List<String> mimes) {
		this.mimes = mimes;
	}

	public void addMime(final String mime) {
		mimes.add(mime);
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

//	public List<Integer> getProtocols() {
//		return protocols;
//	}

//	public void setProtocols(final List<VideoBidResponseProtocol> protocols) {
//		this.protocolsX = protocols;
//	}

	public void addProtocol(final VideoBidResponseProtocol protocol) {
		protocols.add(protocol.getValue());
	}

	public void addApi(final VideoApiFramework api) {
		this.api.add(api.getValue());
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
		return battr;
	}

	public void setBattr(final List<Integer> battr) {
		this.battr = battr;
	}

	public void addBattr(final Integer battr) {
		this.battr.add(battr);
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
		this.companionad = companionad;
	}

//	public List<Integer> getApiX() {
//		return apiX;
//	}

//	public void setApiX(final List<Integer> api) {
//		this.apiX = api;
//	}

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

		public Builder addProtocol(final VideoBidResponseProtocol protocol) {
			video.addProtocol(protocol);
			return this;
		}

		public Builder addApi(final VideoApiFramework api) {
			video.addApi(api);
			return this;
		}

		public Builder addBattr(final Integer battr) {
			video.addBattr(battr);
			return this;
		}

		public Builder setExtension(final Object ext) {
			video.setExt(ext);
			return this;
		}

		public Video build() {
			return video;
		}

		public Builder setLinearity(final VideoLinearity linearity) {
			video.setLinearity(linearity);
			return this;
		}

		public Builder addAllBattr(final List<Integer> battrList) {
			video.setBattr(battrList);
			return this;
		}

		public Builder addCompanionad(final Banner.Builder companionAd) {
			listOfCompaonionAdBuilder.add(companionAd);
			video.getCompanionad().add(companionAd.build());
			return this;
		}
	}

	@Override
	public String toString() {
		return String.format("Video [mimes=%s, minduration=%s, maxduration=%s, w=%s, h=%s, startdelay=%s, protocols=%s, battr=%s, ext=%s]", mimes, minduration, maxduration, w, h,
		        startdelay, protocols, battr, ext);
	}

}
