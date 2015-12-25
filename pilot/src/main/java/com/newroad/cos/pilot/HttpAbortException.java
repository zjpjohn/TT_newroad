package com.newroad.cos.pilot;

import java.io.IOException;

public class HttpAbortException extends IOException {
	private static final long serialVersionUID = -1234567890987654321L;

	public HttpAbortException() {
		super("User abort");
	}

	public HttpAbortException(String detailMessage) {
		super(detailMessage);
	}
	
	public HttpAbortException(String detailMessage,Throwable e) {
		super(detailMessage,e);
	}
}
