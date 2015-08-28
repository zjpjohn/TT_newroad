package com.newroad.fileext.data.model;

import java.io.Serializable;

public class CommonFileData implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3198946079083073498L;

  private String fileId;
  private String fileName;
  private Integer fileType;
  private String location;
  private Long userId;
  private Integer status;
  private Integer version;
  private Long createTime;
  private Long lastUpdateTime;
  
  private CloudFileData cloudFileData;

  public static CommonFileData create(Long userId,CloudFileData cloudFileData) {
    CommonFileData fileResource = new CommonFileData();
  
    fileResource.setFileName(cloudFileData.getKey());
    fileResource.setFileType(1);
    fileResource.setCloudFileData(cloudFileData);
    fileResource.setUserId(userId);
    // stand alone resource
    fileResource.setStatus(1);
    long now = System.currentTimeMillis();
    fileResource.setCreateTime(now);
    fileResource.setLastUpdateTime(now);
    return fileResource;
  }
  
  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Integer getFileType() {
    return fileType;
  }

  public void setFileType(Integer fileType) {
    this.fileType = fileType;
  }
  
  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
  
  public CloudFileData getCloudFileData() {
    return cloudFileData;
  }

  public void setCloudFileData(CloudFileData cloudFileData) {
    this.cloudFileData = cloudFileData;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  public Long getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Long lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
    result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
    result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CommonFileData other = (CommonFileData) obj;
    if (fileId == null) {
      if (other.fileId != null)
        return false;
    } else if (!fileId.equals(other.fileId))
      return false;
    if (createTime == null) {
      if (other.createTime != null)
        return false;
    } else if (!createTime.equals(other.createTime))
      return false;
    if (fileName == null) {
      if (other.fileName != null)
        return false;
    } else if (!fileName.equals(other.fileName))
      return false;
    return true;
  }



}
