package com.lenovo.zy.info.crawler.domain;

import java.util.Date;
import java.util.List;

public class Test {
  private Long testId;
  
  private String title;
  
  private String content;
  
  private String image;
  
  private Integer status;
  
  private Date createTime;
  
  private Date updateTime;
  
  private List<TestAnswer> answerList;

  public Long getTestId() {
    return testId;
  }

  public void setTestId(Long testId) {
    this.testId = testId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public List<TestAnswer> getAnswerList() {
    return answerList;
  }

  public void setAnswerList(List<TestAnswer> answerList) {
    this.answerList = answerList;
  }
  
  
}
