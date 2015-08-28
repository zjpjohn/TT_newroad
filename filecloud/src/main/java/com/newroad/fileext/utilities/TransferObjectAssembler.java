package com.newroad.fileext.utilities;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;

import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.NoteFileData;

/**
 * tangzj1 Apr 1, 2014
 */
public class TransferObjectAssembler {

  public static JSONArray transformFileResourceData(List<NoteFileData> list) {
    JSONArray ja = new JSONArray();
    if (CollectionUtils.isEmpty(list)) {
      return ja;
    }
    for (NoteFileData resource : list) {
      CloudFileData atta = resource.getFileData();
      JSONObject jo = new JSONObject();
      jo.put(FileDataConstant.CLIENT_RESOURCE_ID, resource.getClientResourceID());
      jo.put(FileDataConstant.RESOURCE_KEY_ID, atta.getKey());
      // jo.put(FileDataConstant.RESOURCE_LINK, atta.getLink());
      jo.put(FileDataConstant.FILE_NAME, atta.getFileName());
      String contentType = atta.getContentType();
      if (contentType != null) {
        jo.put(FileDataConstant.FILE_CONTENT_TYPE, contentType);
      }
      // Cos size from cloud
      Long fileSize = atta.getCloudSize();
      if (fileSize > 0l) {
        jo.put(FileDataConstant.FILE_SIZE, fileSize);
      }

      String publicLink = atta.getPublicLink();
      if (publicLink != null) {
        jo.put(FileDataConstant.RESOURCE_PUBLIC_LINK, publicLink);
      }
      // jo.put(FileDataConstant.EXECUTION_STATUS,
      // atta.getStatus() == 1 ? true : false);
      ja.add(jo);
    }
    return ja;
  }

  public static JSONArray transformFileData(List<CloudFileData> list) {
    JSONArray ja = new JSONArray();
    if (CollectionUtils.isEmpty(list)) {
      return ja;
    }
    for (CloudFileData atta : list) {
      JSONObject jo = new JSONObject();
      jo.put(FileDataConstant.RESOURCE_KEY_ID, atta.getKey());
      // jo.put(FileDataConstant.RESOURCE_LINK, atta.getLink());
      jo.put(FileDataConstant.FILE_NAME, atta.getFileName());
      String contentType = atta.getContentType();
      if (contentType != null) {
        jo.put(FileDataConstant.FILE_CONTENT_TYPE, contentType);
      }
      // Cos size from cloud
      Long fileSize = atta.getCloudSize();
      if (fileSize > 0l) {
        jo.put(FileDataConstant.FILE_SIZE, fileSize);
      }

      String publicLink = atta.getPublicLink();
      if (publicLink != null) {
        jo.put(FileDataConstant.RESOURCE_PUBLIC_LINK, publicLink);
      }
      // jo.put(FileDataConstant.EXECUTION_STATUS,
      // atta.getStatus() == 1 ? true : false);
      ja.add(jo);
    }
    return ja;
  }
  
}
