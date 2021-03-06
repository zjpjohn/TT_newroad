package com.newroad.tripmaster.dao.pojo.order;

import java.io.Serializable;
import java.util.Date;

public class ProductOrderUser implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = -8630243274687339222L;

  private Long orderUserId;
  
  private Long travelUserId;
  
  private Long productOrderId;
  
  private Date createTime;

  public Long getOrderUserId() {
    return orderUserId;
  }

  public void setOrderUserId(Long orderUserId) {
    this.orderUserId = orderUserId;
  }

  public Long getTravelUserId() {
    return travelUserId;
  }

  public void setTravelUserId(Long travelUserId) {
    this.travelUserId = travelUserId;
  }

  public Long getProductOrderId() {
    return productOrderId;
  }

  public void setProductOrderId(Long productOrderId) {
    this.productOrderId = productOrderId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  
  
}
