package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class TripDayRoute implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7759365951999813647L;

  private String tripRouteId;

  private String dayRouteTitle;
  
  private String content;

  private List<POIRoute> poiRouteList;

  public TripDayRoute(String tripRouteId, List<POIRoute> poiRouteList) {
    super();
    this.tripRouteId = tripRouteId;
    this.poiRouteList = poiRouteList;
  }

  public String getTripRouteId() {
    return tripRouteId;
  }

  public void setTripRouteId(String tripRouteId) {
    this.tripRouteId = tripRouteId;
  }

  public String getDayRouteTitle() {
    return dayRouteTitle;
  }

  public void setDayRouteTitle(String dayRouteTitle) {
    this.dayRouteTitle = dayRouteTitle;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public List<POIRoute> getPoiRouteList() {
    return poiRouteList;
  }

  public void setPoiRouteList(List<POIRoute> poiRouteList) {
    this.poiRouteList = poiRouteList;
  }

  
}
