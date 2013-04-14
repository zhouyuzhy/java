package org.csp.exception;

public class CryptoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3466799295328698923L;

	public CryptoException() {
		super();
	}

	public CryptoException(String message) {
		super(message);
	}

	public CryptoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CryptoException(Throwable cause) {
		super(cause);
	}
	
}
