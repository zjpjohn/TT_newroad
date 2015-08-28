package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.newroad.tripmaster.dao.pojo.Lucker;
import com.newroad.tripmaster.dao.pojo.info.UserBehavior;

@Entity(value = "tripProduct", noClassnameStored = true)
public class TripProduct implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6035309355545275627L;

  @Id
  private String tripProductId;

  private String productName;

  private String title;
  //
  private Integer productType;

  private String summary;

  private String picture;

  private String userStartCity;

  private Double price;
  
  private Integer priceUnit;

  private Double discount;

  private Integer peopleThreshold;

  private List<Integer> recommendPeoples;

  private List<Integer> recommendDays;

  private Integer maxInventory;

  private List<String> features;

  private List<String> costCover;

  private List<String> travelCities;
  
  private Long luckerId;

  //Temp
  private Lucker lucker;

  private String tripRouteId;

  // 0:unusable 1:publish 2:pre-submit 3:approving 4:unapproved 5:off shelves
  private Integer status;

  //Temp
  private List<TravelDateUnit> travelDateList;
  // Temp
  private CustomizeRoute tripRoute;
  // Temp
  private List<TripNotice> tripNotices;
  // Temp
  private UserBehavior userBehavior;

  private Long createTime;

  private Long updateTime;

  public String getTripProductId() {
    return tripProductId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public String getUserStartCity() {
    return userStartCity;
  }

  public void setUserStartCity(String userStartCity) {
    this.userStartCity = userStartCity;
  }

  public List<String> getTravelCities() {
    return travelCities;
  }

  public void setTravelCities(List<String> travelCities) {
    this.travelCities = travelCities;
  }

  public Integer getPeopleThreshold() {
    return peopleThreshold;
  }

  public void setPeopleThreshold(Integer peopleThreshold) {
    this.peopleThreshold = peopleThreshold;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }
  
  public Integer getPriceUnit() {
    return priceUnit;
  }

  public void setPriceUnit(Integer priceUnit) {
    this.priceUnit = priceUnit;
  }

  public Double getDiscount() {
    return discount;
  }

  public void setDiscount(Double discount) {
    this.discount = discount;
  }

  public List<Integer> getRecommendPeoples() {
    return recommendPeoples;
  }

  public void setRecommendPeoples(List<Integer> recommendPeoples) {
    this.recommendPeoples = recommendPeoples;
  }

  public List<Integer> getRecommendDays() {
    return recommendDays;
  }

  public void setRecommendDays(List<Integer> recommendDays) {
    this.recommendDays = recommendDays;
  }

  public Integer getMaxInventory() {
    return maxInventory;
  }

  public void setMaxInventory(Integer maxInventory) {
    this.maxInventory = maxInventory;
  }

  public List<String> getFeatures() {
    return features;
  }

  public void setFeatures(List<String> features) {
    this.features = features;
  }

  public List<String> getCostCover() {
    return costCover;
  }

  public void setCostCover(List<String> costCover) {
    this.costCover = costCover;
  }

  public Integer getProductType() {
    return productType;
  }

  public void setProductType(Integer productType) {
    this.productType = productType;
  }

  public String getTripRouteId() {
    return tripRouteId;
  }

  public void setTripRouteId(String tripRouteId) {
    this.tripRouteId = tripRouteId;
  }
  
  public List<TravelDateUnit> getTravelDateList() {
    return travelDateList;
  }

  public void setTravelDateList(List<TravelDateUnit> travelDateList) {
    this.travelDateList = travelDateList;
  }

  public CustomizeRoute getTripRoute() {
    return tripRoute;
  }

  public void setTripRoute(CustomizeRoute tripRoute) {
    this.tripRoute = tripRoute;
  }

  public UserBehavior getUserBehavior() {
    return userBehavior;
  }

  public void setUserBehavior(UserBehavior userBehavior) {
    this.userBehavior = userBehavior;
  }

  public List<TripNotice> getTripNotices() {
    return tripNotices;
  }

  public void setTripNotices(List<TripNotice> tripNotices) {
    this.tripNotices = tripNotices;
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
  
  public Lucker getLucker() {
    return lucker;
  }

  public void setLucker(Lucker lucker) {
    this.lucker = lucker;
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
