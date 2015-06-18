package com.newroad.fileext.service.api;

import java.util.Map;

import net.sf.json.JSONObject;

import com.newroad.fileext.data.model.ThumbnailType;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.util.apiresult.ServiceResult;

/**
 * @Info File Extend service api interface,support mongoDB and COS cloud service
 * @author tangzj1
 * @param <K>
 * @param <V>
 * 
 */
public interface FileExtendServiceIf {

  /**
   * @param sessionUser
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> getObjectInfo(JSONObject sessionUser, Map resourceInfo) throws FileResourceException;

  /**
   * @param sessionUser
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> uploadData(JSONObject sessionUser, Map resourceInfo) throws FileResourceException;

  /**
   * @param sessionUser
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> downloadData(JSONObject sessionUser, Map resourceInfo) throws FileResourceException;

  /**
   * @param sessionUser
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> deleteData(JSONObject sessionUser, Map resourceInfo) throws FileResourceException;

  /**
   * @param sessionUser
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> batchCreatePublicLink(JSONObject sessionUser, Map resourceInfo) throws FileResourceException;

  /**
   * @param sessionUser
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> deletePublicLink(JSONObject sessionUser, Map resourceInfo) throws FileResourceException;

  /**
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> getShareResource(Map resourceInfo) throws FileResourceException;

  /**
   * @param resourceInfo
   * @return
   * @throws FileDataException
   */
  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> publishInformation(Map resourceInfo) throws FileResourceException;

  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> getThumbnail(JSONObject sessionUser, Map resourceInfo, ThumbnailType thumbnailType)
      throws FileResourceException;

  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> getCloudThumbnail(JSONObject sessionUser, Map resourceInfo, ThumbnailType thumbnailType)
      throws FileResourceException;

  @SuppressWarnings("rawtypes")
  public ServiceResult<JSONObject> getFileIcon(JSONObject sessionUser, Map resourceInfo) throws FileResourceException;
}
