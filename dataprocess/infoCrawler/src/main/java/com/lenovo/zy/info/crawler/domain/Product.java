package com.lenovo.zy.info.crawler.domain;

import java.util.Date;

public class Product {

  private Long productId;

  private String productName;

  private String status;

  private String introduction;

  private Float price;

  private String source;

  private String summary;

  private Integer favoriteCount;

  private Integer commentCount;

  private Integer auditCount;

  private Float score;

  private String productLink;

  private Long publishUserId;

  private Date publishTime;

  private Long auditUserId;

  private Date auditTime;

  private String isTrial;

  private Date lastUpdateTime;

  private Long categoryId;

  public Product(String productName,String status, String summary, String introduction, Long publishUserId) {
    super();
    this.productName = productName;
    this.status = status;
    this.introduction = introduction;
    this.price = 0f;
    this.source = "Lenovo";
    this.summary = summary;
    this.favoriteCount = 0;
    this.commentCount = 0;
    this.auditCount = 0;
    this.score = 0f;
    this.publishUserId = publishUserId;
    this.publishTime = new Date();
    this.auditTime = new Date();
    this.isTrial = "1";
    this.lastUpdateTime = new Date();
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getIsTrial() {
    return isTrial;
  }

  public void setIsTrial(String isTrial) {
    this.isTrial = isTrial;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Integer getFavoriteCount() {
    return favoriteCount;
  }

  public void setFavoriteCount(Integer favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  public Integer getCommentCount() {
    return commentCount;
  }

  public void setCommentCount(Integer commentCount) {
    this.commentCount = commentCount;
  }

  public Integer getAuditCount() {
    return auditCount;
  }

  public void setAuditCount(Integer auditCount) {
    this.auditCount = auditCount;
  }

  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public String getProductLink() {
    return productLink;
  }

  public void setProductLink(String productLink) {
    this.productLink = productLink;
  }

  public Long getPublishUserId() {
    return publishUserId;
  }

  public void setPublishUserId(Long publishUserId) {
    this.publishUserId = publishUserId;
  }

  public Date getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Date publishTime) {
    this.publishTime = publishTime;
  }

  public Long getAuditUserId() {
    return auditUserId;
  }

  public void setAuditUserId(Long auditUserId) {
    this.auditUserId = auditUserId;
  }

  public Date getAuditTime() {
    return auditTime;
  }

  public void setAuditTime(Date auditTime) {
    this.auditTime = auditTime;
  }

  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }


}
