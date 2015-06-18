package com.newroad.fileext.dao.cos;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.pilot.PilotException;
import com.lenovo.pilot.PilotOssCloud;
import com.lenovo.pilot.PilotOssEx;
import com.lenovo.pilot.PilotOssListenerEx;
import com.lenovo.pilot.PilotOssObjectBaseEx;
import com.lenovo.pilot.PilotOssObjectListEx;
import com.lenovo.pilot.TaskStatus;
import com.lenovo.pilot.util.CallbackData;
import com.lenovo.pilot.util.PilotOssConstants.ThumbnailGenerateType;
import com.lenovo.pilot.util.ResourceData;
import com.newroad.fileext.dao.CloudFileExecutionCode;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.ThumbnailCache;
import com.newroad.fileext.data.model.ThumbnailType;
import com.newroad.fileext.utilities.SystemProperties;
import com.newroad.util.MathsHelper;
import com.newroad.util.exception.COSException;

public class COSExecutionTask implements Callable<Object> {

  private static Logger logger = LoggerFactory.getLogger(COSExecutionTask.class);

  private CloudFileExecutionCode signal;
  private String bucketName;
  private String connector;
  private CloudFileData[] cdata = null;
  private Boolean useListener = false;
  private String ossMessage = null;

  private PilotOssEx oss = null;
  private PilotOssObjectBaseEx object = null;
  private PilotOssObjectListEx batchObjects = null;

  public COSExecutionTask(CloudFileExecutionCode signal, String connector, CloudFileData... cdata) {
    this.signal = signal;
    this.connector = connector;
    this.cdata = cdata;
  }

  public PilotOssEx connect() {
    String token = connector;
    logger.debug("COSDao connect token:" + token);
    PilotOssEx cos = PilotOssCloud.CreateOssCloudEx();
    try {
      if (token.indexOf("lws_token") >= 0) {
        cos.useToken(token);
      } else {
        cos.loginByConnector(SystemProperties.devID, SystemProperties.devKey, token);
        // cos.login(SystemProperties.appID, SystemProperties.devID, SystemProperties.devKey,
        // SystemProperties.testUser,
        // SystemProperties.testPassword, "", SystemProperties.workspace);
      }
      this.useListener = SystemProperties.useListener;
      return cos;
    } catch (IOException e) {
      logger.error("COS initial connection IOException for token " + token + ":", e);
      throw new COSException("COS initial connection IOException for token " + token + ":", e);
    }
  }

  @Override
  public Object call() throws Exception {
    this.bucketName = SystemProperties.bucketName;
    oss = connect();
    if (cdata != null && signal.getSignal() < 9) {
      return executeObject(bucketName, cdata[0]);
    } else if (cdata != null && signal.getSignal() >= 9) {
      return executeBatchObjects(bucketName, cdata);
    }
    return cdata;
  }

