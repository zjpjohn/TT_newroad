package com.newroad.fileext.data.model;

public class CacheFileData extends CloudFileData{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 5595935425964306091L;
  // cache file info
  private byte[] fileByte;
  private String fileCachePath;
  private Long offset;
  private Long cacheSize;
  
  public CacheFileData(byte[] fileByte, Long cacheSize) {
    super();
    this.fileByte = fileByte;
    this.cacheSize = cacheSize;
  }

  public byte[] getFileByte() {
    return fileByte;
  }

  public void setFileByte(byte[] fileByte) {
    this.fileByte = fileByte;
  }

  public String getFileCachePath() {
    return fileCachePath;
  }

  public void setFileCachePath(String fileCachePath) {
    this.fileCachePath = fileCachePath;
  }

  public Long getOffset() {
    return offset;
  }

  public void setOffset(Long offset) {
    this.offset = offset;
  }

  public Long getCacheSize() {
    return cacheSize;
  }

  public void setCacheSize(Long cacheSize) {
    this.cacheSize = cacheSize;
  }

}