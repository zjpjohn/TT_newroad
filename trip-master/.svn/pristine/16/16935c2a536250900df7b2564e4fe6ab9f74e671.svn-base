package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class POITraffic implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8332338496511055534L;
  // Traffic Code
  private String code;
  // site traffic link map <poiName,poiId>
  private List<String> link;
  

  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public List<String> getLink() {
    return link;
  }
  public void setLink(List<String> link) {
    this.link = link;
  }

}
