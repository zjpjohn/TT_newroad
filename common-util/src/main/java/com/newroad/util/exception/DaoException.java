package com.newroad.util.exception;

/**
 * DAO Exception description
 * @author tangzj1
 *
 */
public class DaoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String returnMessage ;
	
	public DaoException(){
		
	}
	
	public DaoException(String message) {
		super(message);
		this.returnMessage =message;
	}
	
	public DaoException(Throwable e) {
		super(e);
		this.returnMessage = e.getMessage();
	}
	
	public DaoException(String message,Throwable e) {
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
