package com.atg.openssp.common.exception;

/**
 * @author André Schmer
 *
 */
public class InvalidBidException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidBidException() {
		super();
	}

	public InvalidBidException(final String msg) {
		super(msg);
	}
}
