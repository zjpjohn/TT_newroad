package com.newroad.tripmaster.dao.pojo.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.newroad.tripmaster.dao.pojo.Lucker;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;

public class ProductOrder implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7767801728599584374L;

  private Long orderId;

  private String tripProductId;

  private String productName;

  // Temp
  private List<String> travelCities;

  private Integer orderType = 1;

  //unit:yuan
  private Double orderAmount;
  //unit:yuan
  private Double payAmount;

  //yoyo pay key
  private String payKey;

  //pcs
  private Integer pcs;

  private String contactName;

  private String contactPhone;

  private String contactMail;

  // status 0:not used,1:confirm without pay,2:pay deal,3:cancel,4:refund,5:complete,6:close,7:refund complete
  private Integer status;

  //payType: 1:weixinPay, 
  private Integer payType;
  
  private String payStatus; // User pay info

  // 10 wei without millisecond
  private Integer tripStartDate;

  private Integer travelDays;

  private String comment;

  // Temp
  private TripProduct tripProduct;
  // Temp
  private List<TravelUser> orderUserList;

  private Long luckerId;

  private Lucker lucker;
  // payroll user account
  private Long userId;

  private Date orderTime;

  private Date updateTime;

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getTripProductId() {
    return tripProductId;
  }

  public void setTripProductId(String tripProductId) {
    this.tripProductId = tripProductId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  public String getContactMail() {
    return contactMail;
  }

  public void setContactMail(String contactMail) {
    this.contactMail = contactMail;
  }

  public Integer getOrderType() {
    return orderType;
  }

  public void setOrderType(Integer orderType) {
    this.orderType = orderType;
  }

  public TripProduct getTripProduct() {
    return tripProduct;
  }

  public void setTripProduct(TripProduct tripProduct) {
    this.tripProduct = tripProduct;
  }

  public List<TravelUser> getOrderUserList() {
    return orderUserList;
  }

  public void setOrderUserList(List<TravelUser> orderUserList) {
    this.orderUserList = orderUserList;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
  
  public Integer getPayType() {
    return payType;
  }

  public void setPayType(Integer payType) {
    this.payType = payType;
  }

  public String getPayStatus() {
    return payStatus;
  }

  public void setPayStatus(String payStatus) {
    this.payStatus = payStatus;
  }

  public Double getOrderAmount() {
    return orderAmount;
  }

  public void setOrderAmount(Double orderAmount) {
    this.orderAmount = orderAmount;
  }

  public Double getPayAmount() {
    return payAmount;
  }

  public void setPayAmount(Double payAmount) {
    this.payAmount = payAmount;
  }

  public String getPayKey() {
    return payKey;
  }

  public void setPayKey(String payKey) {
    this.payKey = payKey;
  }

  public Integer getPcs() {
    return pcs;
  }

  public void setPcs(Integer pcs) {
    this.pcs = pcs;
  }

  public Integer getTripStartDate() {
    return tripStartDate;
  }

  public void setTripStartDate(Integer tripStartDate) {
    this.tripStartDate = tripStartDate;
  }

  public Integer getTravelDays() {
    return travelDays;
  }

  public void setTravelDays(Integer travelDays) {
    this.travelDays = travelDays;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
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

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(Date orderTime) {
    this.orderTime = orderTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public List<String> getTravelCities() {
    return travelCities;
  }

  public void setTravelCities(List<String> travelCities) {
    this.travelCities = travelCities;
  }



}
