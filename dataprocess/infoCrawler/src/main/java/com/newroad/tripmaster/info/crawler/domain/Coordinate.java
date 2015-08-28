package com.newroad.tripmaster.info.crawler.domain;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class Coordinate implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4721801028140773057L;
  // Point,Place
  private Integer sitetype = 1;
  // longitude
  private Double lng;
  // latitude
  private Double lat;
  // unit is kilometer
  private Double radius = 1d;

  public Integer getSitetype() {
    return sitetype;
  }

  public void setSitetype(Integer sitetype) {
    this.sitetype = sitetype;
  }

  public Double getLng() {
    return lng;
  }

  public void setLng(Double lng) {
    this.lng = lng;
  }

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public Double getRadius() {
    return radius;
  }

  public void setRadius(Double radius) {
    this.radius = radius;
  }

  public Double getDistance() {
    return radius / 6371;
  }
}
