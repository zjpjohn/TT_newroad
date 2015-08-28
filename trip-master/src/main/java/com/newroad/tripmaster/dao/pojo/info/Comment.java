package com.newroad.tripmaster.dao.pojo.info;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.newroad.tripmaster.dao.pojo.SimpleUser;

@Entity(value = "comment", noClassnameStored = true)
public class Comment implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 6591587233660621668L;
  @Id
  private String commentId;
  
  private String hashsiteid;
  
  private String title;

  private String content;
  
  private String[] pictures;
  
  private String[] tags;
  
  private Long userid;
  
  private SimpleUser userInfo;
  
  private String replyCommentId;
  
  private Long createtime;
  
  private Long lastupdatedtime;
  
  // Must set constructor for morphia query iterator
  public Comment() {
    super();
  }

  public String getCommentId() {
    return commentId;
  }

  public void setCommentId(String commentId) {
    this.commentId = commentId;
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

  public Long getUserid() {
    return userid;
  }

  public void setUserid(Long userid) {
    this.userid = userid;
  }

  public String getReplyCommentId() {
    return replyCommentId;
  }

  public void setReplyCommentId(String replyCommentId) {
    this.replyCommentId = replyCommentId;
  }
  
  public SimpleUser getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(SimpleUser userInfo) {
    this.userInfo = userInfo;
  }

  public Long getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Long createtime) {
    this.createtime = createtime;
  }

  public Long getLastupdatedtime() {
    return lastupdatedtime;
  }

  public void setLastupdatedtime(Long lastupdatedtime) {
    this.lastupdatedtime = lastupdatedtime;
  }
  
  
}
