package org.csp.store.exception;

public class Md5Exception extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2480190610121993068L;

	public Md5Exception() {
	}

	public Md5Exception(String message) {
		super(message);
	}

	public Md5Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public Md5Exception(Throwable cause) {
		super(cause);
	}
}
