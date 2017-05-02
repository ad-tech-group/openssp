package com.atg.openssp.common.annotation;

/**
 * @author André Schmer
 *
 */
public enum Scope {

	LOCAL("Local Property"),

	GLOBAL("Global Property");

	private String value;

	private Scope(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
