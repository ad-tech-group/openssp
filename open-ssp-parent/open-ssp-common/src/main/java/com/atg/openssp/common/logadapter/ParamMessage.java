package com.atg.openssp.common.logadapter;

/**
 * @author André Schmer
 *
 */
public class ParamMessage {

	private String message;

	private String[] params;

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(final String[] params) {
		this.params = params;
	}

}
