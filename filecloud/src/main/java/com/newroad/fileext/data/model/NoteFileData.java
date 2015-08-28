package com.newroad.fileext.data.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.newroad.util.cosure.Convertable;
import com.newroad.util.cosure.Statisticable;

/**
 * resource data
 */
public class NoteFileData extends CommonFileData implements Serializable {

  private static final long serialVersionUID = 4487488225155611401L;

  private String resourceID;
  private String clientResourceID;
  private String resourceName;
  /** file content type or mix type */
  private Integer resourceType;
  private String description;
  private Long size;

  private CloudFileData fileData;
  private List<ThumbnailCache> thumbnailCacheList;
  // For note resource info
  /** pertain to the specific mixLocalId */
  private String clientMixID;
  private String position;
  private Integer startTime;
  /** 录音文件总时长（mix需求） */
  private Integer fullTime;

  // Don't need to be set every time
  private String noteID;
  private String clientNoteID;
  private String userID;
  private Integer status;
  private Long resourceVersion;

  private Long userCreateTime;
  private Long createTime;
  private Long lastUpdateTime;

  /**
   * 闭包 统计大小
   */
  public static final Statisticable<Double, NoteFileData> STATISTIC_SIZE = new Statisticable<Double, NoteFileData>() {
    @Override
    public Double statistic(Double v1, NoteFileData v2) {
      if (v1 == null) {
        v1 = 0.0d;
      }

      double v2size = v2.getSize();
      return new BigDecimal(v1).add(new BigDecimal(v2size)).doubleValue();
    }
  };

  public static final Convertable<NoteFileData, String> TO_CLIENT_ID = new Convertable<NoteFileData, String>() {
    @Override
    public String convert(NoteFileData q) {
      return q.getClientResourceID();
    }
  };

  public static NoteFileData create(String userId, CloudFileData fileData) {
    NoteFileData fileResource = new NoteFileData();
    fileResource.setUserID(userId);
    fileResource.setClientResourceID(fileData.getKey());
    fileResource.setResourceName(fileData.getFileName());
    fileResource.setResourceType(FileMimeType.getValue(fileData.getContentType()));
    fileResource.setSize(fileData.getCacheFileData().getCacheSize());
    fileResource.setFileData(fileData);

    // stand alone resource
    fileResource.setStatus(1);
    long now = System.currentTimeMillis();
    fileResource.setCreateTime(now);
    fileResource.setLastUpdateTime(now);
    fileResource.setResourceVersion(1L);
    return fileResource;
  }

  public static NoteFileData update(NoteFileData updateResource, String userID) {
    long now = System.currentTimeMillis();
    updateResource.setLastUpdateTime(now);
    updateResource.setUserID(userID);
    return updateResource;
  }

  public String getResourceID() {
    return resourceID;
  }

  public void setResourceID(String resourceID) {
    this.resourceID = resourceID;
  }

  public String getClientResourceID() {
    return clientResourceID;
  }

  public void setClientResourceID(String clientResourceID) {
    this.clientResourceID = clientResourceID;
  }

  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public Integer getResourceType() {
    return resourceType;
  }

  public void setResourceType(Integer resourceType) {
    this.resourceType = resourceType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public CloudFileData getFileData() {
    return fileData;
  }

  public void setFileData(CloudFileData fileData) {
    this.fileData = fileData;
  }

  // ThumbnailCacheList in memory
  public List<ThumbnailCache> getThumbnailCacheList() {
    return thumbnailCacheList;
  }

  public void setThumbnailCacheList(List<ThumbnailCache> thumbnailCacheList) {
    this.thumbnailCacheList = thumbnailCacheList;
  }

  public void addThumbnailCache(ThumbnailCache thumbnailCache) {
    if (thumbnailCacheList == null) {
      thumbnailCacheList = new ArrayList<ThumbnailCache>();
    }
    thumbnailCacheList.add(thumbnailCache);
  }

  public ThumbnailCache getThumbnailCache(Integer thumbnailType) {
    if (thumbnailCacheList != null && thumbnailCacheList.size() > 0) {
      for (ThumbnailCache thumbnailCache : thumbnailCacheList) {
        if (thumbnailCache == null) {
          continue;
        } else if (thumbnailType.equals(thumbnailCache.getThumbnailType())) {
          return thumbnailCache;
        }
      }
    }
    return null;
  }

  public String getClientMixID() {
    return clientMixID;
  }

  public void setClientMixID(String clientMixID) {
    this.clientMixID = clientMixID;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public Integer getStartTime() {
    return startTime;
  }

  public void setStartTime(Integer startTime) {
    this.startTime = startTime;
  }

  public Integer getFullTime() {
    return fullTime;
  }

  public void setFullTime(Integer fullTime) {
    this.fullTime = fullTime;
  }

  public String getNoteID() {
    return noteID;
  }

  public void setNoteID(String noteID) {
    this.noteID = noteID;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getResourceVersion() {
    return resourceVersion;
  }

  public void setResourceVersion(Long resourceVersion) {
    this.resourceVersion = resourceVersion;
  }

  public Long getUserCreateTime() {
    return userCreateTime;
  }

  public void setUserCreateTime(Long userCreateTime) {
    this.userCreateTime = userCreateTime;
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

  public String getClientNoteID() {
    return clientNoteID;
  }

  public void setClientNoteID(String clientNoteID) {
    this.clientNoteID = clientNoteID;
  }

}
