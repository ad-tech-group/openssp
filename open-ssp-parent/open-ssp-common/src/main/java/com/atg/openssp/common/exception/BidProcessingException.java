package com.atg.openssp.common.exception;

/**
 * @author André Schmer
 *
 */
public class BidProcessingException extends Exception {

	private static final long serialVersionUID = 1L;

	private ERROR_CODE code;

	public BidProcessingException() {
		super();
	}

	public BidProcessingException(final String msg) {
		super(msg);
	}

	public BidProcessingException(final ERROR_CODE code) {
		super(code.getValue());
		this.code = code;
	}

	public ERROR_CODE getCode() {
		return code;
	}

}
