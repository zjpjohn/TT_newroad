package com.newroad.tripmaster.dao.pojo.info;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.newroad.tripmaster.dao.pojo.SimpleUser;


@Entity(value = "userBehavior", noClassnameStored = true)
public class UserBehavior implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 6591587233660621668L;
  
  @Id
  private String behaviorId;
  
  //1:like product 2:ignore product 3.tripProduct comment 4. hashsiteid comment
  private Integer actionType;
  
  private String targetId;
  
  private String actionContent;
  
  private Long userId;
  
  //temp
  private SimpleUser userInfo;
  
  private Integer status;
  
  private Long createTime;

  private Long updateTime;
  
  public UserBehavior(Integer actionType, String actionContent, String targetId, Long userId) {
    super();
    this.actionType = actionType;
    this.actionContent = actionContent;
    this.targetId = targetId;
    this.userId = userId;
  }

  public UserBehavior() {
    // TODO Auto-generated constructor stub
  }

  public String getBehaviorId() {
    return behaviorId;
  }

  public void setBehaviorId(String behaviorId) {
    this.behaviorId = behaviorId;
  }

  public Integer getActionType() {
    return actionType;
  }

  public void setActionType(Integer actionType) {
    this.actionType = actionType;
  }

  public String getActionContent() {
    return actionContent;
  }

  public void setActionContent(String actionContent) {
    this.actionContent = actionContent;
  }

  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
  
  public SimpleUser getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(SimpleUser userInfo) {
    this.userInfo = userInfo;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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
