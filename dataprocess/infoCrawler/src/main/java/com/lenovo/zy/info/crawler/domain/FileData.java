package com.lenovo.zy.info.crawler.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


public class FileData implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = -8758508746358605847L;

  private Long fileId;
  private String fileName;
  private Integer fileType;
  private String contentType;
  private String link;
  private String publicLink;
  private String fileCachePath;
  private Long size;
  private Integer width;
  private Integer height;

  private Long cacheSize;
  private Long offset;
  private byte[] fileByte;

  private Integer status;
  private Date createTime;
  private Date lastUpdateTime;



  public FileData(String fileName, Integer fileType, String contentType, String link, String path, Long size) {
    super();
    this.fileName = fileName;
    this.fileType = fileType;
    this.contentType = contentType;
    this.link = link;
    this.fileCachePath = path;
    this.size = size;
    this.status = 1;
    this.createTime = new Date();
    this.lastUpdateTime = new Date();
  }

  public static String generateKeyId() {
    UUID randomUUID = UUID.randomUUID();
    String key = randomUUID.toString().replaceAll("-", "");
    return key;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
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

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
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

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
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

  public String getFileCachePath() {
    return fileCachePath;
  }

  public void setFileCachePath(String fileCachePath) {
    this.fileCachePath = fileCachePath;
  }

  public Long getCacheSize() {
    return cacheSize;
  }

  public void setCacheSize(Long cacheSize) {
    this.cacheSize = cacheSize;
  }

  public Long getOffset() {
    return offset;
  }

  public void setOffset(Long offset) {
    this.offset = offset;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public byte[] getFileByte() {
    return fileByte;
  }

  public void setFileByte(byte[] fileByte) {
    this.fileByte = fileByte;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

}
