package com.newroad.tripmaster.info.crawler.domain;

import java.io.Serializable;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "site", noClassnameStored = true)
public class Site implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6591587233660621668L;
  @Id
  private String siteId;

  @Embedded
  private Coordinate gravity;
  private List<Coordinate> range;
  private List<Site> childSiteList;

  private String name;
  private String description;
  // building,area
  private Integer sitetype;
  private String marktype;
  private String picture;
  private String parentsiteid;
  private String hashcode;
  private String country;
  private String city;
  private String street;
  private Long userid;
  private String username;
  private String usericon;
  private Long createtime;
  private Long lastupdatedtime;

  public String getSiteId() {
    return siteId.toString();
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public List<Coordinate> getRange() {
    return range;
  }

  public void setRange(List<Coordinate> range) {
    this.range = range;
  }


  public List<Site> getChildSiteList() {
    return childSiteList;
  }

  public void setChildSiteList(List<Site> childSiteList) {
    this.childSiteList = childSiteList;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Coordinate getGravity() {
    return gravity;
  }

  public void setGravity(Coordinate gravity) {
    this.gravity = gravity;
  }

  public Integer getSitetype() {
    return sitetype;
  }

  public void setSitetype(Integer sitetype) {
    this.sitetype = sitetype;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMarktype() {
    return marktype;
  }

  public void setMarktype(String marktype) {
    this.marktype = marktype;
  }



  public String getPicture() {
    return picture;
  }


  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getParentsiteid() {
    return parentsiteid;
  }

  public void setParentsiteid(String parentsiteid) {
    this.parentsiteid = parentsiteid;
  }

  public String getHashcode() {
    return hashcode;
  }

  public void setHashcode(String hashcode) {
    this.hashcode = hashcode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public Long getUserid() {
    return userid;
  }


  public void setUserid(Long userid) {
    this.userid = userid;
  }


  public String getUsername() {
    return username;
  }


  public void setUsername(String username) {
    this.username = username;
  }


  public String getUsericon() {
    return usericon;
  }


  public void setUsericon(String usericon) {
    this.usericon = usericon;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + ((createtime == null) ? 0 : createtime.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((gravity == null) ? 0 : gravity.hashCode());
    result = prime * result + ((marktype == null) ? 0 : marktype.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((parentsiteid == null) ? 0 : parentsiteid.hashCode());
    result = prime * result + ((picture == null) ? 0 : picture.hashCode());
    result = prime * result + ((range == null) ? 0 : range.hashCode());
    result = prime * result + ((sitetype == null) ? 0 : sitetype.hashCode());
    result = prime * result + ((street == null) ? 0 : street.hashCode());
    result = prime * result + ((userid == null) ? 0 : userid.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Site other = (Site) obj;
    if (city == null) {
      if (other.city != null)
        return false;
    } else if (!city.equals(other.city))
      return false;
    if (country == null) {
      if (other.country != null)
        return false;
    } else if (!country.equals(other.country))
      return false;
    if (createtime == null) {
      if (other.createtime != null)
        return false;
    } else if (!createtime.equals(other.createtime))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (gravity == null) {
      if (other.gravity != null)
        return false;
    } else if (!gravity.equals(other.gravity))
      return false;
    if (marktype == null) {
      if (other.marktype != null)
        return false;
    } else if (!marktype.equals(other.marktype))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (picture == null) {
      if (other.picture != null)
        return false;
    } else if (!picture.equals(other.picture))
      return false;
    if (range == null) {
      if (other.range != null)
        return false;
    } else if (!range.equals(other.range))
      return false;
    if (sitetype == null) {
      if (other.sitetype != null)
        return false;
    } else if (!sitetype.equals(other.sitetype))
      return false;
    if (street == null) {
      if (other.street != null)
        return false;
    } else if (!street.equals(other.street))
      return false;
    if (userid == null) {
      if (other.userid != null)
        return false;
    } else if (!userid.equals(other.userid))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Site [siteId=" + siteId + ", gravity=" + gravity + ", range=" + range + ", name=" + name + ", description=" + description
        + ", sitetype=" + sitetype + ", marktype=" + marktype + ", picture=" + picture + ", country=" + country + ", city=" + city
        + ", street=" + street + ", userid=" + userid + ", createtime=" + createtime + "]";
  }



}