  public Object executeObject(String bucketName, CloudFileData cdata) throws PilotException {
    boolean result = false;
    cdata.setBucketName(bucketName);
    String keyID = cdata.getKeyId();
    String url = cdata.getLink();
    Long offset = 0L;

    OssCallbacks ossCallback = new OssCallbacks();
    try {
      switch (signal) {
        case GET_OBJECT:
          object = getOssObject(bucketName, keyID, url);
          object.registerListener(ossCallback);
          if (cdata.getCloudSize() == null) {
            cdata.setCloudSize(-1L);
          }
          if (cdata.getCacheFileData().getOffset() != null) {
            offset = cdata.getCacheFileData().getOffset();
          }
          result = getObject(cdata.getCacheFileData().getFileCachePath(), offset, cdata.getCloudSize());
          Map<String, String> info = object.getObjectInfoMap();
          if (info != null) {
            checkCloudContentType(cdata, info.get("media_type"));
            Long size = Long.parseLong(info.get("size"));
            cdata.setCloudSize(size);
          }
          cdata.setCloudExecuteStatus(result);
          logger.info("The cos execute result when getting object is:" + result);
          break;
        case PUT_OBJECT:
          // If keyID is null, create a new object.If key isn't null,
          // create a new object by this keyID.
          object = oss.createObject(bucketName, keyID, url);
          object.registerListener(ossCallback);

          String filePath = cdata.getCacheFileData().getFileCachePath();
          Long cacheSize = cdata.getCacheFileData().getCacheSize();
          if (cdata.getCacheFileData().getOffset() != null) {
            offset = cdata.getCacheFileData().getOffset();
          }
          if (filePath != null) {
            result = putObject(cdata.getCacheFileData().getFileCachePath(), cdata.getContentType(), offset, cacheSize, cdata.getCallback());
          } else {
            InputStream input = new ByteArrayInputStream(cdata.getCacheFileData().getFileByte());
            result = putObject(input, cdata.getContentType(), cdata.getCacheFileData().getOffset(), cacheSize, cdata.getCallback());
          }
          cdata.setKeyId(object.getKeyID());
          cdata.setLink(object.getUrl());
          cdata.setCloudSize(cacheSize);
          cdata.setCloudExecuteStatus(result);
          logger.info("The cos execute result when putting object is:" + result);
          break;
        case DELETE_OBJECT:
          object = getOssObject(bucketName, keyID, url);
          object.registerListener(ossCallback);
          Map<String, String> deleteInfo = deleteObject(cdata.getCallback());
          if (deleteInfo != null && keyID.equals((String) deleteInfo.get("key"))) {
            result = true;
          }
          cdata.setCloudExecuteStatus(result);
          logger.info("The cos execute result when deleting object is:" + result);
          break;
        case GET_OBJECT_INFO:
          object = getOssObject(bucketName, keyID, url);
          Map<String, String> objectInfo = getObjectInfo();
          if (objectInfo != null) {
            cdata.setFileName(objectInfo.get("display_name"));
            cdata.setCloudSize(Long.valueOf(objectInfo.get("size")));
            checkCloudContentType(cdata, objectInfo.get("media_type"));
            result = true;
          }
          cdata.setCloudExecuteStatus(result);
          logger.info("The cos execute result when getting object info is:" + result);
          break;
        case CREATE_PUBLICLINK:
          object = getOssObject(bucketName, keyID, url);
          object.registerListener(ossCallback);
          String publicLink = createPublicLink(-1);
          if (publicLink != null) {
            cdata.setPublicLink(publicLink);
            result = true;
          }
          cdata.setCloudExecuteStatus(result);
          logger.info("The cos execute result when creating public link is:" + result);
          break;
        case DELETE_PUBLICLINK:
          object = getOssObject(bucketName, keyID, url);
          object.registerListener(ossCallback);
          String deletePubLink = deletePublicLink(cdata.getCallback());
          if (deletePubLink != null) {
            cdata.setPublicLink(deletePubLink);
            result = true;
          }
          cdata.setCloudExecuteStatus(result);
          logger.info("The cos execute result when deleting public link is:" + result);
          break;
        case GET_OBJECT_THUMBNAIL:
          object = getOssObject(bucketName, keyID, url);
          ThumbnailCache thumbnailCache = cdata.getCurrentThumbnailCache();
          ThumbnailType thumbnailType = thumbnailCache.getThumbnailType();
          result =
              getObjectThumbnail(thumbnailCache.getThumbnailCachePath(), thumbnailType.getWidth(), thumbnailType.getHeight(),
                  cdata.getFileName());
          cdata.setCloudExecuteStatus(result);
          logger.info("The cos execute result when getting object thumbnail is:" + result);
          break;
        default:
          break;
      }

    } catch (PilotException e) {
      cdata = null;
      logger.error(signal + " PilotException for object key " + keyID + ":", e);
      throw new COSException(signal + " PilotException for object key " + keyID + " execute failure!");
    } finally {
      if (object != null) {
        object.unregisterListener(ossCallback);
        object.stopTransfer();
        logger.debug("Stop object data transfer!");
      }
    }
    return cdata;
  }

  public Object executeBatchObjects(String bucketName, CloudFileData[] dataArray) throws PilotException {
    OssCallbacks ossCallback = new OssCallbacks();
    Object objList = null;

    int fileNum = dataArray.length;
    String[] keys = new String[fileNum];
    for (int i = 0; i < fileNum; i++) {
      CloudFileData fileData = dataArray[i];
      String key = fileData.getKeyId();
      if (key != null) {
        keys[i] = fileData.getKeyId();
      }
    }
    try {
      switch (signal) {
        case BATCH_CREATE_OBJECTS:

          break;
        case BATCH_PUT_OBJECTS:
          if (keys.length <= 0 || keys[0] == null) {
            batchObjects = oss.batchCreateObjects(bucketName, fileNum, null);
          } else {
            batchObjects = oss.getOssObjectList(bucketName, keys);
          }

          keys = batchObjects.getKeyIDs();
          List<ResourceData> resourceList = new ArrayList<ResourceData>(fileNum);
          long batchFileSize = 0;
          for (int i = 0; i < fileNum; i++) {
            String key = keys[i];
            CloudFileData fileData = dataArray[i];
            long filesize = fileData.getCacheFileData().getCacheSize();
            ResourceData resource = new ResourceData(key, fileData.getCacheFileData().getFileCachePath(), filesize, fileData.getContentType());
            batchFileSize += filesize;
            resourceList.add(resource);
          }
          batchObjects.registerListener(ossCallback);
          objList = batchPutObjectList(resourceList, batchFileSize, dataArray[0].getCallback());
          break;
        case BATCH_GET_OBJECTS:
          if (keys.length <= 0) {
            throw new COSException("No specific Objects could be found!");
          }
          batchObjects = oss.getOssObjectList(bucketName, keys);
          long fileSize2 = 0;
          for (CloudFileData fileData : dataArray) {
            fileSize2 += fileData.getCloudSize();
          }
          String tempFilePath = dataArray[0].getCacheFileData().getFileCachePath();
          if (tempFilePath == null) {
            throw new COSException("Couldn't find the specific output file path!");
          }
          batchObjects.registerListener(ossCallback);
          objList = batchGetObjectList(fileSize2, tempFilePath);
          break;
        case BATCH_CREATE_PUBLICLINKS:
          if (keys.length <= 0) {
            throw new COSException("No specific Objects could be found!");
          }
          batchObjects = oss.getOssObjectList(bucketName, keys);
          batchObjects.registerListener(ossCallback);
          objList = batchCreatePublicLink(-1);
          break;
        default:
          break;
      }
    } catch (PilotException e) {
      logger.error(signal + " PilotException for object keys " + keys[0] + ":", e);
      throw new COSException(signal + " PilotException for object keys " + keys[0] + " execute failure!");
    } finally {
      if (batchObjects != null) {
        batchObjects.unregisterListener(ossCallback);
        logger.info("Stop objectList data transfer!");
      }
    }
    return objList;
  }

