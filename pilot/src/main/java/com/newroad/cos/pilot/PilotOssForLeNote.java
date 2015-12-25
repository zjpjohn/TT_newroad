package com.newroad.cos.pilot;

import java.io.IOException;
import java.util.ArrayList;
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

public class PilotOssForLeNote implements PilotOssEx {

  private final Logger logger = LoggerFactory.getLogger(PilotOssForLeNote.class);

  private OssManager ossMgr;
  private boolean loginSuccessed = false;

  protected PilotOssForLeNote() {
    ossMgr = new OssManager();
  }

  public boolean login(String appID, String devKeyID, String devKey, String userName, String password, String spec, String workspace)
      throws PilotException {
    // if (loginSuccessed) {
    // return;
    // }
    try {
      ossMgr.loginByNormal(userName, password, devKeyID, devKey, appID, spec, workspace);
      this.loginSuccessed = true;
      return isLoginSuccessed();
    } catch (IOException e) {
      this.loginSuccessed = false;
      logger.error("login IOException for userName " + userName + ":", e);
      PilotException err = new PilotException("login IOException for userName " + userName + ":", e);
      throw err;
    }

  }

  public boolean loginByLenovoID(String appID, String devKeyID, String devKey, String realm, String lenovoST, String spec, String workspace)
      throws PilotException {
    // if (loginSuccessed) {
    // return;
    // }
    try {
      ossMgr.loginByLenovoID(realm, lenovoST, devKeyID, devKey, appID, spec, workspace);
      this.loginSuccessed = true;
      return isLoginSuccessed();
    } catch (IOException e) {
      this.loginSuccessed = false;
      logger.error("loginByLenovoID IOException for lenovoST " + lenovoST + ":", e);
      PilotException err = new PilotException("loginByLenovoID IOException for lenovoST " + lenovoST + ":", e);
      throw err;
    }
  }

  public String getConnector() {
    return ossMgr.getConnector();
  }

  public boolean loginByConnector(String devID, String devKey, String connector) throws PilotException {
    try {
      ossMgr.loginByConnector(devID, devKey, connector);
      this.loginSuccessed = true;
      return isLoginSuccessed();
    } catch (IOException e) {
      this.loginSuccessed = false;
      logger.error("loginByConnector IOException for connector " + connector + ":", e);
      throw new PilotException("loginByConnector IOException for connector " + connector + ":", e);
    }
  }

  public String generateToken(String appID, String devID, String devKey, String userSlug, long expiration) throws PilotException {
    String token = null;
    try {
      token = ossMgr.generateToken(appID, devID, devKey, userSlug, expiration);
      if (token != null) {
        this.loginSuccessed = true;
      }
      return token;
    } catch (IOException e) {
      this.loginSuccessed = false;
      logger.error("generateToken IOException for token " + token + ":", e);
      throw new PilotException("generateToken IOException for token " + token + ":", e);
    }
  }

  public void useToken(String token) throws IOException {
    try {
      ossMgr.useToken(token);
      this.loginSuccessed = true;
    } catch (IOException e) {
      this.loginSuccessed = false;
      logger.error("useToken IOException for token " + token + ":", e);
      throw new IOException("useToken IOException for token " + token + ":", e);
    }
  }

  public PilotOssObjectBaseEx getOssObject(String bucketName, String keyID) throws PilotException {
    if (!loginSuccessed) {
      logger.error("Don't login to oss using object key " + keyID + "!");
      throw new PilotException("Don't login to oss using object key " + keyID + "!");
    }

    PilotOssObjectBaseEx obj = new PilotOssObjectForLeNote(ossMgr, bucketName, keyID);
    Map<String, String> ret;
    try {
      ret = obj.getObjectInfo();
      if (ret != null && ret.get("location") != null) {
        return obj;
      } else {
        throw new PilotException("object key " + keyID + " isn't exist!");
      }
    } catch (Exception e) {
      logger.error("getOssObject Exception for object key " + keyID + ":", e);
      PilotException err = new PilotException("getOssObject Exception for object key " + keyID + ":", e);
      throw err;
    }
  }

