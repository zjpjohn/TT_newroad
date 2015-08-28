package com.newroad.tripmaster.dao.pojo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "scenic", noClassnameStored = true)
public class Scenic implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6035309355545275627L;

  @Id
  private String scenicId;

  private String hashsiteid;

  private String parentid;

  private String name;

  private String summary;

  private String description;

  private Integer type;

  private Integer grade;

  private String picture;

  private String[] carouselpic;

  private String country;

  private String city;

  private String district;

  private String address;

  @Embedded
  private ScenicGuide scenicguide;

  @Embedded
  private ScenicNearby scenicnearby;

  private List<Scenic> childScenic;

  private Long userid;

  private String username;

  private String usericon;

  private Long createtime;

  private Long lastupdatedtime;

  
  // Must set constructor for morphia query iterator
  public Scenic() {
    super();
  }

  public String getScenicId() {
    return scenicId.toString();
  }

  public void setScenicId(String scenicId) {
    this.scenicId = scenicId;
  }

  public String getHashsiteid() {
    return hashsiteid;
  }

  public void setHashsiteid(String hashsiteid) {
    this.hashsiteid = hashsiteid;
  }

  public String getParentid() {
    return parentid;
  }

  public void setParentid(String parentid) {
    this.parentid = parentid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getGrade() {
    return grade;
  }

  public void setGrade(Integer grade) {
    this.grade = grade;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String[] getCarouselpic() {
    return carouselpic;
  }

  public void setCarouselpic(String[] carouselpic) {
    this.carouselpic = carouselpic;
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

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public ScenicGuide getScenicguide() {
    return scenicguide;
  }

  public void setScenicguide(ScenicGuide scenicguide) {
    this.scenicguide = scenicguide;
  }

  public ScenicNearby getScenicnearby() {
    return scenicnearby;
  }

  public void setScenicnearby(ScenicNearby scenicnearby) {
    this.scenicnearby = scenicnearby;
  }

  public List<Scenic> getChildScenic() {
    return childScenic;
  }

  public void setChildScenic(List<Scenic> childScenic) {
    this.childScenic = childScenic;
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
  public String toString() {
    return "Scenic [hashsiteid=" + hashsiteid + ", name=" + name + ", summary=" + summary + ", description=" + description + ", type=" + type
        + ", grade=" + grade + ", picture=" + picture + ", carouselpic=" + Arrays.toString(carouselpic) + ", country=" + country
        + ", city=" + city + ", district=" + district + ", address=" + address + ", scenicguide=" + scenicguide + ", scenicnearby="
        + scenicnearby + ", userid=" + userid + ", username=" + username + ", usericon=" + usericon + ", createtime=" + createtime
        + ", lastupdatedtime=" + lastupdatedtime + "]";
  }

}
