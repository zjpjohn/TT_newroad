package com.newroad.fileext.service.api;

import net.sf.json.JSONObject;

import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.CommonFileData;
import com.newroad.fileext.data.model.ThumbnailCache;
import com.newroad.fileext.data.model.ThumbnailType;

public interface FileCacheTaskQueueIf {
  
  public CommonFileData cacheFileResource(JSONObject sessionUser, CommonFileData commonFileData);
  
  public CloudFileData cacheCloudThumbnail(JSONObject sessionUser, CloudFileData fileData, ThumbnailType thumbnailType);
  
  public ThumbnailCache cacheLocalThumbnail(String cacheFilePath, ThumbnailType thumbnailType, boolean isSync);

}
