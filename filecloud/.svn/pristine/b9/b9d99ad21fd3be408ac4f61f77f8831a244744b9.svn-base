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
  private Integer contentType;
  private Long size;
  
  private String hash;
  private Integer width;
  private Integer height;

  private String ownerId;
  private Integer status;
  private Integer version;
  private Long createTime;
  private Long lastUpdateTime;
  
  private CloudFileData cloudFileData;

  public static CommonFileData create(String userId,CloudFileData cloudFileData) {
    CommonFileData fileResource = new CommonFileData();
    fileResource.setOwnerId(userId);
    fileResource.setFileId(cloudFileData.getKeyId());
    fileResource.setFileName(cloudFileData.getFileName());
    fileResource.setContentType(FileMimeType.getValue(cloudFileData.getContentType()));
    fileResource.setSize(cloudFileData.getCacheFileData().getCacheSize());
    
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

  public Integer getContentType() {
    return contentType;
  }

  public void setContentType(Integer contentType) {
    this.contentType = contentType;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
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

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getHash() {
    if (hash == null) {
      hash = Integer.valueOf(hashCode()).toString();
    }
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
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
    result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
    result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
    result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
    result = prime * result + ((size == null) ? 0 : size.hashCode());
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
    if (contentType == null) {
      if (other.contentType != null)
        return false;
    } else if (!contentType.equals(other.contentType))
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
    if (size == null) {
      if (other.size != null)
        return false;
    } else if (!size.equals(other.size))
      return false;
    return true;
  }



}
