package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.newroad.tripmaster.constant.SystemConstant;

@Entity(value = "poiRoute", noClassnameStored = true)
public class POIRoute implements Serializable, Comparable<POIRoute> {

  /**
   * 
   */
  private static final long serialVersionUID = -1342588983171595974L;

  @Id
  private String poiRouteId;

  private Integer routeType;

  private String tripRouteId;

  private String routeDay;

  private String content;

  private String description;
  
  private List<String> pictures;

  // Static POI route
  // HH:MM
  private String startTime;
  // route persistent time
  private Double duration;

  private POITraffic traffic;
  // travelPOIId/poiName/picture
  private TravelPOI travelPOI;

  private Integer status = 1;

  private Long createTime;

  private Long updateTime;

  public String getPoiRouteId() {
    return poiRouteId;
  }

  public void setPoiRouteId(String poiRouteId) {
    this.poiRouteId = poiRouteId;
  }

  public Integer getRouteType() {
    return routeType;
  }

  public void setRouteType(Integer routeType) {
    this.routeType = routeType;
  }

  public String getTripRouteId() {
    return tripRouteId;
  }

  public void setTripRouteId(String tripRouteId) {
    this.tripRouteId = tripRouteId;
  }

  public String getRouteDay() {
    return routeDay;
  }

  public void setRouteDay(String routeDay) {
    this.routeDay = routeDay;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  public List<String> getPictures() {
    return pictures;
  }

  public void setPictures(List<String> pictures) {
    this.pictures = pictures;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public Double getDuration() {
    return duration;
  }

  public void setDuration(Double duration) {
    this.duration = duration;
  }

  public POITraffic getTraffic() {
    return traffic;
  }

  public void setTraffic(POITraffic traffic) {
    this.traffic = traffic;
  }

  public TravelPOI getTravelPOI() {
    return travelPOI;
  }

  public void setTravelPOI(TravelPOI travelPOI) {
    this.travelPOI = travelPOI;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
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

  public int compareTo(POIRoute poiRoute) {

    if (this == poiRoute) {
      return 0;
    } else if (poiRoute != null) {
      int routeDay1 = Integer.valueOf(this.routeDay);
      int routeDay2 = Integer.valueOf(poiRoute.getRouteDay());
      if (this.poiRouteId.equals(poiRoute.getPoiRouteId())) {
        return 0;
      } else if (routeDay1 > routeDay2) {
        return 1;
      } else if (routeDay1 < routeDay2) {
        return -1;
      } else if (routeDay1 == routeDay2) {
        try {
          Date date1 = SystemConstant.formatter.parse(this.startTime);
          Date date2 = SystemConstant.formatter.parse(poiRoute.startTime);
          long dateLong1 = (date1.getTime() / 1000);
          long dateLong2 = (date2.getTime() / 1000);
          if (dateLong1 == dateLong2) {
            return 0;
          } else if (dateLong1 > dateLong2) {
            return 1;
          } else if (dateLong1 < dateLong2) {
            return -1;
          }
        } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    return -1;
  }
}
