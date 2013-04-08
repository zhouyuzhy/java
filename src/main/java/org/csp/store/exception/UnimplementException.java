package org.csp.store.exception;

public class UnimplementException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2480190610121993068L;

	public UnimplementException() {
	}

	public UnimplementException(String message) {
		super(message);
	}

	public UnimplementException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnimplementException(Throwable cause) {
		super(cause);
	}
}
