package com.newroad.tripmaster.dao.pojo;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "news", noClassnameStored = true)
public class News implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6631676425744295249L;

  @Id
  private ObjectId id;

  private String title;

  private Integer type;

  private String link;

  private String productid;

  private Integer status;

  private Long createtime;

  private Long lastupdatetime;

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getProductid() {
    return productid;
  }

  public void setProductid(String productid) {
    this.productid = productid;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Long createtime) {
    this.createtime = createtime;
  }

  public Long getLastupdatetime() {
    return lastupdatetime;
  }

  public void setLastupdatetime(Long lastupdatetime) {
    this.lastupdatetime = lastupdatetime;
  }

}