  public PilotOssObjectBaseEx getOssObject(String url) throws PilotException {
    if (!loginSuccessed) {
      logger.error("Don't login to oss using object url " + url + "!");
      throw new PilotException("Don't login to oss using object url " + url + "!");
    }

    PilotOssObjectBaseEx obj = new PilotOssObjectForLeNote(ossMgr, url);
    Map<String, String> ret;
    try {
      ret = obj.getObjectInfo();
      if (ret != null && ret.get("location") != null) {
        String bucketName = ret.get("bucket_name");
        String keyID = ret.get("key");
        obj = new PilotOssObjectForLeNote(ossMgr, bucketName, keyID);
        return obj;
      } else {
        throw new PilotException("object url " + url + " isn't exist");
      }
    } catch (Exception e) {
      logger.error("getOssObject Exception for object url " + url + ":", e);
      PilotException err = new PilotException("getOssObject Exception for object url " + url + ":", e);
      throw err;
    }
  }

  public PilotOssObjectListEx getOssObjectList(String bucketName, String... keys) throws PilotException {
    if (!loginSuccessed) {
      throw new PilotException("Don't login to oss using object keys "+Arrays.toString(keys)+"!");
    }
    PilotOssObjectListEx objList = new PilotOssObjectListForLeNote(ossMgr, bucketName, keys);
    return objList;
  }

  /*
   * if keyID = null, uses url create object
   */
  public PilotOssObjectBaseEx createObject(String bucketName, String keyID, Object param) throws PilotException {
    if (!loginSuccessed) {
      throw new PilotException("need to login using object key " + keyID + "!");
    }
    CallbackData callbackData = null;
    if (param != null) {
      callbackData = (CallbackData) param;
    }
    try {
      Map<String, String> ret = ossMgr.createObject(bucketName, keyID, callbackData);
      if (ret != null && ret.get("key") != null && ret.get("location") != null) {
        return new PilotOssObjectForLeNote(ossMgr, bucketName, ret.get("key"));
      } else {
        throw new PilotException("can't create object using object key " + keyID);
      }
    } catch (Exception e) {
      logger.error("createObject Exception for object key " + keyID + ":", e);
      PilotException err = new PilotException("createObject Exception for object key " + keyID + ":", e);
      throw err;
    }
  }

  /**
   * 在OSS服务器上批量创建对象
   * 
   * @param bucketName OSS服务器上已有的bucket name
   * @param keyID 对象的唯一标识，可以为null
   * @param param 参数，对于lesync为CallbackData类
   * @return 返回PilotOssObjectBase接口
   * @see PilotOssObjectBase
   */
  public PilotOssObjectListEx batchCreateObjects(String bucketName, int objNumber, Object parameterData) throws PilotException {
    CallbackData cbData = null;
    if (parameterData != null) {
      cbData = (CallbackData) parameterData;
    } else {
      cbData = new CallbackData();
    }
    String[] keys = null;
    try {
      Map<String, String> param = new HashMap<String, String>();
      param.put("object_count", String.valueOf(objNumber));
      List<String> keyList = ossMgr.batchCreateObjects(bucketName, param, cbData);
      String[] strings = new String[keyList.size()];
      keys = keyList.toArray(strings);
      return new PilotOssObjectListForLeNote(ossMgr, bucketName, keys);
    } catch (IOException e) {
      logger.error("batchCreateObjects IOException for object keys " + Arrays.toString(keys) + ":", e);
      PilotException err = new PilotException("batchCreateObjects IOException for object keys " + Arrays.toString(keys) + ":", e);
      throw err;
    }
  }

  public List<PilotOssObjectBaseEx> listOssObject(String bucketID, long offset, long length) throws PilotException {
    List<PilotOssObjectBaseEx> list = new ArrayList<PilotOssObjectBaseEx>();
    Map<String, String> param = new HashMap<String, String>();
    param.put(PilotOssConstants.OFFSET, String.valueOf(offset));
    param.put(PilotOssConstants.LIMIT, String.valueOf(length));

    JSONArray jsonarray = null;
    try {
      jsonarray = ossMgr.ListObjects(bucketID, param);
      int count = jsonarray.length();
      if (count == 0) {
        return null;
      }
      for (int i = 0; i < count; i++) {
        JSONObject item = jsonarray.getJSONObject(i);
        String bucketname = item.getString("bucket_name");
        String key = item.getString("key");
        // String location = item.getString("location");
        PilotOssObjectBaseEx object = new PilotOssObjectForLeNote(ossMgr, bucketname, key);
        list.add(object);
      }
      return list;
    } catch (Exception e) {
      logger.error("listOssObject IOException for object array " + jsonarray + ":", e);
      PilotException err = new PilotException("listOssObject IOException for object array " + jsonarray + ":", e);
      throw err;
    }
  }

  @Override
  public Map<String, Object> getBucketInfo(String url) throws PilotException {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isLoginSuccessed() {
    return this.loginSuccessed;
  }

}
