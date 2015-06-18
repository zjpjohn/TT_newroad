package com.newroad.fileext.service.cloud;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.newroad.cache.common.couchbase.CouchbaseCache;
import com.newroad.fileext.dao.CloudFileExecutionCode;
import com.newroad.fileext.dao.cos.COSDao;
import com.newroad.fileext.dao.cos.COSExecutionTask;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.NoteFileData;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.fileext.utilities.SystemProperties;
import com.newroad.mongodb.orm.db.MongoDaoIf;

@Service
public class CloudManageService {

  private static Logger logger = LoggerFactory.getLogger(CloudManageService.class);

  private CouchbaseCache newroadCache;

  private MongoDaoIf mongoDao;

  private COSDao cosDao;

  private static ExecutorService checkExecutor = Executors.newCachedThreadPool();

  /**
   * 获得cos connector
   */
  public String getConnector(String realm, String lenovoST) throws FileResourceException {
    logger.debug("getConnector LenovoID=" + realm + ":" + lenovoST);
    String connector = null;
    if ("".equals(realm) && "".equals(lenovoST)) {
      connector = "";
    } else {
      connector = cosDao.getConnectorByLenovoID(realm, lenovoST);
    }
    if (connector == null || "".equals(connector)) {
      return null;
    }
    JSONObject connectorJSON = new JSONObject();
    connectorJSON.put(FileDataConstant.COS_CONNECTOR, connector);
    return connectorJSON.toString();
  }

  public String getConnectorByUser(String userName, String password) {
    return cosDao.getConnectorByUser(userName, password);
  }

  public JSONObject getCloudToken(JSONObject sessionUser) throws FileResourceException {
    String account = sessionUser.getString("account");
    logger.debug("getCloudToken user accountId:" + account);
    Serializable token = newroadCache.get(account);
    // The unit of expire time is second.
    long expire = 3600 * 2;
    long tokenExpire = System.currentTimeMillis() / 1000L + 3600 * 24;
    if (token == null) {
      token = cosDao.getCOSToken(account, tokenExpire);
      newroadCache.set(account, token, expire,false);
    } else {
      newroadCache.touch(account, expire);
    }

    JSONObject tokenInfo = new JSONObject();
    tokenInfo.put("token", token);
    tokenInfo.put("expire", expire);
    return tokenInfo;
  }


  public int listObjectNumber(JSONObject sessionUser) throws FileResourceException {
    String connector = getConnectorByUser(SystemProperties.cloudUser, SystemProperties.cloudPassword);
    if (connector == null) {
      logger.error("Fail to get COS connector during COS monitor!");
      throw new FileResourceException("Fail to get COS connector during COS monitor!");
    }
    sessionUser.put(FileDataConstant.COS_CONNECTOR, connector);
    return cosDao.listObjectNumber(sessionUser);
  }

  @SuppressWarnings("unchecked")
  public void rectifyCloudResource() {
    File checkCloudFile = new File(SystemProperties.cloudRecifyMonitorLogPath);
    BufferedWriter bw = null;
    try {
      if (!checkCloudFile.exists()) {
        checkCloudFile.createNewFile();
      }
      bw = new BufferedWriter(new FileWriter(checkCloudFile, true));
      Map<String, Object> map = new HashMap<String, Object>(2);
      map.put("fileData.cloudStatus", 0);
      List<NoteFileData> localFileList = (List<NoteFileData>) mongoDao.selectList(FileDataConstant.SELECT_FILES_BY_CLOUD_STATUS, map, 0, 0);
      int localFileNum = localFileList.size();
      if (localFileNum > 0) {
        for (int i = 0; i < localFileNum; i++) {
          NoteFileData localFileResource = (NoteFileData) localFileList.get(i);
          CloudFileData localFile = localFileResource.getFileData();
          // Couldn't get the useful token.
          String token = localFile.getToken();
          if (token != null) {
            COSExecutionTask task = new COSExecutionTask(CloudFileExecutionCode.GET_OBJECT_INFO, token, localFile);
            CloudFileData cloudFile = (CloudFileData) checkExecutor.submit(task);
            writeOutputFile(bw, localFile, cloudFile);
          }
        }
      }
    } catch (IOException e) {
      logger.error("rectifyCloudResource IOException:", e);
    } finally {
      try {
        if (bw != null)
          bw.close();
      } catch (IOException e) {
        logger.error("rectifyCloudResource BufferedWriter IOException:", e);
      }
    }
  }

  private void writeOutputFile(BufferedWriter bw, CloudFileData localFile, CloudFileData cloudFile) throws IOException {
    long localSize = localFile.getCloudSize();
    long cloudSize = cloudFile.getCloudSize();
    String keyID = localFile.getKeyId();
    if (localSize != cloudSize) {
      bw.write(keyID + "," + localSize + "," + cloudSize);
      bw.newLine();
      bw.flush();
    }
  }

  public void setMongoDao(MongoDaoIf mongoDao) {
    this.mongoDao = mongoDao;
  }

  public void setNewroadCache(CouchbaseCache newroadCache) {
    this.newroadCache = newroadCache;
  }

  public void setCosDao(COSDao cosDao) {
    this.cosDao = cosDao;
  }

}
