package com.lenovo.zy.info.crawler.domain;

import java.util.Date;

public class Photo {

  private Long photoId;

  private String url;

  private String status;

  private Date creatTime;

  public Photo(String url) {
    super();
    this.url = url;
    this.status = "1";
  }

  public Long getPhotoId() {
    return photoId;
  }

  public void setPhotoId(Long photoId) {
    this.photoId = photoId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getCreatTime() {
    return creatTime;
  }

  public void setCreatTime(Date creatTime) {
    this.creatTime = creatTime;
  }


}
