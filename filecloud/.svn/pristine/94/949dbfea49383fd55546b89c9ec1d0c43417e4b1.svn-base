package com.newroad.fileext.utilities;

/**
 * @info FileDataException
 * @author tangzj1
 * @date Nov 28, 2013
 * @version
 */
public class FileResourceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String returnMessage;

	public FileResourceException() {

	}

	public FileResourceException(String message) {
		super(message);
		this.returnMessage = message;
	}

	public FileResourceException(Throwable e) {
		super(e);
		this.returnMessage = e.getMessage();
	}

	public FileResourceException(String message, Throwable e) {
		super(message, e);
		this.returnMessage = e.getMessage();
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

}
