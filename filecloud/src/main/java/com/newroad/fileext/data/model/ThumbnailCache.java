package com.newroad.fileext.data.model;

import java.io.Serializable;

/**
 * @author tangzj1
 * @version 2.0
 * @since May 15, 2014
 */
public class ThumbnailCache implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -2845924319986391409L;

  private ThumbnailType thumbnailType;
  private String thumbnailCachePath;
  
  public enum ThumbnailGenerateMode {
    LOCAL,
    CLOUD;
  }

  public ThumbnailCache() {}

  /**
   * @param thumbnailType
   * @param thumbnailCachePath
   */
  public ThumbnailCache(ThumbnailType thumbnailType, String thumbnailCachePath) {
    super();
    this.thumbnailType = thumbnailType;
    this.thumbnailCachePath = thumbnailCachePath;
  }


  public ThumbnailType getThumbnailType() {
    return thumbnailType;
  }


  public void setThumbnailType(ThumbnailType thumbnailType) {
    this.thumbnailType = thumbnailType;
  }

  public String getThumbnailCachePath() {
    return thumbnailCachePath;
  }


  public void setThumbnailCachePath(String thumbnailCachePath) {
    this.thumbnailCachePath = thumbnailCachePath;
  }

  public static String getThumbnailCachePath(String fileCachePath, ThumbnailType thumbnailType) {
    return fileCachePath + "_" + thumbnailType.getWidth() + "_" + thumbnailType.getHeight();
  }
}
