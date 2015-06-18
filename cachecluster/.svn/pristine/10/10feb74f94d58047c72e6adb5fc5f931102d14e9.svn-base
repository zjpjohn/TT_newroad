package com.newroad.cache.common.couchbase;

/**
 * Cache Exception description
 * @author tangzj1
 *
 */
public class CacheException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String returnMessage;

	public CacheException() {

	}

	public CacheException(String massage) {
		this.returnMessage = massage;
	}

	public CacheException(Throwable e) {
		super(e);
		this.returnMessage = e.getMessage();
	}

	public CacheException(String message,Throwable e) {
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
