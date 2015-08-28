package com.lenovo.zy.info.crawler.domain;

import java.util.Date;

public class ProductCapture {
  private Long captureId;
  private String title;
  private String link;
  private String name;
  private String source;
  private Integer type;
  private String pictureLink;
  private String content;
  private Double price;
  private String payLink;
  private String setting;
  private String tag;
  private Integer status;
  private Date createTime;

  public ProductCapture() {
    super();
  }

  public ProductCapture(String title, String link, String source, String name, Integer type,String pictureLink, String content, Double price, String payLink,
      String setting, String tag, Date createTime) {
    super();
    this.title = title;
    this.link = link;
    this.source = source;
    this.name = name;
    this.type = type;
    this.pictureLink = pictureLink;
    this.content = content;
    this.price = price;
    this.payLink = payLink;
    this.setting = setting;
    this.tag = tag;
    this.createTime = createTime;
  }
  
  public Long getCaptureId() {
    return captureId;
  }

  public void setCaptureId(Long captureId) {
    this.captureId = captureId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPictureLink() {
    return pictureLink;
  }

  public void setPictureLink(String pictureLink) {
    this.pictureLink = pictureLink;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getPayLink() {
    return payLink;
  }

  public void setPayLink(String payLink) {
    this.payLink = payLink;
  }

  public String getSetting() {
    return setting;
  }

  public void setSetting(String setting) {
    this.setting = setting;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
  
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

}
