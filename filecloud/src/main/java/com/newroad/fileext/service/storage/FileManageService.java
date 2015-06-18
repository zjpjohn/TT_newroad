package com.newroad.fileext.service.storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.newroad.cache.common.couchbase.CouchbaseCache;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.CommonFileData;
import com.newroad.fileext.service.api.FileStoreServiceIf;
import com.newroad.fileext.service.storage.cache.ClusterManager;
import com.newroad.fileext.service.storage.cache.Node;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.mongodb.orm.db.MongoDaoIf;
import com.newroad.util.iohandler.TypeConvertor;

/**
 * @info FileCacheServiceImpl
 * @author tangzj1
 * @date Nov 27, 2013
 * @version
 */
@Service
public class FileManageService implements FileStoreServiceIf {

  private static Logger logger = LoggerFactory.getLogger(FileManageService.class);

  private MongoDaoIf mongoDao;

  private CouchbaseCache newroadCache;

  /**
   * @param key
   * @return
   * @throws Exception
   */
  public CommonFileData getFileResource(final String fileId) {
    CommonFileData fileResource = getFileResourceById(fileId, null, 1);
    if (fileResource == null || fileResource.getCloudFileData().getKeyId() == null) {
      return null;
    }
    return fileResource;
  }

  /**
   * @param key
   * @param NoteFileData
   */
  public void saveFileResource(final String clientResourceID, CommonFileData fileResource) {
    CommonFileData returnData = getFileResourceById(clientResourceID, null, 1);
    if (returnData == null) {
      mongoDao.insert(FileDataConstant.INSERT_FILE, fileResource);
    } else {
      updateFileResource(clientResourceID, fileResource);
    }
  }

  public void updateFileResource(final String clientResourceID, CommonFileData commonFileData) {

    String fileCachePath = commonFileData.getCloudFileData().getCacheFileData().getFileCachePath();
    String contentType = commonFileData.getCloudFileData().getContentType();
    Map<String, Object> queryMap = new HashMap<String, Object>(1);
    queryMap.put("clientResourceID", clientResourceID);
    Map<String, Object> updateMap = new HashMap<String, Object>(4);
    if (fileCachePath != null) {
      updateMap.put("fileData.fileCachePath", fileCachePath);
    }
    if (contentType != null && !"null".equals(contentType)) {
      updateMap.put("fileData.contentType", contentType);
    }
    if (commonFileData.getFileType() != null) {
      updateMap.put("resourceType", commonFileData.getFileType());
    }
    long now = System.currentTimeMillis();
    updateMap.put("lastUpdateTime", now);
    mongoDao.update(FileDataConstant.UPDATE_FILE_BY_CLIENTID, queryMap, updateMap);
    logger.debug("FileResource update clientResourceID=" + clientResourceID);
  }

  /**
   * @param key
   */
  public void invalidateFileResource(String clientResourceID) {
    newroadCache.delete(clientResourceID);
    Map<String, Object> map = new HashMap<String, Object>(2);
    map.put("clientResourceID", clientResourceID);
    map.put("status", 2);
    mongoDao.update(FileDataConstant.UPDATE_FILE_BY_CLIENTID, map);
  }

  public void setFileCache(String clientResourceID, CloudFileData fileCache) {
    newroadCache.set(clientResourceID, fileCache, true);
  }

  public CloudFileData getFileCache(String clientResourceID) {
    Object obj = newroadCache.get(clientResourceID);
    if (obj != null && obj instanceof CloudFileData) {
      return (CloudFileData) newroadCache.get(clientResourceID);
    } else {
      return null;
    }
  }

  private CommonFileData getFileResourceById(String fileId, String userID, Integer... status) {
    Map<String, Object> map = new HashMap<String, Object>(3);
    map.put("fileId", fileId);
    if (userID != null && !"".equals(userID))
      map.put("ownerId", userID);
    List<Integer> statusList = Arrays.asList(status);
    map.put("status", statusList);
    // logger.debug("getFileResourceByKeyStatus field:" + map.toString());
    CommonFileData FileResource = (CommonFileData) mongoDao.selectOne(FileDataConstant.SELECT_FILE_BY_CLIENTID, map);
    return FileResource;
  }

  public List<?> getFileListByStatus(Integer... status) {
    Map<String, Object> map = new HashMap<String, Object>(2);
    List<Integer> statusList = Arrays.asList(status);
    map.put("status", statusList);
    return mongoDao.selectList(FileDataConstant.SELECT_FILES_BY_STATUS, map, 0, 0);
  }

  public String createCacheFilePath(String cacheFileName) {
    Node node = ClusterManager.getNodeInfo();
    String remoteFilePath = createRemoteFilePath(node.getPath(), cacheFileName);
    return remoteFilePath;
  }

  public String saveFileOnLocal(InputStream input, String keyId) {
    try {
      String remoteFilePath = createCacheFilePath(keyId);
      File localFile = TypeConvertor.inputStream2File(input, remoteFilePath);
      // File localFile = TypeConvertor.inputStreamNIO2File(input,
      // remoteFilePath);
      String returnFilePath = localFile.getAbsolutePath();
      logger.info("Save cache file on local in " + returnFilePath + " & size=" + localFile.length());
      return returnFilePath;
    } catch (Exception e) {
      logger.error("saveFileOnLocal Exception!", e);
      throw new FileResourceException("saveFileOnLocal IOException!", e);
    } finally {
      try {
        if (input != null)
          input.close();
      } catch (IOException e) {
        logger.error("saveFileOnLocal IOException!", e);
      }
    }
  }

  /**
   * @info create remote file path on the specific node
   * @param remoteNodePath
   * @param remotefileName
   * @return
   * @throws FileResourceException
   */
  private String createRemoteFilePath(String remoteNodePath, String remotefileName) throws FileResourceException {
    String remoteFilePath = null;

    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int date = cal.get(Calendar.DATE);
    int hourInterval = cal.get(Calendar.HOUR_OF_DAY) / 3;
    String currentYear = String.valueOf(year);
    String currentDate = String.valueOf(month) + "_" + String.valueOf(date);
    String currentHourInterval = String.valueOf(hourInterval);

    String remoteFolderPath = remoteNodePath + File.separator + currentYear;
    File folder1 = new File(remoteFolderPath);
    if (!folder1.exists() || (!folder1.isDirectory())) {
      folder1.mkdirs();
    }

    remoteFolderPath = remoteFolderPath + File.separator + currentDate;
    File folder2 = new File(remoteFolderPath);
    if (!folder2.exists() || (!folder2.isDirectory())) {
      folder2.mkdirs();
    }

    remoteFolderPath = remoteFolderPath + File.separator + currentHourInterval;
    File folder3 = new File(remoteFolderPath);
    if (!folder3.exists() || (!folder3.isDirectory())) {
      folder3.mkdirs();
    }

    File remoteFolder = new File(remoteFolderPath);
    if (remoteFolder.exists()) {
      remoteFilePath = remoteFolderPath + File.separator + remotefileName;
      logger.debug("Remote File Path:" + remoteFilePath);
      return remoteFilePath;
    } else {
      throw new FileResourceException("Couldn't create new folder on the local disk! The current folder is " + remoteFolderPath);
    }
  }


  public void setMongoDao(MongoDaoIf mongoDao) {
    this.mongoDao = mongoDao;
  }

  public void setNewroadCache(CouchbaseCache newroadCache) {
    this.newroadCache = newroadCache;
  }

}
