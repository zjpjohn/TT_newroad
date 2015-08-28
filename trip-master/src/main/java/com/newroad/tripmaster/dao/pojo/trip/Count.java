package com.newroad.tripmaster.dao.pojo.trip;

public class Count {
  private Object status;
  private Integer count;

  public Count(Object status, Integer count) {
    super();
    this.status = status;
    this.count = count;
  }


  public Object getStatus() {
    return status;
  }


  public void setStatus(Integer status) {
    this.status = status;
  }


  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }



}
