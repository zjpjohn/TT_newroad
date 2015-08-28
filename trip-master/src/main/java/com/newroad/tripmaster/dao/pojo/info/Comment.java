package com.newroad.tripmaster.dao.pojo.info;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;

@Entity(value = "comment", noClassnameStored = true)
public class Comment extends UserBehavior implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 6591587233660621668L;
  
  private String title;

  private String content;
  
  private String[] pictures;
  
  private String[] tags;
  
  private String replyCommentId;
  
  // Must set constructor for morphia query iterator
  public Comment() {
    super();
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

  public String getReplyCommentId() {
    return replyCommentId;
  }

  public void setReplyCommentId(String replyCommentId) {
    this.replyCommentId = replyCommentId;
  }

}
