package com.newroad.cos.pilot;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cos.pilot.util.CallbackData;
import com.newroad.cos.pilot.util.PilotOssConstants;
import com.newroad.cos.pilot.util.ResourceData;

/**
 * @info
 * @author tangzj1
 * @date Nov 2, 2013
 * @version
 */
public class PilotOssObjectListForLeNote extends PilotOssObjectForLeNote implements PilotOssObjectListEx, OssManagerListener {

  private final Logger logger = LoggerFactory.getLogger(PilotOssObjectListForLeNote.class);

  private List<String> keyList;

  // private long batchUploadSizeLimit = 1048576l;

  protected PilotOssObjectListForLeNote(OssManager ossMgr, String bucketName, String... keys) {
    super(ossMgr, bucketName, keys);
  }

  @Override
  public String batchPutObjectList(List<ResourceData> resourceList, long batchFileSize, Object parameterData, Object userData)
      throws PilotException {
    if (resourceList == null || resourceList.size() <= 0) {
      throw new PilotException("batchPutObjectList: No specific key list exists!");
    }
    if (batchFileSize == 0) {
      throw new PilotException("batchPutObjectList: The size of batch file size is zero for resource list " + resourceList);
    }
    ProgressData progress = new ProgressData(batchFileSize, userData);
    synchronized (this) {
      isContinue = true;
    }

    CallbackData cbData = null;
    if (parameterData != null) {
      cbData = (CallbackData) parameterData;
    } else {
      cbData = new CallbackData();
    }

    String keys = null;
    try {
      JSONObject fileObjects = new JSONObject();
      JSONArray array = new JSONArray();
      for (ResourceData resource : resourceList) {
        JSONObject json = new JSONObject();
        json.put(PilotOssConstants.OBJECT_KEY, resource.getKey());
        json.put(PilotOssConstants.TEMP_FILE_PATH, resource.getPath());
        json.put(PilotOssConstants.CONTENT_TYPE, resource.getContentType());
        array.put(json);
      }
      fileObjects.put(PilotOssConstants.FILE_LIST, array);
      keys = fileObjects.toString();
      byte[] keysByte = keys.getBytes();

      notifyStart(userData);
      String objects =
          ossMgr.batchPutObjectList(bucketName, new ByteArrayInputStream(keysByte), batchFileSize, this, null, cbData, progress);
      notifyClient(TaskStatus.TASK_COMPLETED, batchFileSize, batchFileSize, userData, 0);
      return objects;
    } catch (Exception e) {
      isContinue = false;
      notifyStop(progress.completed, progress.total, userData, 1);
      //logger.error("batchPutObjectList files Exception for object keys " + keys + ":", e);
      PilotException err = new PilotException("batchPutObjectList files Exception for object keys " + keys + ":", e);
      throw err;
    }
  }

  @Override
  public List<Map<String, Object>> batchGetObjectList(long batchFileSize, String outputFolderPath, Object parameterData, Object userData)
      throws PilotException {
    if (keys == null || keys.length <= 0) {
      throw new PilotException("No specific keys exists!");
    }
    keyList = Arrays.asList(keys);
    if (batchFileSize == 0) {
      throw new PilotException("The size of batch file size is zero for object keys " + keyList + "!");
    }

    ProgressData progress = new ProgressData(batchFileSize, userData);
    synchronized (this) {
      isContinue = true;
    }

    CallbackData cbData = null;
    if (parameterData != null) {
      cbData = (CallbackData) parameterData;
    } else {
      cbData = new CallbackData();
    }
    String keys = null;
    try {
      JSONObject objkeys = new JSONObject();
      JSONArray array = new JSONArray(keyList);
      objkeys.put(PilotOssConstants.OBJECT_KEYS, array);
      keys = objkeys.toString();
      byte[] keysByte = keys.getBytes();

      Map<String, String> data = new HashMap<String, String>();
      data.put(PilotOssConstants.TEMP_STORAGE, outputFolderPath);
      cbData.setData(data);

      notifyStart(userData);
      List<Map<String, Object>> objectList =
          ossMgr.batchGetObjectList(bucketName, new ByteArrayInputStream(keysByte), keysByte.length, batchFileSize, this, null, cbData,
              progress);
      notifyClient(TaskStatus.TASK_COMPLETED, batchFileSize, batchFileSize, userData, 0);
      return objectList;
    } catch (Exception e) {
      isContinue = false;
      notifyStop(progress.completed, progress.total, userData, 1);
      //logger.error("batchGetObjectList files Exception for object keys " + keys + ":", e);
      PilotException err = new PilotException("batchGetObjectList files Exception for object keys " + keys + ":", e);
      throw err;
    }
  }

  @Override
  public String batchCreateObjectPublicLink(int validTime) throws PilotException {
    if (keys == null || keys.length <= 0) {
      throw new PilotException("No specific keys exists!");
    }
    keyList = Arrays.asList(keys);
    String objKeyStr = null;
    try {
      JSONObject objkeys = new JSONObject();
      JSONArray array = new JSONArray(keyList);
      objkeys.put(PilotOssConstants.OBJECT_KEYS, array);
      objkeys.put(PilotOssConstants.VALID_TIME, validTime);
      objKeyStr = objkeys.toString();
      byte[] keysByte = objKeyStr.getBytes();
      return ossMgr.batchCreateObjectPublicLink(bucketName, new ByteArrayInputStream(keysByte), keysByte.length, null, null);
    } catch (Exception e) {
      //logger.error("batchCreateObjectPublicLink Exception for object keys " + objKeyStr + ":", e);
      PilotException err = new PilotException("batchCreateObjectPublicLink Exception for object keys " + objKeyStr + ":", e);
      throw err;
    }
  }

}
