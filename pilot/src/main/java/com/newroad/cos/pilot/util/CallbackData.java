package com.newroad.cos.pilot.util;

import java.io.Serializable;
import java.util.Map;

public class CallbackData implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6092118643277085309L;

  private String cburl;
  private Map<String, String> cbdata;
  private Map<String, String> headerData;

  public CallbackData() {}

  public void setUrl(String url) {
    cburl = url;
  }

  public void setData(Map<String, String> data) {
    cbdata = data;
  }

  public String getUrl() {
    return cburl;
  }

  public Map<String, String> getData() {
    return cbdata;
  }

  public void setHeaderData(Map<String, String> hData) {
    headerData = hData;
  }

  public Map<String, String> getHeaderData() {
    return headerData;
  }
}
