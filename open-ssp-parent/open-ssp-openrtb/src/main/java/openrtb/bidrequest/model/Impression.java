package openrtb.bidrequest.model;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import openrtb.tables.ImpressionSecurity;

/**
 * @author André Schmer
 *
 */
public final class Impression implements Cloneable {

	@Until(2.0)
	private String impid;

	@Since(2.0)
	private String id;

	private Banner banner;

//	private Native native;

	private Video video;

	@Since(2.2)
	private int secure = ImpressionSecurity.NON_SECURE.getValue();

	private float bidfloor;

	private String bidfloorcur = "USD";// default

	@Since(2.2)
	private PMP pmp;

	private Object ext;

	public Impression() {
		pmp = new PMP();
	}

	//@Deprecated
	public String getImpid() {
		return impid;
	}

	//@Deprecated
	public void setImpid(final String impid) {
		this.impid = impid;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(final Video video) {
		this.video = video;
	}

	public Banner getBanner() {
		return banner;
	}

	public void setBanner(final Banner banner) {
		this.banner = banner;
	}

//	public Native getNative() {
//		return native;
//	}

//	public void setNative(final Native native) {
//		this.native = native;
//	}

	public ImpressionSecurity getSecure() {
		return ImpressionSecurity.convertValue(secure);
	}

	public void setSecure(final ImpressionSecurity secure) {
		this.secure = secure.getValue();
	}

	public float getBidfloor() {
		return bidfloor;
	}

	public void setBidfloor(final float bidfloor) {
		this.bidfloor = bidfloor;
	}

	public String getBidfloorcur() {
		return bidfloorcur;
	}

	public void setBidfloorcur(final String bidfloorcur) {
		this.bidfloorcur = bidfloorcur;
	}

	public PMP getPmp() {
		return pmp;
	}

	public void setPmp(final PMP pmp) {
		this.pmp = pmp;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	public boolean hasVideo() {
		return video != null;
	}

	public boolean hasBanner() {
		return banner != null;
	}

//	public boolean hasNative() {
//		return native != null;
//	}

	@Override
	public Impression clone() {
		try {
			final Impression clone = (Impression) super.clone();
			if (video != null) {
				clone.setVideo(video.clone());
			}
			if (banner != null) {
				clone.setBanner(banner.clone());
			}
//			if (native != null) {
//				clone.setNative(native.clone());
//			}
			return clone;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		String bannerString;
		if (banner!=null) {
			bannerString = String.format(", banner=%s", banner);
		} else {
			bannerString = "";
		}
		String videoString;
		if (video!=null) {
			videoString = String.format(", video=%s", video);
		} else {
			videoString = "";
		}
		String nativeString;
//		if (native!=null) {
//			nativeString = String.format(", native=%s", native);
//		} else {
			nativeString = "";
//		}
		return String.format("Impression [id=%s"+bannerString+videoString+nativeString+", secure=%s, ext=%s]", id, secure, ext);
	}

	public static class Builder {

		private final Impression impression;

		// private final List<Banner.Builder> listOfBannerBuilder = new ArrayList<>();

		// private final List<Video.Builder> listOfVideoBuilder = new ArrayList<>();

		public Builder() {
			impression = new Impression();
		}

		@Deprecated
		public Builder setImpid(final String impid) {
			impression.setImpid(impid);
			return this;
		}

		public Builder setId(final String id) {
			impression.setId(id);
			return this;
		}

		public Builder setSecure(final ImpressionSecurity secure) {
			impression.setSecure(secure);
			return this;
		}

		public Builder setExtension(final Object ext) {
			impression.setExt(ext);
			return this;
		}

		public Builder setVideo(final Video video) {
			impression.setVideo(video);
			return this;
		}

		public Builder setBanner(final Banner banner) {
			impression.setBanner(banner);
			return this;
		}

//		public Builder setNative(final Native native) {
//			impression.setNative(native);
//			return this;
//		}

		public Builder setVideo(final Video.Builder videoBuilder) {
			impression.setVideo(videoBuilder.build());
			return this;
		}

		public Builder setBanner(final Banner.Builder bannerBuilder) {
			impression.setBanner(bannerBuilder.build());
			return this;
		}

//		public Builder setNative(final Native.Builder nativeBuilder) {
//			impression.setNative(nativeBuilder.build());
//			return this;
//		}

		public Builder setBidfloor(final float floor) {
			impression.setBidfloor(floor);
			return this;
		}

		public Builder setBidfloorcurrency(final String floorcurrency) {
			impression.setBidfloorcur(floorcurrency);
			return this;
		}

		public Builder setPmp(final PMP.Builder pmp) {
			impression.setPmp(pmp.build());
			return this;
		}

		public Impression build() {
			return impression;
		}
	}

}
