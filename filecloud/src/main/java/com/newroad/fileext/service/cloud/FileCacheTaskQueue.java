package com.newroad.fileext.service.cloud;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newroad.fileext.dao.CloudFileExecutionCode;
import com.newroad.fileext.dao.cos.COSExecutionTask;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.CommonFileData;
import com.newroad.fileext.data.model.FileMimeType;
import com.newroad.fileext.data.model.ThumbnailCache;
import com.newroad.fileext.data.model.ThumbnailType;
import com.newroad.fileext.data.model.ThumbnailType.ThumbnailDict;
import com.newroad.fileext.service.api.FileCacheTaskQueueIf;
import com.newroad.fileext.service.api.FileStoreServiceIf;
import com.newroad.fileext.service.picture.ThumbnailCacheExecutor;
import com.newroad.fileext.utilities.FileResourceException;

public class FileCacheTaskQueue implements FileCacheTaskQueueIf {

  private static Logger logger = LoggerFactory.getLogger(FileCacheTaskQueue.class);

  private FileStoreServiceIf fileStoreService;

  private CloudManageService cloudManageService;

  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  public CommonFileData cacheFileResource(JSONObject sessionUser, CommonFileData fileResource) {
    if (fileResource == null) {
      logger.error("Couldn't find the correct resource which fileResource is null!");
      throw new FileResourceException("Couldn't find the correct resource which fileResource is null!");
    }

    String fileId = fileResource.getFileId();
    CloudFileData fileData = fileResource.getCloudFileData();
    if (fileData == null) {
      logger.error("Couldn't find the correct cloud file which fileData is null!");
      throw new FileResourceException("Couldn't find the correct cloud file which fileData is null!");
    }

    String keyID = fileData.getKey();
    String fileName = fileData.getFileName();
    if (fileName == null) {
      fileName = fileResource.getFileName();
      fileData.setFileName(fileName);
    }
    Long fileSize = fileData.getCloudSize();
    String contentType = fileData.getContentType() == null ? FileMimeType.getContentTypeByName(fileName) : fileData.getContentType();
    String cacheFilePath = fileData.getCacheFileData().getFileCachePath();
    if (cacheFilePath == null) {
      fileData = refreshCloudFileData(sessionUser, keyID, fileName, fileSize, contentType);
      fileResource.setCloudFileData(fileData);
      fileStoreService.updateFileResource(fileId, fileResource);
    } else {
      File cacheFile = new File(cacheFilePath);
      if (!cacheFile.exists() || cacheFile.length() == 0) {
        fileData = refreshCloudFileData(sessionUser, keyID, fileName, fileSize, contentType);
        fileResource.setCloudFileData(fileData);
        fileStoreService.updateFileResource(fileId, fileResource);
      }
    }
    logger.info("Get File cache info (clientResourceID:" + fileId + ") in the path " + cacheFilePath + " and the size is "
        + fileSize);
    return fileResource;
  }

  public CloudFileData cacheCloudThumbnail(JSONObject sessionUser, CloudFileData fileData, ThumbnailType thumbnailType) {
    String token = getAuthToken(sessionUser);
    fileData.setToken(token);
    String resourceKeyID = fileData.getKey();
    String cacheFilePath = fileData.getCacheFileData().getFileCachePath();

    if (cacheFilePath == null) {
      cacheFilePath = fileStoreService.createCacheFilePath(resourceKeyID);
      fileData.getCacheFileData().setFileCachePath(cacheFilePath);
    }
    // Create new cache thumbnail path for refresh.
    String thumbnailFilePath = ThumbnailCache.getThumbnailCachePath(cacheFilePath, thumbnailType);
    ThumbnailCache currentThumbnailCache=new ThumbnailCache(thumbnailType, thumbnailFilePath);
    fileData.setCurrentThumbnailCache(new ThumbnailCache(thumbnailType, thumbnailFilePath));

    File thumbnailFile = new File(thumbnailFilePath);
    if (!thumbnailFile.exists() || thumbnailFile.length() == 0) {
      fileData = (CloudFileData) executeCallableCOSTask(CloudFileExecutionCode.GET_OBJECT_THUMBNAIL, token, fileData);
      if (fileData.getCloudStatus() != 1) {
        logger.error("Couldn't find the correct resource file which keyID is " + resourceKeyID + "!");
        throw new FileResourceException("Couldn't find the correct resource file which keyID is " + resourceKeyID + "!");
      }
      fileData.setCurrentThumbnailCache(null);
    }
    fileData.setThumbnailCacheMap(currentThumbnailCache);
    return fileData;
  }

