package com.newroad.tripmaster.dao.pojo;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "dictionary", noClassnameStored = true)
public class Dictionary {


  @Id
  private String dictionaryId;

  private String name;

  private Integer code;

  private Integer type;

  private String value;

  public String getDictionaryId() {
    return dictionaryId;
  }

  public void setDictionaryId(String dictionaryId) {
    this.dictionaryId = dictionaryId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
  
  

}
