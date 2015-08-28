package com.newroad.tripmaster.dao.pojo.trip;

import java.io.Serializable;
import java.util.Date;


public class TripCityDict implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = -3610435560099518190L;

  private Long cityId;
  
  private String cityCode;
  
  private String cityName;
  
  private Integer cityType;
  
  private String city2Code;
  
  private String city2Name;
  
  private String provinceCode;
  
  private String province;
  
  private String countryCode;
  
  private String country;

  private String continentCode;
  
  private String continent;
  
  private Date createTime;

  public Long getCityId() {
    return cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public Integer getCityType() {
    return cityType;
  }

  public void setCityType(Integer cityType) {
    this.cityType = cityType;
  }

  public String getCity2Code() {
    return city2Code;
  }

  public void setCity2Code(String city2Code) {
    this.city2Code = city2Code;
  }

  public String getCity2Name() {
    return city2Name;
  }

  public void setCity2Name(String city2Name) {
    this.city2Name = city2Name;
  }

  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getContinentCode() {
    return continentCode;
  }

  public void setContinentCode(String continentCode) {
    this.continentCode = continentCode;
  }

  public String getContinent() {
    return continent;
  }

  public void setContinent(String continent) {
    this.continent = continent;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  
  
}
