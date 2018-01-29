package openrtb.bidrequest.model;

/**
 * @author André Schmer
 *
 */
public final class Impression implements Cloneable {

	private String id;

	private Banner banner;// normally not used in that ssp context

	private Video video;

	private Integer secure;

	private float bidfloor;

	private String bidfloorcur = "USD";// default

	private PMP pmp;

	private Object ext;

	public Impression() {
		pmp = new PMP();
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

	public int getSecure() {
		return secure;
	}

	public void setSecure(final Integer secure) {
		this.secure = secure;
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

	@Override
	public Impression clone() {
		try {
			final Impression clone = (Impression) super.clone();
			if (video != null) {
				clone.setVideo(video.clone());
			}
			return clone;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("Impression [id=%s, video=%s, secure=%s, ext=%s]", id, video, secure, ext);
	}

	public static class Builder {

		private final Impression impression;

		// private final List<Banner.Builder> listOfBannerBuilder = new ArrayList<>();

		// private final List<Video.Builder> listOfVideoBuilder = new ArrayList<>();

		public Builder() {
			impression = new Impression();
		}

		public Builder setId(final String id) {
			impression.setId(id);
			return this;
		}

		public Builder setVideo(final Video video) {
			impression.setVideo(video);
			return this;
		}

		public Builder setSecure(final int secure) {
			impression.setSecure(secure);
			return this;
		}

		public Builder setExtension(final Object ext) {
			impression.setExt(ext);
			return this;
		}

		public Builder setBanner(final Banner banner) {
			impression.setBanner(banner);
			return this;
		}

		public Builder setBanner(final Banner.Builder bannerBuilder) {
			// listOfBannerBuilder.add(bannerBuilder);
			impression.setBanner(bannerBuilder.build());
			return this;
		}

		public Builder setVideo(final Video.Builder videoBuilder) {
			// listOfVideoBuilder.add(videoBuilder);
			impression.setVideo(videoBuilder.build());
			return this;
		}

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
