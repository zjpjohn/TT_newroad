package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.newroad.tripmaster.dao.pojo.Coordinate;

@Entity(value = "travelPOI", noClassnameStored = true)
public class TravelPOI implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4939286450442101566L;

  @Id
  private String travelPOIId;

  private String poiName;

  private String hashsiteid;

  @Embedded
  private Coordinate coordinate;

  private String poiType;

  //fileId,fileURL
  private String picture;

  private String description;

  private String feature;

  private String cityCode;

  private String address;

  private String contact;

  private Integer level;

  @Embedded
  private TravelHotel travelHotel;

  // 0:unusable 1:usable
  private Integer status;

  private Long luckerId;

  private Long createTime;

  private Long updateTime;


  public String getTravelPOIId() {
    return travelPOIId;
  }



  public void setTravelPOIId(String travelPOIId) {
    this.travelPOIId = travelPOIId;
  }



  public String getPoiName() {
    return poiName;
  }



  public void setPoiName(String poiName) {
    this.poiName = poiName;
  }



  public String getHashsiteid() {
    return hashsiteid;
  }



  public void setHashsiteid(String hashsiteid) {
    this.hashsiteid = hashsiteid;
  }



  public Coordinate getCoordinate() {
    return coordinate;
  }



  public void setCoordinate(Coordinate coordinate) {
    this.coordinate = coordinate;
  }



  public String getPoiType() {
    return poiType;
  }



  public void setPoiType(String poiType) {
    this.poiType = poiType;
  }



  public String getPicture() {
    return picture;
  }



  public void setPicture(String picture) {
    this.picture = picture;
  }



  public String getDescription() {
    return description;
  }



  public void setDescription(String description) {
    this.description = description;
  }

  public String getFeature() {
    return feature;
  }



  public void setFeature(String feature) {
    this.feature = feature;
  }



  public String getCityCode() {
    return cityCode;
  }



  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  public String getAddress() {
    return address;
  }



  public void setAddress(String address) {
    this.address = address;
  }

  public String getContact() {
    return contact;
  }



  public void setContact(String contact) {
    this.contact = contact;
  }



  public Integer getLevel() {
    return level;
  }



  public void setLevel(Integer level) {
    this.level = level;
  }



  public TravelHotel getTravelHotel() {
    return travelHotel;
  }



  public void setTravelHotel(TravelHotel travelHotel) {
    this.travelHotel = travelHotel;
  }


  public Integer getStatus() {
    return status;
  }



  public void setStatus(Integer status) {
    this.status = status;
  }


  public Long getLuckerId() {
    return luckerId;
  }



  public void setLuckerId(Long luckerId) {
    this.luckerId = luckerId;
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



  class Hotel {
    private Integer roomType;
    private String service;

    private Boolean cancelRequirment;

    private Boolean wifi;

    public Integer getRoomType() {
      return roomType;
    }

    public void setRoomType(Integer roomType) {
      this.roomType = roomType;
    }

    public String getService() {
      return service;
    }

    public void setService(String service) {
      this.service = service;
    }

    public Boolean getCancelRequirment() {
      return cancelRequirment;
    }

    public void setCancelRequirment(Boolean cancelRequirment) {
      this.cancelRequirment = cancelRequirment;
    }

    public Boolean getWifi() {
      return wifi;
    }

    public void setWifi(Boolean wifi) {
      this.wifi = wifi;
    }

  }
}
