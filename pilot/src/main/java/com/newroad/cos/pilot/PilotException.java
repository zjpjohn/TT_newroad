package com.newroad.cos.pilot;

import java.io.IOException;

public final class PilotException extends IOException {

  /**
	 * 
	 */
  private static final long serialVersionUID = -2844365024095904807L;

  private String url;
  private String description;
  private int errCode = 0;

  public PilotException() {
    super();
  }

  public PilotException(String message) {
    super(message);
    try {
      setErrCode(Integer.parseInt(message));
    } catch (NumberFormatException e) {
      setErrCode(0);
    }
  }

  public PilotException(String message, Throwable e) {
    super(message, e);
    try {
      setErrCode(Integer.parseInt(message));
    } catch (NumberFormatException err) {
      setErrCode(0);
    }
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getErrCode() {
    return errCode;
  }

  public void setErrCode(int errCode) {
    this.errCode = errCode;
  }
}
