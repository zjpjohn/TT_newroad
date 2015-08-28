package com.lenovo.zy.info.crawler.wp.domain;

import java.util.Date;

public class WPPost {
  private Long id;
  private int postAuthor;
  private Date postDate;
  private Date postDateGmt;
  private String postContent;
  private String postTitle;
  private String postExcerpt;
  private String postStatus;
  private String postName;
  private String toPing;
  private String pinged;
  private Date postModified;
  private Date postModifiedGmt;
  private String postContentFiltered;
  private Long postParent;
  private String guid;
  private String postType;
  private String postMimeType;
  private Long subjectId;
  private String subjectType;

  /**
   * Product/Article
   * 
   * @param postAuthor
   * @param postDate
   * @param postContent
   * @param postTitle
   * @param postExcerpt
   * @param postStatus
   * @param toPing
   * @param pinged
   * @param postContentFiltered
   * @param subjectId
   * @param subjectType
   */
  public WPPost(String postContent, String postTitle, String postName, Long postParent, String postType, String subjectType) {
    super();
    this.postAuthor = 1;
    this.postDate = new Date();
    this.postContent = postContent;
    this.postTitle = postTitle;
    this.postExcerpt = "";
    this.postStatus = "publish";
    this.postName = postName;
    this.postParent = postParent;
    this.toPing = "";
    this.pinged = "";
    this.postContentFiltered = "";
    this.guid = "";
    this.postType = postType;
    this.postMimeType ="";
    this.subjectType = subjectType;
  }

  /**
   * Picture/Attachment
   * 
   * @param postAuthor
   * @param postDate
   * @param postDateGmt
   * @param postContent
   * @param postTitle
   * @param postExcerpt
   * @param postStatus
   * @param postName
   * @param toPing
   * @param pinged
   * @param postModified
   * @param postModifiedGmt
   * @param postContentFiltered
   * @param postParent
   * @param guid
   * @param postType
   * @param postMimeType
   */
  public WPPost(String postContent, String postTitle, String postName, Long postParent, String guid, String postType, String postMimeType) {
    super();
    this.postAuthor = 1;
    this.postDate = new Date();
    this.postDateGmt = new Date();
    this.postContent = postContent;
    this.postTitle = postTitle;
    this.postExcerpt = " ";
    this.postStatus = "inherit";
    this.postName = postName;
    this.toPing = " ";
    this.pinged = " ";
    this.postModified = new Date();
    this.postModifiedGmt = new Date();
    this.postContentFiltered = " ";
    this.postParent = postParent;
    this.guid = guid;
    this.postType = postType;
    this.postMimeType = postMimeType;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getPostAuthor() {
    return postAuthor;
  }

  public void setPostAuthor(int postAuthor) {
    this.postAuthor = postAuthor;
  }

  public Date getPostDate() {
    return postDate;
  }

  public void setPostDate(Date postDate) {
    this.postDate = postDate;
  }

  public Date getPostDateGmt() {
    return postDateGmt;
  }

  public void setPostDateGmt(Date postDateGmt) {
    this.postDateGmt = postDateGmt;
  }

  public String getPostContent() {
    return postContent;
  }

  public void setPostContent(String postContent) {
    this.postContent = postContent;
  }

  public String getPostTitle() {
    return postTitle;
  }

  public void setPostTitle(String postTitle) {
    this.postTitle = postTitle;
  }

  public String getPostExcerpt() {
    return postExcerpt;
  }

  public void setPostExcerpt(String postExcerpt) {
    this.postExcerpt = postExcerpt;
  }

  public String getPostStatus() {
    return postStatus;
  }

  public void setPostStatus(String postStatus) {
    this.postStatus = postStatus;
  }

  public String getPostName() {
    return postName;
  }

  public void setPostName(String postName) {
    this.postName = postName;
  }

  public String getToPing() {
    return toPing;
  }

  public void setToPing(String toPing) {
    this.toPing = toPing;
  }

  public String getPinged() {
    return pinged;
  }

  public void setPinged(String pinged) {
    this.pinged = pinged;
  }

  public Date getPostModified() {
    return postModified;
  }

  public void setPostModified(Date postModified) {
    this.postModified = postModified;
  }

  public Date getPostModifiedGmt() {
    return postModifiedGmt;
  }

  public void setPostModifiedGmt(Date postModifiedGmt) {
    this.postModifiedGmt = postModifiedGmt;
  }

  public String getPostContentFiltered() {
    return postContentFiltered;
  }

  public void setPostContentFiltered(String postContentFiltered) {
    this.postContentFiltered = postContentFiltered;
  }

  public Long getPostParent() {
    return postParent;
  }

  public void setPostParent(Long postParent) {
    this.postParent = postParent;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public String getPostType() {
    return postType;
  }

  public void setPostType(String postType) {
    this.postType = postType;
  }

  public String getPostMimeType() {
    return postMimeType;
  }

  public void setPostMimeType(String postMimeType) {
    this.postMimeType = postMimeType;
  }


  public Long getSubjectId() {
    return subjectId;
  }


  public void setSubjectId(Long subjectId) {
    this.subjectId = subjectId;
  }


  public String getSubjectType() {
    return subjectType;
  }


  public void setSubjectType(String subjectType) {
    this.subjectType = subjectType;
  }


}
