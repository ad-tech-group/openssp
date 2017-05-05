package com.atg.openssp.common.exception;

/**
 * @author André Schmer
 *
 */
public class RequestException extends Exception {

	private static final long serialVersionUID = 1L;

	public RequestException(final ERROR_CODE code, final String msg) {
		super(msg + " " + code.getValue());
	}

	public RequestException(final String msg) {
		super(msg);
	}

	public RequestException(final ERROR_CODE code) {
		super(code.getValue());
	}

}
