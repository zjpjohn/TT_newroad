package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class TravelHotel implements Serializable{

  
  /**
   * 
   */
  private static final long serialVersionUID = -1037875747220057644L;

  private String hotelName;
  
  private String hotelSummary;
  
  private List<String> equipments;
  
  private Map<String,String> roomType;
  
  private Map<String,String> policy;
  
  
  public String getHotelName() {
    return hotelName;
  }

  public void setHotelName(String hotelName) {
    this.hotelName = hotelName;
  }

  public String getHotelSummary() {
    return hotelSummary;
  }

  public void setHotelSummary(String hotelSummary) {
    this.hotelSummary = hotelSummary;
  }

  public List<String> getEquipments() {
    return equipments;
  }

  public void setEquipments(List<String> equipments) {
    this.equipments = equipments;
  }

  public Map<String, String> getRoomType() {
    return roomType;
  }

  public void setRoomType(Map<String, String> roomType) {
    this.roomType = roomType;
  }

  public Map<String, String> getPolicy() {
    return policy;
  }

  public void setPolicy(Map<String, String> policy) {
    this.policy = policy;
  }

}
