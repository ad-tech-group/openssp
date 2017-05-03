package openrtb.bidrequest.extension;

import java.io.Serializable;

import openrtb.bidrequest.model.Gender;

/**
 * This is not a part of the standard OpenRTB. It defines a target group of deliveries and substitutes the user object in bidrequest object.
 * 
 * @author André Schmer
 *
 */
public class Contact implements Serializable {

	private static final long serialVersionUID = -454410149412758676L;

	private String age;

	private String gender;

	private String agevarianz;

	public Contact() {}

	public String getAge() {
		return age;
	}

	public void setAge(final String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(final Gender gender) {
		this.gender = gender.getValue();
	}

	public String getAgevarianz() {
		return agevarianz;
	}

	public void setAgevarianz(final String agevarianz) {
		this.agevarianz = agevarianz;
	}

	public static class Builder {

		private final Contact contact;

		public Builder() {
			contact = new Contact();
		}

		public Builder setAge(final String age) {
			contact.setAge(age);
			return this;
		}

		public Builder setGender(final Gender gender) {
			contact.setGender(gender);
			return this;
		}

		public Builder setAgevarianz(final String agevarianz) {
			contact.setAgevarianz(agevarianz);
			return this;
		}

		public Contact build() {
			return contact;
		}

	}

	@Override
	public String toString() {
		return String.format("Contact [age=%s, gender=%s, agediff=%s]", age, gender, agevarianz);
	}

}
