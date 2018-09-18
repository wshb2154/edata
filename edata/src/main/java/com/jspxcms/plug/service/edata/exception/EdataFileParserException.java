package com.jspxcms.plug.service.edata.exception;

public class EdataFileParserException extends RuntimeException{

	public EdataFileParserException() {
		super();
	}

	public EdataFileParserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EdataFileParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public EdataFileParserException(String message) {
		super(message);
	}

	public EdataFileParserException(Throwable cause) {
		super(cause);
	}

}
