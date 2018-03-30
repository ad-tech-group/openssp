package com.atg.openssp.common.exception;

/**
 * @author André Schmer
 *
 */
public class RequestException extends Exception {

	private static final long serialVersionUID = 1L;
	private final ERROR_CODE code;

	public RequestException(final ERROR_CODE code, final String msg) {
		super(msg + " " + code.getValue());
		this.code = code;
	}

	public RequestException(final String msg) {
		super(msg);
		this.code = ERROR_CODE.E999;
	}

	public RequestException(final ERROR_CODE code) {
		super(code.getValue());
		this.code = code;
	}

	public ERROR_CODE getCode()
	{
		return code;
	}

}
