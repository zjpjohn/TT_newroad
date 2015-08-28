package com.newroad.tripmaster.info.crawler.domain;

import java.io.Serializable;
import java.util.List;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class ScenicGuide implements Serializable{

   /**
   * 
   */
  private static final long serialVersionUID = 6496422660129350049L;

  private List<String> path;
  
   private List<String> route;
   
   private Double price;
   
   private String tip;
   
   private Integer favoritecount;
   
   private Integer arrivecount;
   
   private Integer commentcount;

  public List<String> getPath() {
    return path;
  }

  public void setPath(List<String> path) {
    this.path = path;
  }

  public List<String> getRoute() {
    return route;
  }

  public void setRoute(List<String> route) {
    this.route = route;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public Integer getFavoritecount() {
    return favoritecount;
  }

  public void setFavoritecount(Integer favoritecount) {
    this.favoritecount = favoritecount;
  }

  public Integer getArrivecount() {
    return arrivecount;
  }

  public void setArrivecount(Integer arrivecount) {
    this.arrivecount = arrivecount;
  }

  public Integer getCommentcount() {
    return commentcount;
  }

  public void setCommentcount(Integer commentcount) {
    this.commentcount = commentcount;
  }
   
   
}
