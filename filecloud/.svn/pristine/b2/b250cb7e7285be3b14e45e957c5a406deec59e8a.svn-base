package com.newroad.fileext.service.picture;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.fileext.data.model.ThumbnailCache;
import com.newroad.fileext.data.model.ThumbnailType;

public class ThumbnailCacheExecutor implements Callable<ThumbnailCache> {
  private static Logger logger = LoggerFactory.getLogger(ThumbnailCacheExecutor.class);

  private String cacheFilePath;

  private ThumbnailType thumbnailType;

  public ThumbnailCacheExecutor(String cacheFilePath, ThumbnailType thumbnailType) {
    this.cacheFilePath = cacheFilePath;
    this.thumbnailType = thumbnailType;
  }

  @Override
  public ThumbnailCache call() {
    return createThumbnailCache();
  }

  private ThumbnailCache createThumbnailCache() {
    ThumbnailCache thumbnailCache = null;
    String targetFilePath = ThumbnailDesigner.makeThumbnail(cacheFilePath, false, thumbnailType.getWidth(), thumbnailType.getHeight());
    if (targetFilePath != null) {
      thumbnailCache = new ThumbnailCache(thumbnailType, targetFilePath);
    } else {
      logger.error("Fail to make Thumbnail according to the original file path " + cacheFilePath);
    }
    return thumbnailCache;
  }

}
