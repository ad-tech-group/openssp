package openrtb.bidrequest.model;

import java.io.Serializable;

/**
 * @author André Schmer
 *
 */
public final class User implements Cloneable, Serializable {

	private static final long serialVersionUID = 1406239930528483500L;

	private String id;

	private String buyeruid;

	private int yob;

	private String gender;

	private Object ext;

	public User() {
	}

	public User(final User clone) {
		id = clone.getId();
		yob = clone.getYob();
		gender = clone.getGender();
		ext = clone.getExt();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getBuyeruid() {
		return buyeruid;
	}

	public void setBuyeruid(final String buyeruid) {
		this.buyeruid = buyeruid;
	}

	public int getYob() {
		return yob;
	}

	public void setYob(final int yob) {
		this.yob = yob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(final Gender gender) {
		this.gender = gender.getValue();
	}

	public Object getExt() {
		return ext;
	}

	public void setExt(final Object ext) {
		this.ext = ext;
	}

	@Override
	public User clone() {
		try {
			final User clone = (User) super.clone();
			return clone;
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class Builder {

		private final User user;

		public Builder() {
			user = new User();
		}

		public Builder setId(final String id) {
			user.setId(id);
			return this;
		}

		public Builder setGender(final Gender gender) {
			user.setGender(gender);
			return this;
		}

		public Builder setBuyeruid(final String buyeruid) {
			user.setBuyeruid(buyeruid);
			return this;
		}

		public Builder setYob(final int yob) {
			user.setYob(yob);
			return this;
		}

		public Builder setExtension(final Object ext) {
			user.setExt(ext);
			return this;
		}

		public User build() {
			return user;
		}

	}

	@Override
	public String toString() {
		return String.format("User [id=%s, buyeruid=%s, yob=%s, gender=%s, ext=%s]", id, buyeruid, yob, gender, ext);
	}

}
