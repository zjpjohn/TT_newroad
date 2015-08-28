package com.newroad.util.exception;

public class AuthException extends RuntimeException {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String returnMessage;

  public AuthException() {

  }

  public AuthException(String massage) {
    this.returnMessage = massage;
  }

  public AuthException(Throwable e) {
    super(e);
    this.returnMessage = e.getMessage();
  }

  public AuthException(String message, Throwable e) {
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
