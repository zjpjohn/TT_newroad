package com.newroad.util.exception;

public class COSException extends RuntimeException {
	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	private String returnMessage;

	public COSException() {

	}

	public COSException(String massage) {
		this.returnMessage = massage;
	}

	public COSException(Throwable e) {
		super(e);
		this.returnMessage = e.getMessage();
	}

	public COSException(String message, Throwable e) {
		super(message, e);
		this.returnMessage = message;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
}
