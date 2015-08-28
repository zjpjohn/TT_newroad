package com.newroad.fileext.service.api;

import java.util.List;

import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.util.apiresult.ServiceResult;


public interface FileResourceServiceIf {
  /**
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  public ServiceResult<String> uploadFile(CloudFileData fileData) throws FileResourceException;
  
  public ServiceResult<List<String>> uploadCloudFiles(Long userId, List<CloudFileData> fileDataList) throws FileResourceException;
  
  public ServiceResult<String> saveFileMetaData(Long userId, CloudFileData cloudFileData) throws FileResourceException;
  

}
