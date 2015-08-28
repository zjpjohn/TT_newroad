package com.newroad.fileext.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import net.sf.json.JSONArray;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newroad.fileext.dao.CloudFileExecutionCode;
import com.newroad.fileext.dao.FileResourceSQLDao;
import com.newroad.fileext.dao.qiniu.QiNiuExecutionTask;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.CommonFileData;
import com.newroad.fileext.data.model.FileMimeType;
import com.newroad.fileext.filter.TokenAuthFilter;
import com.newroad.fileext.service.api.FileCacheTaskQueueIf;
import com.newroad.fileext.service.api.FileResourceServiceIf;
import com.newroad.fileext.service.api.FileStoreServiceIf;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.fileext.utilities.FileDataUtils;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.fileext.utilities.JSONConvertor;
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
    Long userId = TokenAuthFilter.getCurrentUser();

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
  public ServiceResult<List<String>> uploadCloudFiles(Long userId, List<CloudFileData> fileDataList) throws FileResourceException {
    ServiceResult<List<String>> sr = new ServiceResult<List<String>>();

    List<CloudFileData> cloudFileList = fileDataList;
    int fileCount = cloudFileList.size();
    List<String> localFileList = new ArrayList<String>(fileCount);
    if (fileCount > 0) {
      for (int i = 0; i < fileCount; i++) {
        CloudFileData fileData = (CloudFileData) cloudFileList.get(i);
        fileData.setBucketName(SystemProperties.bucketName);
        // if (SystemProperties.cloudCallbackStatus) {
        // fileData.setCallback(assembleCallbackData(fileData.getFileName(), callbackToken));
        // }
        CommonFileData localFile = CommonFileData.create(userId, fileData);

        // Save File in Cache
        // CacheFileData cacheFileData = fileData.getCacheFileData();
        // InputStream input = new ByteArrayInputStream(fileData.getFileByte());
        // String filecache = fileStoreService.saveFileOnLocal(input, keyId);
        // if (filecache != null) {
        // cacheFileData.setFileCachePath(filecache);
        // if (SystemProperties.cloudCallbackStatus) {
        // // cloudCallback status is true so that cloud status will be updated after callback is
        // // called.
        // fileData.setCloudStatus(0);
        // } else {
        // fileData.setCloudStatus(1);
        // }
        // fileData.setCloudSize(cacheFileData.getCacheSize());
        // // Insert file info into Cache ant the status is 0
        // fileStoreService.saveFileResource(null, localFile);
        // String fileId = localFile.getFileId();
        // fileStoreService.setFileCache(fileId, fileData);
        // logger.info("Set file cache which the fileId:" + fileId + " before uploading file.");
        // }


        fileData =(CloudFileData) executeCallbackCloudTask(CloudFileExecutionCode.PUT_OBJECT, fileData);
        localFile.setCloudFileData(fileData);
        fileStoreService.saveFileResource(null, localFile);
        localFile.getCloudFileData().setFileByte(null);
        localFileList.add(localFile.getCloudFileData().getLink());
      }
    }
    sr.setBusinessResult(localFileList);
    return sr;
  }


  @Override
  public ServiceResult<String> saveFileMetaData(Long userId, CloudFileData cloudFileData) throws FileResourceException {
    ServiceResult<String> sr = new ServiceResult<String>();

    Map<String, Object> map = new HashMap<String, Object>();
    if (cloudFileData != null) {
      String fileName=cloudFileData.getKey();
      if(fileName!=null){
        FileMimeType.getContentTypeByName(fileName);
      }
      cloudFileData.setCloudStatus(1);
      CommonFileData commonFileData=CommonFileData.create(userId, cloudFileData);

      fileStoreService.saveFileResource(null, commonFileData);
      map.put("fileId", commonFileData.getFileId());
    }
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    sr.setBusinessResult(jsonResult);
    return sr;
  }

  @SuppressWarnings("unused")
  private void executeCloudTask(CloudFileExecutionCode code, CloudFileData fileData) {
    QiNiuExecutionTask task = new QiNiuExecutionTask(code, fileData);
    FutureTask<Object> futureTask = new FutureTask<Object>(task);
    threadPoolTaskExecutor.execute(futureTask);
  }

  private Object executeCallbackCloudTask(CloudFileExecutionCode code, CloudFileData fileData) {
    try {
      QiNiuExecutionTask task = new QiNiuExecutionTask(code, fileData);
      Future<Object> future = threadPoolTaskExecutor.submit(task);
      if (future != null) {
        return future.get();
      }
      return null;
    } catch (InterruptedException e) {
      logger.error("executeCallableCOSTask InterruptedException!", e);
      throw new FileResourceException("Couldn't find the correct file " + fileData.getKey() + " from Cloud!");
    } catch (ExecutionException e) {
      logger.error("executeCallableCOSTask ExecutionException!", e);
      throw new FileResourceException("Couldn't find the correct file " + fileData.getKey() + " from Cloud!");
    }
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
