package com.newroad.util.exception;

/**
 * Service Exception description
 * @author tangzj1
 *
 */
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String returnMessage;

	public ServiceException() {

	}

	public ServiceException(String massage) {
		this.returnMessage = massage;
	}

	public ServiceException(Throwable e) {
		super(e);
		this.returnMessage = e.getMessage();
	}

	public ServiceException(String message,Throwable e) {
		super(message,e);
		this.returnMessage = message;
	}
	
	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
}
