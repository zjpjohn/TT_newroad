package com.newroad.fileext.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.newroad.fileext.dao.CloudFileExecutionCode;
import com.newroad.fileext.dao.cos.COSExecutionTask;
import com.newroad.fileext.data.model.CacheFileData;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.FileMimeType;
import com.newroad.fileext.data.model.NoteFileData;
import com.newroad.fileext.data.model.ThumbnailCache;
import com.newroad.fileext.data.model.ThumbnailType;
import com.newroad.fileext.service.api.FileCacheTaskQueueIf;
import com.newroad.fileext.service.api.FileExtendServiceIf;
import com.newroad.fileext.service.api.FileStoreServiceIf;
import com.newroad.fileext.service.cloud.CloudManageService;
import com.newroad.fileext.service.cloud.FileCacheTaskQueue;
import com.newroad.fileext.utilities.AudioConvertor;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.fileext.utilities.SystemProperties;
import com.newroad.fileext.utilities.TransferObjectAssembler;
import com.newroad.mongodb.orm.db.MongoDaoIf;
import com.newroad.pilot.util.CallbackData;
import com.newroad.pilot.util.PilotOssConstants;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.apiresult.ServiceResult;

/**
 * @Info File Extend service implementation api
 * @author tangzj1
 * 
 */
@Service
public class FileExtendService implements FileExtendServiceIf {

  private static Logger logger = LoggerFactory.getLogger(FileExtendService.class);

  private FileStoreServiceIf fileStoreService;

  private MongoDaoIf mongoDao;

  private CloudManageService cloudManageService;

  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  private FileCacheTaskQueueIf cacheTaskQueue;


  @SuppressWarnings("rawtypes")
  @Override
  public ServiceResult<JSONObject> getObjectInfo(JSONObject sessionUser, Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    JSONObject tokenInfo = cloudManageService.getCloudToken(sessionUser);
    String token = tokenInfo.getString("token");
    CloudFileData cdata = new CloudFileData();
    cdata.setKey((String) resourceInfo.get(FileDataConstant.RESOURCE_KEY_ID));
    // cdata.setLink((String)
    // resourceInfo.get(FileDataConstant.RESOURCE_LINK));

    CloudFileData returnData = (CloudFileData) executeCallableCOSTask(CloudFileExecutionCode.GET_OBJECT_INFO, token, cdata);

    JSONObject jsObjectInfo = new JSONObject();
    jsObjectInfo.put(FileDataConstant.FILE_NAME, returnData.getFileName());
    jsObjectInfo.put(FileDataConstant.FILE_SIZE, returnData.getCloudSize());
    jsObjectInfo.put(FileDataConstant.CONTENT_TYPE_JSON, returnData.getContentType());
    sr.setBusinessResult(jsObjectInfo);
    return sr;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public ServiceResult<JSONObject> uploadData(JSONObject sessionUser, Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    if (sessionUser == null) {
      sr.setReturnCode(ReturnCode.UNAUTHORIZED);
      logger.error("Authentication bad because sessionUser is null!");
      return sr;
    }
    ServiceResult<List<NoteFileData>> result = uploadFiles(sessionUser, resourceInfo);
    JSONObject jsAttachs = new JSONObject();
    if (!result.checkOK()) {
      sr.setReturnCode(result.gainReturnCode());
      sr.setReturnMessage(result.gainReturnMessage());
      jsAttachs.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_FAILURE);
      sr.setBusinessResult(jsAttachs);
      return sr;
    }

    List<NoteFileData> list = result.getBusinessResult();
    jsAttachs.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_SUCCESS);
    jsAttachs.put(FileDataConstant.EXECUTION_RESULT, TransferObjectAssembler.transformFileResourceData(list));
    sr.setBusinessResult(jsAttachs);
    return sr;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public ServiceResult<JSONObject> downloadData(JSONObject sessionUser, Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    if (sessionUser == null) {
      sr.setReturnCode(ReturnCode.UNAUTHORIZED);
      logger.error("Authentication bad because sessionUser is null!");
      return sr;
    }
    String clientResourceID = (String) resourceInfo.get(FileDataConstant.CLIENT_RESOURCE_ID);
    String fileName = (String) resourceInfo.get(FileDataConstant.FILE_NAME);
    logger.info("File download clientResourceID=" + clientResourceID + " & fileName=" + fileName);

    CloudFileData fileCache = getCloudFileData(sessionUser, clientResourceID, fileName);
    JSONObject returnFile = new JSONObject();
    if (fileCache == null) {
      sr.setReturnCode(ReturnCode.NO_DATA_RETURN);
      sr.setReturnMessage("Fail to find this file resource which clientResourceID=" + clientResourceID);
      returnFile.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_FAILURE);
      sr.setBusinessResult(returnFile);
      return sr;
    }

