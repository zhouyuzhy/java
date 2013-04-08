package org.csp.store.exception;

public class StoreKeyNotFoundException extends StoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2480190610121993068L;

	public StoreKeyNotFoundException() {
	}

	public StoreKeyNotFoundException(String message) {
		super(message);
	}

	public StoreKeyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public StoreKeyNotFoundException(Throwable cause) {
		super(cause);
	}
}
