package org.csp.exception;

public class InvalidParamterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3466799295328698923L;

	public InvalidParamterException() {
		super();
	}

	public InvalidParamterException(String message) {
		super(message);
	}

	public InvalidParamterException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidParamterException(Throwable cause) {
		super(cause);
	}
	
}
