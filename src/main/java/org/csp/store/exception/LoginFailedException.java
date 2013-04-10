package org.csp.store.exception;

public class LoginFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2480190610121993068L;

	public LoginFailedException() {
	}

	public LoginFailedException(String message) {
		super(message);
	}

	public LoginFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginFailedException(Throwable cause) {
		super(cause);
	}
}
