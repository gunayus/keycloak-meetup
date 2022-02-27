package org.springmeetup.backend.backendspringboot.security.exception;

public class UnauthorizedException extends RuntimeException {

	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(Exception cause) {
		super(cause);
	}

	public UnauthorizedException(String message, Exception cause) {
		super(message, cause);
	}
}
