package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "tripNotice", noClassnameStored = true)
public class TripNotice implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7759365951999813647L;

  @Id
  private String noticeId;

  private String noticeName;

  private String noticeType;

  private List<String> noticeContents;

  private String tripProductId;
  
  private Long luckerId;

  private Long createTime;

  private Long updateTime;


  public String getNoticeId() {
    return noticeId;
  }

  public void setNoticeId(String noticeId) {
    this.noticeId = noticeId;
  }

  public String getNoticeName() {
    return noticeName;
  }

  public void setNoticeName(String noticeName) {
    this.noticeName = noticeName;
  }

  public String getNoticeType() {
    return noticeType;
  }

  public void setNoticeType(String noticeType) {
    this.noticeType = noticeType;
  }

  public List<String> getNoticeContents() {
    return noticeContents;
  }

  public void setNoticeContents(List<String> noticeContents) {
    this.noticeContents = noticeContents;
  }

  public String getTripProductId() {
    return tripProductId;
  }

  public void setTripProductId(String tripProductId) {
    this.tripProductId = tripProductId;
  }

  public Long getLuckerId() {
    return luckerId;
  }

  public void setLuckerId(Long luckerId) {
    this.luckerId = luckerId;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }


}
