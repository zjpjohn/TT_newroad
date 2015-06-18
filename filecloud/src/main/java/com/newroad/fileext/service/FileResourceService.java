package com.newroad.fileext.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newroad.fileext.dao.CloudFileExecutionCode;
import com.newroad.fileext.dao.FileResourceSQLDao;
import com.newroad.fileext.dao.qiniu.QiNiuExecutionTask;
import com.newroad.fileext.data.model.CacheFileData;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.CommonFileData;
import com.newroad.fileext.filter.TokenAuthFilter;
import com.newroad.fileext.service.api.FileCacheTaskQueueIf;
import com.newroad.fileext.service.api.FileResourceServiceIf;
import com.newroad.fileext.service.api.FileStoreServiceIf;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.fileext.utilities.FileDataUtils;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.fileext.utilities.SystemProperties;
import com.newroad.util.apiresult.ServiceResult;


public class FileResourceService implements FileResourceServiceIf {

  private static Logger logger = LoggerFactory.getLogger(FileResourceService.class);

  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  private FileCacheTaskQueueIf cacheTaskQueue;

  private FileStoreServiceIf fileStoreService;

  private FileResourceSQLDao fileSQLDao;

  @Override
  public ServiceResult<String> uploadFile(CloudFileData fileData) throws FileResourceException {
    ServiceResult<String> sr = new ServiceResult<String>();
    JSONObject sessionUser = TokenAuthFilter.getCurrent();
    String userId = (String) sessionUser.get("userID");
    
    JSONArray array = null;
    List<String> fileLinks = new ArrayList<String>();
    CommonFileData commonfileData = CommonFileData.create(userId, fileData);
    commonfileData.setFileType(1);
    InputStream inputStream = new ByteArrayInputStream(fileData.getCacheFileData().getFileByte());
    String localFileSuffix = FileDataUtils.getCurrentDateInfo();
    if (processResourceData(fileData, inputStream, localFileSuffix)) {
      fileSQLDao.insert("insertFileData", fileData);
      fileData.getCacheFileData().setFileByte(null);
      fileLinks.add(fileData.getLink());
    }
    array = JSONArray.fromObject(fileLinks);
    sr.setBusinessResult(array.toString());
    return sr;
  }

  @Override
  public ServiceResult<List<CommonFileData>> uploadCloudFiles(List<CloudFileData> fileDataList) throws FileResourceException {
    ServiceResult<List<CommonFileData>> sr = new ServiceResult<List<CommonFileData>>();
    JSONObject sessionUser = TokenAuthFilter.getCurrent();
    String userID = (String) sessionUser.get("userID");

    List<CloudFileData> cloudFileList = fileDataList;
    int fileCount = cloudFileList.size();
    List<CommonFileData> localFileList = new ArrayList<CommonFileData>(fileCount);
    if (fileCount > 0) {
      for (int i = 0; i < fileCount; i++) {
        CloudFileData fileData = (CloudFileData) cloudFileList.get(i);
        fileData.setBucketName(SystemProperties.bucketName);
        // if (SystemProperties.cloudCallbackStatus) {
        // fileData.setCallback(assembleCallbackData(fileData.getFileName(), callbackToken));
        // }
        CacheFileData cacheFileData=fileData.getCacheFileData();
        CommonFileData localFile = CommonFileData.create(userID,fileData);
        String keyId = localFile.getHash();
        // Save File in Cache
        InputStream input = new ByteArrayInputStream(cacheFileData.getFileByte());
        String filecache = fileStoreService.saveFileOnLocal(input, keyId);
        if (filecache != null) {
          cacheFileData.setFileCachePath(filecache);
          if (SystemProperties.cloudCallbackStatus) {
            // cloudCallback status is true so that cloud status will be updated after callback is
            // called.
            fileData.setCloudStatus(0);
          } else {
            fileData.setCloudStatus(1);
          }
          fileData.setCloudSize(cacheFileData.getCacheSize());
          // Insert file info into Cache ant the status is 0
          fileStoreService.saveFileResource(null, localFile);
          String fileId = localFile.getFileId();
          fileStoreService.setFileCache(fileId, localFile.getCloudFileData());
          logger.info("Set file cache which the fileId:" + fileId + " before uploading file.");
        }
        executeCloudTask(CloudFileExecutionCode.PUT_OBJECT, fileData);
        cacheFileData.setFileByte(null);
        localFileList.add(localFile);
      }
    }
    sr.setBusinessResult(localFileList);
    return sr;
  }


  private void executeCloudTask(CloudFileExecutionCode code, CloudFileData... fileData) {
    QiNiuExecutionTask task = new QiNiuExecutionTask(code, fileData);
    FutureTask<Object> futureTask = new FutureTask<Object>(task);
    threadPoolTaskExecutor.execute(futureTask);
  }

  public boolean processResourceData(CloudFileData fileResource, InputStream input, String destFilePathSuffix) {
    boolean result = false;
    String fileLink = null;
    String localDestFolderPathPrefix = FileDataConstant.ZY_FILE_STORE_HEADER + destFilePathSuffix;
    String linkUrlPathPrefix = FileDataConstant.LINK_HEADER + destFilePathSuffix;
    File destFolder = new File(localDestFolderPathPrefix);
    if (!destFolder.exists())
      destFolder.mkdir();

    String fileName = fileResource.getFileName();
    String hashcode = Integer.valueOf(fileResource.hashCode()).toString();
    String hashCodeName = hashcode + "_" + fileName;
    String destFilePath = localDestFolderPathPrefix + File.separator + hashCodeName;
    logger.debug("Download file path:" + destFilePath);
    File file = new File(destFilePath);
    try {
      FileUtils.copyInputStreamToFile(input, file);
      if (file != null) {
        fileLink = linkUrlPathPrefix + "/" + hashCodeName;
        logger.info("FileLink:" + fileLink + ",localFilePath:" + destFilePath);
        fileResource.setFileName(hashCodeName);
        fileResource.setLink(fileLink);
        fileResource.getCacheFileData().setFileCachePath(destFilePath);
        result = true;
      }
    } catch (IOException e) {
      logger.error("Fail to copy inputStream into local file!", e);
    }
    return result;
  }

  public void setFileSQLDao(FileResourceSQLDao fileSQLDao) {
    this.fileSQLDao = fileSQLDao;
  }

  public FileCacheTaskQueueIf getCacheTaskQueue() {
    return cacheTaskQueue;
  }

  public void setCacheTaskQueue(FileCacheTaskQueueIf cacheTaskQueue) {
    this.cacheTaskQueue = cacheTaskQueue;
  }

  public FileStoreServiceIf getFileStoreService() {
    return fileStoreService;
  }

  public void setFileStoreService(FileStoreServiceIf fileStoreService) {
    this.fileStoreService = fileStoreService;
  }

  public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
    this.threadPoolTaskExecutor = threadPoolTaskExecutor;
  }



}
