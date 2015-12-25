package com.newroad.cos.pilot;


public class AuthorizationException extends RuntimeException {
	private static final long serialVersionUID = -9876543210L;

	public AuthorizationException() {
		super("Authorization exception");
	}

	public AuthorizationException(String detailMessage) {
		super(detailMessage);
	}
	
	public AuthorizationException(String detailMessage,Throwable e) {
		super(detailMessage,e);
	}
}