  private PilotOssObjectBaseEx getOssObject(String BUCKETID, String KEYID, String URL) throws PilotException {
    PilotOssObjectBaseEx pilotOssObjectEx = null;
    if (KEYID != null) {
      pilotOssObjectEx = oss.getOssObject(BUCKETID, KEYID);
    } else if (URL != null) {
      pilotOssObjectEx = oss.getOssObject(URL);
    }
    if (pilotOssObjectEx == null) {
      throw new COSException("No specific Object Key " + KEYID + " could be found!");
    } else {
      return pilotOssObjectEx;
    }
  }

  private boolean putObject(String filePath, String contentType, long offset, long size, CallbackData callback) throws PilotException {
    boolean ret = false;
    File localFile = new File(filePath);
    if (localFile.exists()) {
      ret = object.putObject(localFile, contentType, offset, size, callback, null);
    }
    return ret;
  }

  private boolean putObject(InputStream inputStream, String contentType, long offset, long size, CallbackData callback)
      throws PilotException {
    boolean ret = false;
    ret = object.putObject(inputStream, contentType, offset, size, callback, null);
    return ret;
  }

  private Map<String, String> getObjectInfo() throws PilotException {
    return object.getObjectInfo();
  }

  private void checkCloudContentType(CloudFileData cdata, String cloudContentType) {
    String contentType = cdata.getContentType();
    if (cloudContentType != null && cloudContentType.indexOf("application/octet-stream") < 0 && !cloudContentType.equals(contentType)) {
      cdata.setContentType(cloudContentType);
    }
  }

  private boolean getObject(String filePath, Long offset, Long size) throws PilotException {
    boolean ret = false;
    OutputStream outStream = null;
    File tempFile = new File(filePath);
    try {
      outStream = new FileOutputStream(tempFile);
      ret = object.getObject(outStream, offset, size, null);
    } catch (FileNotFoundException e) {
      logger.error("getObject FileNotFoundException for filePath " + filePath + ":", e);
      throw new PilotException("getObject FileNotFoundException for filePath " + filePath + ":", e);
    } finally {
      try {
        if (outStream != null)
          outStream.close();
      } catch (IOException e) {
        logger.error("getObject IOException:", e);
      }
    }
    return ret;
  }

  private boolean getObjectThumbnail(String thumbnailFilePath, Integer width, Integer height, String fileName) throws PilotException {
    boolean ret = false;
    BufferedOutputStream stream = null;
    File tempFile = new File(thumbnailFilePath);
    try {
      stream = new BufferedOutputStream(new FileOutputStream(tempFile));
      byte[] thumbnailByte = object.getObjectThumbnail(width, height, ThumbnailGenerateType.normal, fileName);
      stream.write(thumbnailByte);
      ret = true;
    } catch (FileNotFoundException e) {
      logger.error("getObjectThumbnail FileNotFoundException for thumbnailFilePath " + thumbnailFilePath + ":", e);
      throw new PilotException("getObjectThumbnail FileNotFoundException for thumbnailFilePath " + thumbnailFilePath + ":", e);
    } catch (IOException e) {
      logger.error("getObjectThumbnail IOException for thumbnailFilePath " + thumbnailFilePath + ":", e);
      throw new PilotException("getObjectThumbnail IOException for thumbnailFilePath " + thumbnailFilePath + ":", e);
    } finally {
      try {
        if (stream != null)
          stream.close();
      } catch (IOException e) {
        logger.error("getObjectThumbnail IOException:" + e);
      }
    }
    return ret;
  }

