package com.cfm.bankinterface.exception;

public class ValidateException extends Exception {

	private static final long serialVersionUID = -2873134250879739462L;

	public ValidateException() {
		super();
	}

	public ValidateException(String message) {
		super(message);
	}

	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateException(Throwable cause) {
		super(cause);
	}

}
