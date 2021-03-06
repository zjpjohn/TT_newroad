package com.newroad.tripmaster.dao.pojo.info;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.newroad.tripmaster.dao.pojo.Coordinate;
import com.newroad.tripmaster.dao.pojo.SimpleUser;

@Entity(value="tips",noClassnameStored=true)
public class Tips implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6035309355545275627L;

  @Id
  private String tipsId;

  private String hashsiteid;
  
  private String title;

  private String content;
  
  private String address;
  
  @Embedded
  private Coordinate coordinate;
  
  private String[] pictures;
  
  private String[] tags;
  
  private Integer useful=0;
  
  private Integer useless=0;
  
  private Long userId;
  
  private SimpleUser userInfo;
  
  private Long createTime;
  
  private Long updateTime;

  private Integer supporttype;
  
  // Must set constructor for morphia query iterator
  public Tips() {
    super();
  }


  public String getTipsId() {
    return tipsId.toString();
  }


  public void setTipsId(String tipsId) {
    this.tipsId = tipsId;
  }


  public String getHashsiteid() {
    return hashsiteid;
  }

  
  public void setHashsiteid(String hashsiteid) {
    this.hashsiteid = hashsiteid;
  }

  
  public String getTitle() {
    return title;
  }

  
  public void setTitle(String title) {
    this.title = title;
  }

  
  public String getContent() {
    return content;
  }

  
  public void setContent(String content) {
    this.content = content;
  }

  
  public String getAddress() {
    return address;
  }

  
  public void setAddress(String address) {
    this.address = address;
  }

  
  public Coordinate getCoordinate() {
    return coordinate;
  }

  
  public void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }

  
  public String[] getPictures() {
    return pictures;
  }

  
  public void setPictures(String[] pictures) {
    this.pictures = pictures;
  }

  
  public String[] getTags() {
    return tags;
  }

  
  public void setTags(String[] tags) {
    this.tags = tags;
  }

  
  public int getUseful() {
    return useful;
  }

  
  public void setUseful(int useful) {
    this.useful = useful;
  }

  
  public int getUseless() {
    return useless;
  }

  
  public void setUseless(int useless) {
    this.useless = useless;
  }

 

  public Long getUserId() {
    return userId;
  }


  public void setUserId(Long userId) {
    this.userId = userId;
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


  public SimpleUser getUserInfo() {
    return userInfo;
  }


  public void setUserInfo(SimpleUser userInfo) {
    this.userInfo = userInfo;
  }

  public Integer getSupporttype() {
    return supporttype;
  }


  public void setSupporttype(Integer supporttype) {
    this.supporttype = supporttype;
  }
  
}