  private Map<String, String> deleteObject(Object param) throws PilotException {
    return object.deleteObject(param);
  }

  private String createPublicLink(int validTime) throws PilotException {
    String pubLink = object.createObjectPublicLink(validTime);
    logger.debug(String.format("create pubLink = %s", pubLink));
    return pubLink;
  }

  @SuppressWarnings("unused")
  private String createPublicViewLink(String publicLink) throws PilotException {
    String pubViewLink = null;
    String tag = "tiny";
    StringBuilder bf = null;
    int index = 0;
    index = publicLink.indexOf(tag);
    bf = new StringBuilder();
    bf.append(publicLink.substring(0, index + tag.length()));
    bf.append("/view");
    bf.append(publicLink.substring(index + tag.length()));
    pubViewLink = bf.toString();
    logger.debug(String.format("create pubViewLink = %s", pubViewLink));
    return pubViewLink;
  }

  private String deletePublicLink(Object param) throws PilotException {
    String deletePubLink = null;
    deletePubLink = object.deleteObjectPublicLink(param);
    logger.debug(String.format("delete pubLink = %s", deletePubLink));
    return deletePubLink;
  }

  @SuppressWarnings("rawtypes")
  private Map batchPutObjectList(List<ResourceData> resourceList, long batchFileSize, CallbackData callback) throws PilotException {
    String resources = batchObjects.batchPutObjectList(resourceList, batchFileSize, callback, null);
    JSONObject json = JSONObject.fromObject(resources);
    return json;
  }

  private List<?> batchGetObjectList(long batchFileSize, String outputPath) throws PilotException {
    return batchObjects.batchGetObjectList(batchFileSize, outputPath, null, null);
  }

  private List<?> batchCreatePublicLink(int validTime) throws PilotException {
    String pubLinkList = batchObjects.batchCreateObjectPublicLink(validTime);
    JSONArray array = JSONArray.fromObject(pubLinkList);
    logger.debug(String.format("batch create pubLink number = %s", pubLinkList.length()));
    return array;
  }

  @SuppressWarnings("unused")
  private static String getMD5(InputStream fis) throws Exception {
    byte[] buffer = new byte[1024 * 32];
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    int numRead = 0;
    while ((numRead = fis.read(buffer)) > 0) {
      md5.update(buffer, 0, numRead);
    }
    fis.close();
    return toHexString(md5.digest(), "");
  }

  private static String toHexString(byte[] bytes, String separator) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : bytes) {
      hexString.append(Integer.toHexString(0xFF & b)).append(separator);
    }
    return hexString.toString();
  }

  public String getOssMessage() {
    return ossMessage;
  }

  // COS Progress Listener
  class OssCallbacks implements PilotOssListenerEx {

    private long startTime;
    private long endTime;

    @Override
    public void onFinished(long completed, long total, Object userdata) {
      if (useListener) {
        StringBuilder sb = new StringBuilder();
        sb.append("status=" + TaskStatus.TASK_COMPLETED + ";");
        sb.append("completed=" + completed + ";");
        sb.append("total=" + total + ";");
        endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        sb.append("duration=" + duration);
        ossMessage = sb.toString();
        logger.trace("COS Execution onFinished:" + ossMessage);
      }
    }

    @Override
    public boolean onProgress(TaskStatus status, long completed, long total, Object userdata, int errorcode) {
      boolean ret = false;
      if (useListener) {
        StringBuilder sb = new StringBuilder();
        sb.append("status=" + status + ";");
        sb.append("completed=" + completed + ";");
        sb.append("total=" + total + ";");
        sb.append("percent=" + MathsHelper.getPercent(completed, total) + "%;");
        sb.append("errorcode=" + errorcode);
        ossMessage = sb.toString();
        logger.trace("COS Execution onProgress:" + ossMessage);
      }
      if (completed == total) {
        ret = true;
      }
      return ret;
    }

    @Override
    public void onStart(Object userdata) {
      if (useListener) {
        String s = "status=" + TaskStatus.TASK_START;
        ossMessage = s;
        startTime = System.currentTimeMillis();
        logger.trace("COS Execution onStart:" + ossMessage);
      }
    }

    @Override
    public void onStop(long completed, long total, Object userdata, int errorcode) {
      if (useListener) {
        StringBuilder sb = new StringBuilder();
        sb.append("status=" + TaskStatus.TASK_STOP + ";");
        sb.append("completed=" + completed + ";");
        sb.append("total=" + total + ";");
        sb.append("errorcode=" + errorcode);
        ossMessage = sb.toString();
        logger.trace("COS Execution onStop:" + ossMessage);
      }
    }

  }
}
