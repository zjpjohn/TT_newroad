package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class DateInfo implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 8859058458629650164L;
  private String date;
  private Integer maxInventory;
  private Integer buyQuantity;
  //private Integer available;
  private Double price;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getMaxInventory() {
    return maxInventory;
  }

  public void setMaxInventory(Integer maxInventory) {
    this.maxInventory = maxInventory;
  }

  public Integer getBuyQuantity() {
    return buyQuantity;
  }

  public void setBuyQuantity(Integer buyQuantity) {
    this.buyQuantity = buyQuantity;
  }

}
