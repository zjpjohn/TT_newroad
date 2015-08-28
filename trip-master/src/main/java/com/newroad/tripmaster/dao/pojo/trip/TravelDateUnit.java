package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "travelDate", noClassnameStored = true)
public class TravelDateUnit implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 806887993657382585L;

  @Id
  private String travelDateId;

  private String tripProductId;

  private String yearMonth;

  private List<DateInfo> dateList;

  private Integer status;

  private Long updateTime;

  public String getTravelDateId() {
    return travelDateId;
  }

  public void setTravelDateId(String travelDateId) {
    this.travelDateId = travelDateId;
  }

  public String getTripProductId() {
    return tripProductId;
  }

  public void setTripProductId(String tripProductId) {
    this.tripProductId = tripProductId;
  }

  public String getYearMonth() {
    return yearMonth;
  }

  public void setYearMonth(String yearMonth) {
    this.yearMonth = yearMonth;
  }

  public List<DateInfo> getDateList() {
    for (DateInfo dateInfo : dateList) {
      if (dateInfo.getBuyQuantity() == null) {
        dateInfo.setBuyQuantity(0);
      }
    }
    return dateList;
  }

  public void setDateList(List<DateInfo> dateList) {
    this.dateList = dateList;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Long updateTime) {
    this.updateTime = updateTime;
  }


}
