package io.freestar.ssp.dspSim.domain.request;

/**
 * @author André Schmer
 *
 */
public enum Gender {

	/**
	 * m
	 */
	MALE("M"),

	/**
	 * f
	 */
	FEMALE("F"),

	/**
	 * O
	 */
	OTHER("O");

	private String value;

	private Gender(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static Gender convert(final String value) {
		for (final Gender gender : values()) {
			if (gender.value.equalsIgnoreCase(value)) {
				return gender;
			}
		}
		return OTHER;
	}
}
