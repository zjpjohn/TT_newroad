package com.newroad.fileext.data.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lenovo.pilot.util.CallbackData;

/**
 * @Info basic file data object for upload/download/cloud storage 
 * @author tangzj1
 */
public class CloudFileData implements Serializable {


  private static final long serialVersionUID = -8758508746358605847L;

  private String token;
  private String bucketName;
  private String keyId;
  private String fileName;
  private String contentType;
  private String link;
  private String publicLink;

  private ThumbnailCache currentThumbnailCache;
  private Map<ThumbnailType, ThumbnailCache> thumbnailCacheMap;

  // callback status
  private Long cloudSize;
  private Integer cloudStatus;

  private CacheFileData cacheFileData;

  // Lenovo COS callback data
  private CallbackData callback;

  public static String generateKeyId() {
    UUID randomUUID = UUID.randomUUID();
    String key = randomUUID.toString().replaceAll("-", "");
    return key;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getPublicLink() {
    return publicLink;
  }

  public void setPublicLink(String publicLink) {
    this.publicLink = publicLink;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Long getCloudSize() {
    return cloudSize;
  }

  public void setCloudSize(Long cloudSize) {
    this.cloudSize = cloudSize;
  }

  public ThumbnailCache getCurrentThumbnailCache() {
    return currentThumbnailCache;
  }

  public void setCurrentThumbnailCache(ThumbnailCache currentThumbnailCache) {
    this.currentThumbnailCache = currentThumbnailCache;
  }

  public Map<ThumbnailType, ThumbnailCache> getThumbnailCacheMap() {
    return thumbnailCacheMap;
  }

  public void setThumbnailCacheMap(ThumbnailCache thumbnailCache) {
    if (thumbnailCacheMap == null) {
      thumbnailCacheMap = new HashMap<ThumbnailType, ThumbnailCache>();
    }
    thumbnailCacheMap.put(thumbnailCache.getThumbnailType(), thumbnailCache);
  }

  public Integer getCloudStatus() {
    return cloudStatus;
  }

  public void setCloudStatus(Integer cloudStatus) {
    this.cloudStatus = cloudStatus;
  }

  public void setCloudExecuteStatus(Boolean result) {
    if (result) {
      this.cloudStatus = 1;
    } else {
      this.cloudStatus = 0;
    }
  }

  public CacheFileData getCacheFileData() {
    return cacheFileData;
  }

  public void setCacheFileData(CacheFileData cacheFileData) {
    this.cacheFileData = cacheFileData;
  }

  public CallbackData getCallback() {
    return callback;
  }

  public void setCallback(CallbackData callback) {
    this.callback = callback;
  }

}