  public ThumbnailCache cacheLocalThumbnail(String cacheFilePath, ThumbnailType thumbnailType, boolean isSync) {
    ThumbnailCache thumbnailCache = null;
    if (cacheFilePath == null) {
      logger.error("Fail to get file cache path!");
      return thumbnailCache;
    } else {
      File cacheFile = new File(cacheFilePath);
      if (!cacheFile.exists() || cacheFile.length() == 0) {
        logger.error("Fail to get file cache path " + cacheFilePath + "!");
        return thumbnailCache;
      }
    }
    if (thumbnailType == null) {
      thumbnailType = ThumbnailType.getThumbnailDictType(ThumbnailDict.WEB_DEFAULT_TYPE);
    }
    String thumbnailFilePath = ThumbnailCache.getThumbnailCachePath(cacheFilePath, thumbnailType);
    File thumbnailFile = new File(thumbnailFilePath);
    try {
      if (!thumbnailFile.exists() || thumbnailFile.length() == 0) {
        ThumbnailCacheExecutor thumbnailExecutor = new ThumbnailCacheExecutor(cacheFilePath, thumbnailType);
        Future<ThumbnailCache> future = threadPoolTaskExecutor.submit(thumbnailExecutor);
        if (isSync) {
          thumbnailCache = future.get();
        }
      } else {
        thumbnailCache = new ThumbnailCache(thumbnailType, thumbnailFilePath);
      }
    } catch (InterruptedException e) {
      logger.error("cacheThumbnail InterruptedException:", e);
    } catch (ExecutionException e) {
      logger.error("cacheThumbnail ExecutionException:", e);
    }
    return thumbnailCache;
  }

  private CloudFileData refreshCloudFileData(JSONObject sessionUser, String resourceKeyID, String fileName, Long fileSize,
      String contentType) {
    String token = getAuthToken(sessionUser);
    CloudFileData fileData = new CloudFileData();
    fileData.setToken(token);
    fileData.setKey(resourceKeyID);
    // fileData.setLink(resourceLink);
    fileData.setFileName(fileName);
    if (fileSize != null) {
      fileData.getCacheFileData().setOffset(0L);
      fileData.setCloudSize(fileSize);
    }

    // Create new cache file path for refresh.
    String fileCachePath = fileStoreService.createCacheFilePath(resourceKeyID);
    fileData.getCacheFileData().setFileCachePath(fileCachePath);
    fileData = (CloudFileData) executeCallableCOSTask(CloudFileExecutionCode.GET_OBJECT, token, fileData);
    if (fileData.getCloudStatus() != 1) {
      logger.error("Couldn't find the correct resource file which keyID is " + resourceKeyID + "!");
      throw new FileResourceException("Couldn't find the correct resource file which keyID is " + resourceKeyID + "!");
    }
    return fileData;
  }


  private String getAuthToken(JSONObject sessionUser) {
    String token = null;
    JSONObject tokenInfo = cloudManageService.getCloudToken(sessionUser);
    // String token = cloudManageService.getConnectorByUser(SystemProperties.testUser,
    // SystemProperties.testPassword);
    if (!tokenInfo.isNullObject()) {
      return tokenInfo.getString("token");
    }
    return token;
  }



  private Object executeCallableCOSTask(CloudFileExecutionCode signal, String authToken, CloudFileData... fileData) {
    try {
      COSExecutionTask task = new COSExecutionTask(signal, authToken, fileData);
      Future<Object> future = threadPoolTaskExecutor.submit(task);
      if (future != null) {
        return future.get();
      }
      return null;
    } catch (InterruptedException e) {
      logger.error("executeCallableCOSTask InterruptedException!", e);
      throw new FileResourceException("Couldn't find the correct file which keyID is " + fileData[0].getKey() + " from Cloud!");
    } catch (ExecutionException e) {
      logger.error("executeCallableCOSTask ExecutionException!", e);
      throw new FileResourceException("Couldn't find the correct file which keyID is " + fileData[0].getKey() + " from Cloud!");
    }
  }

  public void setFileStoreService(FileStoreServiceIf fileStoreService) {
    this.fileStoreService = fileStoreService;
  }

  public void setCloudManageService(CloudManageService cloudManageService) {
    this.cloudManageService = cloudManageService;
  }

  public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
    this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    this.threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
  }

}
