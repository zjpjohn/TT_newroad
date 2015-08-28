package com.newroad.tripmaster.dao.pojo.order;

import java.io.Serializable;
import java.util.Date;

public class TravelUser implements Serializable{
  
  /**
   * 
   */
  private static final long serialVersionUID = -5119696174785512348L;

  private Long travelUserId;
  
  private String userName;
  
  private String userNamePY;
  
  private String identity;
  
  private String passport;
  
  private String zoneCode;
  
  private String mobile;
  
  private String mail;
  
  //user account 
  private Long userId;
  
  private Date createTime;
  
  private Date updateTime;

  public Long getTravelUserId() {
    return travelUserId;
  }

  public void setTravelUserId(Long travelUserId) {
    this.travelUserId = travelUserId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getUserNamePY() {
    return userNamePY;
  }

  public void setUserNamePY(String userNamePY) {
    this.userNamePY = userNamePY;
  }

  public String getIdentity() {
    return identity;
  }

  public void setIdentity(String identity) {
    this.identity = identity;
  }

  public String getPassport() {
    return passport;
  }

  public void setPassport(String passport) {
    this.passport = passport;
  }

  public String getZoneCode() {
    return zoneCode;
  }

  public void setZoneCode(String zoneCode) {
    this.zoneCode = zoneCode;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }
  
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
  
  

}
