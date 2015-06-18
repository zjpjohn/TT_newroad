package com.newroad.fileext.service.api;

import java.io.InputStream;
import java.util.List;

import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.CommonFileData;

/**
 * @info FileCacheService
 * @author tangzj1
 * @date Dec 2, 2013
 * @version
 */
public interface FileStoreServiceIf {

  
  //File MetaData DB Storage
  public CommonFileData getFileResource(final String fileId);

  public void saveFileResource(final String fileId, CommonFileData fileMetaData);
  
  public void updateFileResource(final String fileId, CommonFileData fileMetaData);

  public void invalidateFileResource(String fileId);
  
  
  // File MetaData Cache
  public void setFileCache(String clientResourceID,CloudFileData fileCache);
  
  public CloudFileData getFileCache(String clientResourceID);
  
  public String createCacheFilePath(String cacheFileName);

  public String saveFileOnLocal(InputStream input, String keyId);

  public List<?> getFileListByStatus(Integer... status);
}
