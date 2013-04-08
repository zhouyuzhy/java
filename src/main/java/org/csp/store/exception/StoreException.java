package org.csp.store.exception;

public class StoreException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2480190610121993068L;

	public StoreException() {
	}

	public StoreException(String message) {
		super(message);
	}

	public StoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public StoreException(Throwable cause) {
		super(cause);
	}
}
