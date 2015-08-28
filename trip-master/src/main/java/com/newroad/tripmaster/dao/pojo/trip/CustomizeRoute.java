package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "customizeRoute", noClassnameStored = true)
public class CustomizeRoute implements Serializable{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5556816400055635825L;

  @Id
  private String tripRouteId;
  
  private String routeName;

  private String description;
  
  //trip time duration
  private Integer duration;
  
  //fileId,fileURL
  private List<String> carouselPics;
  
  private List<String> travelCities;
  
  //route map
  private String routeMap;
  
  private List<String> routeTags;
  
  //routeDay,dayInfo
  private Map<String,String> dayRouteInfo;
  
  //0:unusable 1:usable 2:pre-submit 
  private Integer status;
  //daren userId
  private Long luckerId;
  
  private Long createTime;
  
  private Long updateTime;
  
  //Temp
  private List<String> travelPOIs; 

  //Temp POIRouteInfo & roundDay
  //private List<POIRoute> poiRouteList;
  
  //Temp
  private Map<String,TripDayRoute> dayRouteMap;
  
  // Must set constructor for morphia query iterator
  public CustomizeRoute() {
    super();
  }
  
  public CustomizeRoute(String routeName, Long luckerId) {
    super();
    this.routeName = routeName;
    this.luckerId = luckerId;
  }

  public String getTripRouteId() {
    return tripRouteId;
  }

  public String getRouteName() {
    return routeName;
  }

  public void setRouteName(String routeName) {
    this.routeName = routeName;
  }
  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public List<String> getCarouselPics() {
    return carouselPics;
  }

  public void setCarouselPics(List<String> carouselPics) {
    this.carouselPics = carouselPics;
  }

  public List<String> getTravelCities() {
    return travelCities;
  }

  public void setTravelCities(List<String> travelCities) {
    this.travelCities = travelCities;
  }

  public String getRouteMap() {
    return routeMap;
  }

  public void setRouteMap(String routeMap) {
    this.routeMap = routeMap;
  }

  public List<String> getRouteTags() {
    return routeTags;
  }

  public void setRouteTags(List<String> routeTags) {
    this.routeTags = routeTags;
  }
  
  public List<String> getTravelPOIs() {
    return travelPOIs;
  }

  public void setTravelPOIs(List<String> travelPOIs) {
    this.travelPOIs = travelPOIs;
  }

  public Map<String, String> getDayRouteInfo() {
    return dayRouteInfo;
  }

  public void setDayRouteInfo(Map<String, String> dayRouteInfo) {
    this.dayRouteInfo = dayRouteInfo;
  }

//  public List<POIRoute> getPoiRouteList() {
//    return poiRouteList;
//  }
//
//  public void setPoiRouteList(List<POIRoute> poiRouteList) {
//    this.poiRouteList = poiRouteList;
//  }
  
  public Map<String, TripDayRoute> getDayRouteMap() {
    return dayRouteMap;
  }

  
  public void setDayRouteMap(Map<String, TripDayRoute> dayRouteMap) {
    this.dayRouteMap = dayRouteMap;
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
  
  
}
