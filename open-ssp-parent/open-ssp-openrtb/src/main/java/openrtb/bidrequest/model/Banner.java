package openrtb.bidrequest.model;

import java.util.Comparator;
import java.util.List;

import com.google.gson.annotations.Since;

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
	private int pos;
	private List<Integer> btype;// blocked creative types
	private List<Integer> battr;
	private String[] mimes;// commaseparated list
	private int topframe = 0;
	private int[] expdir; // expandable directions 1-6
	private int[] api;
	private Object ext;

	@Since(2.2)
	private int wmax;

	@Since(2.2)
	private int hmax;

	@Since(2.2)
	private int wmin;

	@Since(2.2)
	private int hmin;
	private Object[] format;

	public Banner() {
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

	public void setId(final String id) {
		this.id = id;
	}

	public int[] getExpdir() {
		return expdir;
	}

	public void setExpdir(final int[] expdir) {
		this.expdir = expdir;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(final int pos) {
		this.pos = pos;
	}

	public List<Integer> getBtype() {
		return btype;
	}

	public void setBtype(final List<Integer> btype) {
		this.btype = btype;
	}

	public List<Integer> getBattr() {
		return battr;
	}

	public void setBattr(final List<Integer> battr) {
		this.battr = battr;
	}

	public int getTopframe() {
		return topframe;
	}

	public void setTopframe(final int topframe) {
		this.topframe = topframe;
	}

	public int[] getApi() {
		return api;
	}

	public void setApi(final int[] api) {
		this.api = api;
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
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

	public void setFormat(Object[] format) {
		this.format = format;
	}

	public Object[] getFormat() {
		return format;
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

		public Builder addAllBattr(final List<Integer> allBattr) {
			banner.setBattr(allBattr);
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