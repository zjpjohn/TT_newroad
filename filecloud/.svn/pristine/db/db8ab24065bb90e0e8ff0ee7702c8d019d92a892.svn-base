package com.newroad.fileext.service.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import com.newroad.fileext.data.model.NoteFileData;
import com.newroad.fileext.service.mq.RabbitMQListener;
import com.newroad.fileext.service.mq.RabbitMQSender;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.mongodb.orm.db.MongoDaoIf;

/**
 * @author tangzj1
 * @version 2.0
 * @since Apr 24, 2014
 */
public class CloudCallbackHandler extends RabbitMQListener {

  private static Logger logger = LoggerFactory.getLogger(CloudCallbackHandler.class);

  private MongoDaoIf mongoDao;

  private RabbitMQSender messageSender;

  @Override
  public void onMessage(Message message) {
    String messageBody = new String(message.getBody());
    logger.info("Cloud Callback resource info during mq listening:" + messageBody);
    JSONObject messageJSON;
    try {
      messageJSON = JSONObject.fromObject(messageBody);
      JSONObject sessionUser = (JSONObject) messageJSON.get("sessionUser");
      JSONArray array = messageJSON.getJSONArray("resources");
      syncCloudResource(sessionUser, array);
    } catch (Exception e) {
      logger.error("CloudCallbackHandler onMessage Exception:", e);
    }
  }

  public List<String> syncCloudResource(JSONObject sessionUser, JSONArray array) {
    String userID = sessionUser.getString("superNoteUserID");
    List<String> resourceIDList = new ArrayList<String>(array.size());
    for (Object obj : array) {
      JSONObject json = (JSONObject) obj;
      String keyID = (String) json.get("key");
      if (keyID != null) {
        Map<String, Object> queryMap = new HashMap<String, Object>(2);
        queryMap.put("fileData.keyID", keyID);
        // queryMap.put(FileDataConstant.USER_ID, userID);
        NoteFileData fileResource = (NoteFileData) mongoDao.selectOne(FileDataConstant.SELECT_FILE_BY_KEYID, queryMap);
        if (fileResource == null) {
          logger.error("Couldn't find the specific file resource which the keyID is" + keyID + " when updating callback resource!");
          continue;
        }

        String resourceID = fileResource.getResourceID();
        Map<String, Object> resourceMap = new HashMap<String, Object>(3);
        resourceMap.put(FileDataConstant.CLOUD_SIZE, json.getLong("size"));
        resourceMap.put(FileDataConstant.CLOUD_STATUS, 1);
        resourceMap.put(FileDataConstant.LAST_UPDATE_TIME, json.getLong("lastUpdateTime"));
        int updateNum = mongoDao.update(FileDataConstant.SYNC_CLOUD_FILE_STATUS, queryMap, resourceMap);

        // Need to sync or not
        if (updateNum > 0) {
          // resourceIDList.add(resourceID);
          // syncResourceOpLog(userID, resourceID);
          logger.info("Sync Callback ResourceID=" + resourceID + " & key=" + keyID);
        } else {
          logger.error("Fail to update resource cloud status when resourceID=" + resourceID + "!");
        }

        // Need to update file cache exist or not
        // callCacheCallbackTaskMQ(sessionUser, fileResource);
      }
    }
    if (resourceIDList != null && resourceIDList.size() > 0 && userID != null) {
      logger.info("SyncCloudResource count:" + resourceIDList.size());
    }
    return resourceIDList;
  }

  public void callCacheCallbackTaskMQ(JSONObject sessionUser, NoteFileData fileResource) {
    JSONObject resource = new JSONObject();
    resource.put("sessionUser", sessionUser);
    resource.put("fileResource", fileResource);
    messageSender.sendMessage(resource.toString());
  }

  @SuppressWarnings("unused")
  private void syncResourceOpLog(String userID, String resourceID) {
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put("dataID", resourceID);
    queryMap.put("userID", userID);

    long now = System.currentTimeMillis();
    Map<String, Object> updateMap = new HashMap<String, Object>();
    updateMap.put("timeStamp", now);
    updateMap.put("lastUpdateTime", now);
    int returnNum = mongoDao.update("updateOpLog", "ln_oplog", queryMap, updateMap);
    if (returnNum == 0) {
      logger.error("Fail to update oplog by dataID:" + resourceID + ",dataType:4");
    }
  }

  public void setMongoDao(MongoDaoIf mongoDao) {
    this.mongoDao = mongoDao;
  }

  public void setMessageSender(RabbitMQSender messageSender) {
    this.messageSender = messageSender;
  }

}