    String contentType = fileCache.getContentType();
    CacheFileData cacheFileData=fileCache.getCacheFileData();
    String tempFilePath = cacheFileData.getFileCachePath();
    if (contentType.equals(FileMimeType.kk.getContentType())) {
      JSONObject convertFile = AudioConvertor.audioHandler(fileName, contentType, tempFilePath);
      if (convertFile == null) {
        logger.error("Fail to play this audio file " + fileName);
        throw new FileResourceException("Fail to play this audio file " + fileName);
      }
      fileName = convertFile.getString(FileDataConstant.FILE_NAME);
      contentType = convertFile.getString(FileDataConstant.FILE_CONTENT_TYPE);
      tempFilePath = convertFile.getString(FileDataConstant.TEMP_FILE_PATH);
      logger.info("Download KK File Name:" + fileName + "; ContentType:" + contentType + "; OriginalFilePath:" + tempFilePath);
    }

    returnFile.put(FileDataConstant.FILE_NAME, fileName);
    returnFile.put(FileDataConstant.FILE_CONTENT_TYPE, contentType);
    returnFile.put(FileDataConstant.TEMP_FILE_PATH, tempFilePath);
    returnFile.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_SUCCESS);
    logger.info("File download fileName=" + fileName + " & contentType=" + contentType);

    sr.setBusinessResult(returnFile);
    return sr;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public ServiceResult<JSONObject> getThumbnail(JSONObject sessionUser, Map resourceInfo, ThumbnailType thumbnailType)
      throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    if (sessionUser == null) {
      sr.setReturnCode(ReturnCode.UNAUTHORIZED);
      logger.error("Authentication bad because sessionUser is null!");
      return sr;
    }
    String clientResourceID = (String) resourceInfo.get(FileDataConstant.CLIENT_RESOURCE_ID);
    String fileName = (String) resourceInfo.get(FileDataConstant.FILE_NAME);
    logger.info("File thumbnail clientResourceID=" + clientResourceID + " & fileName=" + fileName);

    String tempFilePath = null;
    CloudFileData fileCache = getCloudFileData(sessionUser, clientResourceID, fileName);
    if (fileCache != null) {
      ThumbnailCache thumbnailCache = cacheTaskQueue.cacheLocalThumbnail(fileCache.getCacheFileData().getFileCachePath(), thumbnailType, true);
      if (thumbnailCache == null) {
        logger.error("Fail to create thumbnail of file " + fileName + "!");
        throw new FileResourceException("Fail to create thumbnail of file " + fileName + "!");
      }
      tempFilePath = thumbnailCache.getThumbnailCachePath();
    }

    JSONObject returnFile = new JSONObject();
    if (fileCache == null) {
      sr.setReturnCode(ReturnCode.NO_DATA_RETURN);
      sr.setReturnMessage("Fail to find this file resource which clientResourceID=" + clientResourceID);
      returnFile.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_FAILURE);
      sr.setBusinessResult(returnFile);
      return sr;
    }

    String contentType = fileCache.getContentType();
    returnFile.put(FileDataConstant.FILE_NAME, fileName);
    returnFile.put(FileDataConstant.FILE_CONTENT_TYPE, contentType);
    returnFile.put(FileDataConstant.TEMP_FILE_PATH, tempFilePath);
    returnFile.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_SUCCESS);
    logger.info("File thumbnail fileName=" + fileName + " & contentType=" + contentType);
    sr.setBusinessResult(returnFile);
    return sr;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public ServiceResult<JSONObject> getCloudThumbnail(JSONObject sessionUser, Map resourceInfo, ThumbnailType thumbnailType)
      throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    if (sessionUser == null) {
      sr.setReturnCode(ReturnCode.UNAUTHORIZED);
      logger.error("Authentication bad because sessionUser is null!");
      return sr;
    }
    String clientResourceID = (String) resourceInfo.get(FileDataConstant.CLIENT_RESOURCE_ID);
    String fileName = (String) resourceInfo.get(FileDataConstant.FILE_NAME);
    logger.info("File thumbnail clientResourceID=" + clientResourceID + " & fileName=" + fileName);

    CloudFileData fileCache = getCloudThumbnail(sessionUser, clientResourceID, fileName, thumbnailType);
    JSONObject returnFile = new JSONObject();
    if (fileCache == null) {
      sr.setReturnCode(ReturnCode.NO_DATA_RETURN);
      sr.setReturnMessage("Fail to find this file thumbnail which clientResourceID=" + clientResourceID);
      returnFile.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_FAILURE);
      sr.setBusinessResult(returnFile);
      return sr;
    }
    ThumbnailCache thumbnailCache = fileCache.getThumbnailCacheMap().get(thumbnailType);
    String tempFilePath = thumbnailCache.getThumbnailCachePath();
    String contentType = fileCache.getContentType();
    returnFile.put(FileDataConstant.FILE_NAME, fileName);
    returnFile.put(FileDataConstant.FILE_CONTENT_TYPE, contentType);
    returnFile.put(FileDataConstant.TEMP_FILE_PATH, tempFilePath);
    returnFile.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_SUCCESS);
    logger.info("File thumbnail fileName=" + fileName + " & contentType=" + contentType);
    sr.setBusinessResult(returnFile);
    return sr;
  }

  /**
   * 
   * @info get resource display icon contains name,size and time
   **/
  @SuppressWarnings("rawtypes")
  @Override
  public ServiceResult<JSONObject> getFileIcon(JSONObject sessionUser, Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    if (sessionUser == null) {
      sr.setReturnCode(ReturnCode.UNAUTHORIZED);
      logger.error("Authentication bad because sessionUser is null!");
      return sr;
    }
    String clientResourceID = (String) resourceInfo.get(FileDataConstant.CLIENT_RESOURCE_ID);
    String fileName = (String) resourceInfo.get(FileDataConstant.FILE_NAME);
    logger.info("File get icon clientResourceID=" + clientResourceID + " & fileName=" + fileName);

    CloudFileData fileCache = getCloudFileData(sessionUser, clientResourceID, fileName);

    JSONObject returnFile = new JSONObject();
    if (fileCache == null) {
      sr.setReturnCode(ReturnCode.NO_DATA_RETURN);
      sr.setReturnMessage("Fail to find this file resource which clientResourceID=" + clientResourceID);
      returnFile.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_FAILURE);
      sr.setBusinessResult(returnFile);
      return sr;
    }

    String contentType = fileCache.getContentType();
    String tempFilePath = fileCache.getCacheFileData().getFileCachePath();
    returnFile.put(FileDataConstant.FILE_NAME, fileName);
    returnFile.put(FileDataConstant.FILE_CONTENT_TYPE, contentType);
    returnFile.put(FileDataConstant.TEMP_FILE_PATH, tempFilePath);
    returnFile.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_SUCCESS);
    sr.setBusinessResult(returnFile);
    return sr;
  }

  private CloudFileData getCloudFileData(JSONObject sessionUser, String clientResourceID, String fileName) {
    CloudFileData fileCache = null;
    try {
      fileCache = fileStoreService.getFileCache(clientResourceID);
      if (fileCache == null || fileCache.getCacheFileData().getFileCachePath() == null) {
        NoteFileData fileResource = checkFileResourceExist(clientResourceID);
        fileResource = (NoteFileData) cacheTaskQueue.cacheFileResource(sessionUser, fileResource);
        fileCache = fileResource.getFileData();
        fileStoreService.setFileCache(clientResourceID, fileCache);
      } else {
        String fileCachePath = fileCache.getCacheFileData().getFileCachePath();
        File cacheFile = new File(fileCachePath);
        if (!cacheFile.exists() || cacheFile.length() == 0) {
          NoteFileData fileResource = checkFileResourceExist(clientResourceID);
          fileResource = (NoteFileData) cacheTaskQueue.cacheFileResource(sessionUser, fileResource);
          fileCache = fileResource.getFileData();
          fileStoreService.setFileCache(clientResourceID, fileCache);
        }
      }
      String contentType = null;
      String cacheContentType = fileCache.getContentType();
      if (cacheContentType == null || "null".equals(cacheContentType)) {
        String name = fileCache.getFileName() == null ? fileName : fileCache.getFileName();
        contentType = FileMimeType.getContentTypeByName(name);
        fileCache.setFileName(name);
        fileCache.setContentType(contentType);
        fileStoreService.setFileCache(clientResourceID, fileCache);
      }
    } catch (FileResourceException frx) {
      fileCache = null;
      logger.error("Couldn't find the file resource when getting cloud file data!", frx);
    }
    return fileCache;
  }

  private CloudFileData getCloudThumbnail(JSONObject sessionUser, String clientResourceID, String fileName, ThumbnailType thumbnailType) {
    boolean isCacheUpdate = false;
    CloudFileData fileCache = null;
    try {
      fileCache = fileStoreService.getFileCache(clientResourceID);
      String fileCachePath = null;
      // get file thumbnail when there is no file cache path on local
      if (fileCache == null) {
        NoteFileData fileResource = checkFileResourceExist(clientResourceID);
        fileCache = cacheTaskQueue.cacheCloudThumbnail(sessionUser, fileResource.getFileData(), thumbnailType);
        fileCachePath = fileCache.getCacheFileData().getFileCachePath();
        isCacheUpdate = true;
      } else {
        fileCachePath = fileCache.getCacheFileData().getFileCachePath();
        File cacheFile = new File(fileCachePath);
        if (fileCachePath == null || !cacheFile.exists() || cacheFile.length() == 0) {
          NoteFileData fileResource = checkFileResourceExist(clientResourceID);
          fileCache = cacheTaskQueue.cacheCloudThumbnail(sessionUser, fileResource.getFileData(), thumbnailType);
          isCacheUpdate = true;
        }
      }

      // get file thumbnail when there exists file cache path on local
      if (fileCache.getThumbnailCacheMap() == null || fileCache.getThumbnailCacheMap().get(thumbnailType) == null) {
        ThumbnailCache thumbnailCache = null;
        String thumbnailFilePath = ThumbnailCache.getThumbnailCachePath(fileCachePath, thumbnailType);
        File cacheThumbnailFile = new File(thumbnailFilePath);
        if (!cacheThumbnailFile.exists() || cacheThumbnailFile.length() == 0) {
          thumbnailCache = cacheTaskQueue.cacheLocalThumbnail(fileCachePath, thumbnailType, true);
          if (thumbnailCache == null) {
            logger.error("Fail to create thumbnail of file " + fileName + "!");
            throw new FileResourceException("Fail to create thumbnail of file " + fileName + "!");
          }
        } else {
          thumbnailCache = new ThumbnailCache(thumbnailType, thumbnailFilePath);
        }
        fileCache.setThumbnailCacheMap(thumbnailCache);
        isCacheUpdate = true;
      }

      String cacheContentType = fileCache.getContentType();
      if (cacheContentType == null || "null".equals(cacheContentType)) {
        String name = fileCache.getFileName() == null ? fileName : fileCache.getFileName();
        cacheContentType = FileMimeType.getContentTypeByName(name);
        fileCache.setFileName(name);
        fileCache.setContentType(cacheContentType);
        isCacheUpdate = true;
      }
      if (isCacheUpdate) {
        fileStoreService.setFileCache(clientResourceID, fileCache);
      }
    } catch (FileResourceException frx) {
      fileCache = null;
      logger.error("Couldn't find the file thumbnail when getting cloud file data!", frx);
    }
    return fileCache;
  }

  private NoteFileData checkFileResourceExist(String clientResourceID) {
    NoteFileData fileResource = (NoteFileData) fileStoreService.getFileResource(clientResourceID);
    if (fileResource == null) {
      throw new FileResourceException("Couldn't find the file resource data using clientResourceID " + clientResourceID);
    }
    return fileResource;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public ServiceResult<JSONObject> deleteData(JSONObject sessionUser, Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    if (resourceInfo == null) {
      logger.error("Couldn't find this file resource when deleting resource!");
      throw new FileResourceException("Couldn't find this file resource when deleting resource!");
    }
    List<NoteFileData> deleteList = new ArrayList<NoteFileData>();
    JSONObject tokenInfo = cloudManageService.getCloudToken(sessionUser);
    String token = tokenInfo.getString("token");

    JSONObject jsonParam = (JSONObject) resourceInfo;
    JSONArray array = (JSONArray) jsonParam.get(FileDataConstant.RESOURCES);
    Iterator<String> iter = array.iterator();
    while (iter.hasNext()) {
      String clientResourceID = iter.next();
      NoteFileData fileResource = (NoteFileData) fileStoreService.getFileResource(clientResourceID);
      if (fileResource != null) {
        fileStoreService.invalidateFileResource(clientResourceID);
        executeCOSTask(CloudFileExecutionCode.DELETE_OBJECT, token, fileResource.getFileData());
        deleteList.add(fileResource);
      }
    }

    JSONObject returnFile = new JSONObject();
    returnFile.put(FileDataConstant.EXECUTION_RESULT, TransferObjectAssembler.transformFileResourceData(deleteList));
    sr.setBusinessResult(returnFile);
    return sr;
  }

  /**
   * 
   * @info
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public ServiceResult<JSONObject> batchCreatePublicLink(JSONObject sessionUser, Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    JSONObject tokenInfo = cloudManageService.getCloudToken(sessionUser);
    String token = tokenInfo.getString("token");

    JSONObject jsonParam = (JSONObject) resourceInfo;
    JSONArray array = (JSONArray) jsonParam.get(FileDataConstant.RESOURCES);
    // Long expireTime=(Long)jsonParam.get(FileDataConstant.LINK_VALIDTIME);

    int size = array.size();
    CloudFileData[] cplList = new CloudFileData[size];
    int i = 0;
    Iterator<JSONObject> iter = array.iterator();
    while (iter.hasNext()) {
      JSONObject json = iter.next();
      CloudFileData cdata = new CloudFileData();
      JSONObject fileDataJSON = (JSONObject) json.get(FileDataConstant.FILE_DATE);
      cdata.setKey((String) fileDataJSON.get(FileDataConstant.RESOURCE_KEY_ID));
      cplList[i] = cdata;
      i++;
    }

    int cpsize = cplList.length;
    List<?> resultList = null;
    if (cpsize > 0) {
      resultList = (List<?>) executeCallableCOSTask(CloudFileExecutionCode.BATCH_CREATE_PUBLICLINKS, token, cplList);
      for (Object m : resultList) {
        Map map = (Map) m;
        Object keyID = map.get("key");
        if (!(keyID == null || keyID instanceof JSONNull)) {
          map.put("key", Arrays.asList(keyID));
          if (!StringUtils.isBlank((String) map.get("public_link"))) {
            mongoDao.update("updateResourcePublicLink", map);
          }
        }
      }
    }

    JSONObject returnFile = new JSONObject();
    if (resultList != null && resultList.size() > 0) {
      returnFile.put(FileDataConstant.EXECUTION_RESULT, FileDataConstant.FILE_STATE_SUCCESS);
    } else {
      returnFile.put(FileDataConstant.EXECUTION_RESULT, FileDataConstant.FILE_STATE_FAILURE);
    }
    sr.setBusinessResult(returnFile);
    return sr;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public ServiceResult<JSONObject> deletePublicLink(JSONObject sessionUser, Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    JSONObject tokenInfo = cloudManageService.getCloudToken(sessionUser);
    String token = tokenInfo.getString("token");

    JSONObject jsonParam = (JSONObject) resourceInfo;
    JSONArray array = (JSONArray) jsonParam.get(FileDataConstant.RESOURCES);
    List<NoteFileData> dplList = new ArrayList<NoteFileData>(array.size());
    Iterator<String> iter = array.iterator();
    while (iter.hasNext()) {
      String clientResourceID = iter.next();
      NoteFileData fileResource = (NoteFileData) fileStoreService.getFileResource(clientResourceID);
      if (fileResource != null) {
        executeCOSTask(CloudFileExecutionCode.DELETE_PUBLICLINK, token, fileResource.getFileData());
        dplList.add(fileResource);
      }
    }

    JSONObject returnFile = new JSONObject();
    returnFile.put(FileDataConstant.EXECUTION_RESULT, TransferObjectAssembler.transformFileResourceData(dplList));
    sr.setBusinessResult(returnFile);
    return sr;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public ServiceResult<JSONObject> getShareResource(Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    String clientResourceID = (String) resourceInfo.get(FileDataConstant.CLIENT_RESOURCE_ID);
    String fileName = (String) resourceInfo.get(FileDataConstant.FILE_NAME);
    if (!resourceInfo.containsKey(FileDataConstant.RESOURCE_PUBLIC_LINK)) {
      throw new FileResourceException("Couldn't find the resource public link which clientResourceID is " + clientResourceID + "!");
    }
    String publicLink = (String) resourceInfo.get(FileDataConstant.RESOURCE_PUBLIC_LINK);
    String contentType = FileMimeType.getContentTypeByName(fileName);

    String fileCachePath = null;
    CloudFileData fileCache = fileStoreService.getFileCache(clientResourceID);
    if (fileCache == null) {
      NoteFileData fileResource = checkFileResourceExist(clientResourceID);
      if (fileResource.getFileData().getCacheFileData().getFileCachePath() != null) {
        fileCachePath = fileResource.getFileData().getCacheFileData().getFileCachePath();
      } else {
        String keyID = fileResource.getFileData().getKey();
        if (publicLink == null && fileResource.getFileData().getPublicLink() == null) {
          throw new FileResourceException("Couldn't find the correct public link of resource file which keyID is " + keyID + "!");
        }
        fileCachePath = saveShareFileonLocal(keyID, contentType, publicLink);
      }
    } else {
      fileCachePath = fileCache.getCacheFileData().getFileCachePath();
    }

    JSONObject returnFile = AudioConvertor.audioHandler(fileName, contentType, fileCachePath);
    if (returnFile == null) {
      logger.error("Fail to convert audio file in the path " + fileCachePath + "!");
      throw new FileResourceException("Fail to convert audio file in the path " + fileCachePath + "!");
    }
    sr.setBusinessResult(returnFile);
    return sr;
  }

  /**
   * @info access CloudFileData
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public ServiceResult<JSONObject> publishInformation(Map resourceInfo) throws FileResourceException {
    ServiceResult<JSONObject> sr = new ServiceResult<JSONObject>();
    String connector = cloudManageService.getConnectorByUser(SystemProperties.cloudUser, SystemProperties.cloudPassword);
    List<CloudFileData> fileArray = (List<CloudFileData>) resourceInfo.get(FileDataConstant.FILE_FIELD);
    int fileCount = fileArray.size();
    List<CloudFileData> returnFileArray = new ArrayList<CloudFileData>(fileCount);
    if (fileCount > 0) {
      CloudFileData fileData = (CloudFileData) fileArray.get(0);
      fileData.setBucketName(SystemProperties.bucketName);
      fileData.setToken(connector);
      CallbackData callback = new CallbackData();
      Map<String, String> mdata = new HashMap<String, String>();
      mdata.put(PilotOssConstants.DISPLAY_NAME, fileData.getFileName());
      callback.setData(mdata);
      fileData.setCallback(callback);

      CloudFileData returnData = (CloudFileData) executeCallableCOSTask(CloudFileExecutionCode.PUT_OBJECT, connector, fileData);
      fileData.getCacheFileData().setFileByte(null);
      if (returnData != null) {
        CloudFileData returnData2 = (CloudFileData) executeCallableCOSTask(CloudFileExecutionCode.CREATE_PUBLICLINK, connector, fileData);
        returnFileArray.add(returnData2);
      }
    }

    JSONObject jsAttachs = new JSONObject();
    jsAttachs.put(FileDataConstant.FILE_STATE, FileDataConstant.FILE_STATE_SUCCESS);
    jsAttachs.put(FileDataConstant.EXECUTION_RESULT, TransferObjectAssembler.transformFileData(returnFileArray));
    sr.setBusinessResult(jsAttachs);
    return sr;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private ServiceResult<List<NoteFileData>> uploadFiles(JSONObject sessionUser, Map resourceInfo) throws FileResourceException {
    ServiceResult<List<NoteFileData>> sr = new ServiceResult<List<NoteFileData>>();
    String userID = (String) sessionUser.get("superNoteUserID");
    if (userID == null || "".equals(userID)) {
      sr.setReturnCode(ReturnCode.UNAUTHORIZED);
      logger.error("Authentication bad because userID is null or blank!");
      return sr;
    }
    String callbackToken = (String) sessionUser.get("token");
    JSONObject tokenInfo = cloudManageService.getCloudToken(sessionUser);
    String token = tokenInfo.getString("token");

    List<CloudFileData> fileArray = (List<CloudFileData>) resourceInfo.get(FileDataConstant.FILE_FIELD);
    int fileCount = fileArray.size();
    List<NoteFileData> fileDataList = new ArrayList<NoteFileData>(fileCount);
    if (fileCount > 0) {
      for (int i = 0; i < fileCount; i++) {
        CloudFileData fileData = (CloudFileData) fileArray.get(i);
        fileData.setBucketName(SystemProperties.bucketName);
        fileData.setToken(token);
        if (SystemProperties.cloudCallbackStatus) {
          fileData.setCallback(assembleCallbackData(fileData.getFileName(), callbackToken));
        }
        NoteFileData fileResource = NoteFileData.create(userID,fileData);

        String clientResourceID = fileResource.getClientResourceID();
        String keyId = fileData.getKey();
        // Save File in Cache
        InputStream input = new ByteArrayInputStream(fileData.getCacheFileData().getFileByte());
        String filecache = fileStoreService.saveFileOnLocal(input, keyId);
        if (filecache != null) {
          fileData.getCacheFileData().setFileCachePath(filecache);
          if (SystemProperties.cloudCallbackStatus) {
            // cloudCallback status is true so that cloud status will be updated after callback is
            // called.
            fileData.setCloudStatus(0);
          } else {
            fileData.setCloudStatus(1);
          }
          fileData.setCloudSize(fileData.getCacheFileData().getCacheSize());
          // Insert file info into Cache ant the status is 0
          fileStoreService.saveFileResource(clientResourceID, fileResource);
          fileStoreService.setFileCache(clientResourceID, fileResource.getFileData());
          logger.info("Set file cache which the clientResourceID:" + clientResourceID + " before uploading file.");
        }
        executeCOSTask(CloudFileExecutionCode.PUT_OBJECT, token, fileData);
        fileData.getCacheFileData().setFileByte(null);
        fileDataList.add(fileResource);
      }
    }
    sr.setBusinessResult(fileDataList);
    return sr;
  }

  private CallbackData assembleCallbackData(String fileName, String userToken) {
    CallbackData callback = new CallbackData();
    callback.setUrl(SystemProperties.cloudCallbackURL);
    Map<String, String> mdata = new HashMap<String, String>();
    mdata.put(PilotOssConstants.DISPLAY_NAME, fileName);
    mdata.put(PilotOssConstants.CALLBACK_TOKEN, userToken);
    callback.setData(mdata);
    return callback;
  }

  private String saveShareFileonLocal(String resourceKeyID, String contentType, String resourcePublicLink) {
    HttpClient httpClient = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(resourcePublicLink);
    HttpResponse cosreponse;
    InputStream bis = null;
    try {
      get.addHeader("Content-Type", contentType);
      cosreponse = httpClient.execute(get);

      bis = new BufferedInputStream(cosreponse.getEntity().getContent());
      return fileStoreService.saveFileOnLocal(bis, resourceKeyID);
    } catch (IOException ioe) {
      logger.error("saveShareFileonLocal IOException!", ioe);
      throw new FileResourceException("saveShareFileonLocal IOException!", ioe);
    }
  }

  private Object executeCallableCOSTask(CloudFileExecutionCode signal, String connector, CloudFileData... fileData) {
    try {
      COSExecutionTask task = new COSExecutionTask(signal, connector, fileData);
      Future<Object> future = threadPoolTaskExecutor.submit(task);
      if (future != null) {
        return future.get();
      }
      return null;
    } catch (InterruptedException e) {
      logger.error("executeCallableCOSTask InterruptedException!", e);
      throw new FileResourceException("Couldn't find the correct file " + fileData[0].getKey() + " from Cloud!");
    } catch (ExecutionException e) {
      logger.error("executeCallableCOSTask ExecutionException!", e);
      throw new FileResourceException("Couldn't find the correct file " + fileData[0].getKey() + " from Cloud!");
    }
  }

  private void executeCOSTask(CloudFileExecutionCode signal, String connector, CloudFileData... fileData) {
    COSExecutionTask task = new COSExecutionTask(signal, connector, fileData);
    FutureTask<Object> futureTask = new FutureTask<Object>(task);
    threadPoolTaskExecutor.execute(futureTask);
  }

  public void setFileStoreService(FileStoreServiceIf fileStoreService) {
    this.fileStoreService = fileStoreService;
  }

  public void setMongoDao(MongoDaoIf mongoDao) {
    this.mongoDao = mongoDao;
  }

  public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
    this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    this.threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
  }

  public void setCacheTaskQueue(FileCacheTaskQueue cacheTaskQueue) {
    this.cacheTaskQueue = cacheTaskQueue;
  }

  public void setCloudManageService(CloudManageService cloudManageService) {
    this.cloudManageService = cloudManageService;
  }

}
