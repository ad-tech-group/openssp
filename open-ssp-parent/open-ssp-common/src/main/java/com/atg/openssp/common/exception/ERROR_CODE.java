package com.atg.openssp.common.exception;

/**
 * @author André Schmer
 * 
 */
public enum ERROR_CODE {

	/**
	 * 400, Request Invalid.
	 */
	E400(400, "Request Invalid."),

	/**
	 * 901, Technical Error.
	 */
	E901(901, "Technical Error."),

	/**
	 * 906, parameter incomplete.
	 */
	E906(906, "Parameter Incomplete."),

	/**
	 * 907, website not connected.
	 */
	E907(907, "website not connected."),

	/**
	 * 908, zone not connected.
	 */
	E908(908, "zone not connected."),

	/**
	 * 909, VideoAd not connected.
	 */
	E909(909, "VideoAd not connected."),

	/**
	 * 204, no content
	 */
	E204(204, "no content"),

	/**
	 * 999, gen. error
	 */
	E999(999, "gen. error");

	private String value;
	private int code;

	ERROR_CODE(final int code, final String errorValue) {
		this.code = code;
		value = errorValue;
	}

	public String getValue() {
		return value;
	}

	public int getCode() {
		return code;
	}

}
