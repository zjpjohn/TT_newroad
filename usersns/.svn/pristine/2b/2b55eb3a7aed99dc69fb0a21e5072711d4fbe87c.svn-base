package com.newroad.user.sns.service.application;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.user.sns.model.login.LoginContext;
import com.newroad.user.sns.model.login.LoginResponse;
import com.newroad.user.sns.model.login.LoginResponse.LoginData;
import com.newroad.user.sns.model.login.LoginResponse.LoginInfo;
import com.newroad.user.sns.model.login.LoginUser;
import com.newroad.user.sns.model.user.User;
import com.newroad.user.sns.util.ControllerUtils;
import com.newroad.user.sns.util.HttpUtil;
import com.newroad.user.sns.util.HttpUtil.Method;
import com.newroad.util.auth.TokenUtil;


/**
 * @info : supernote 应用
 * @author: xiangping_yu
 * @data : 2013-11-4
 * @since : 1.5
 */
public class SuperNoteApplication implements Application {

  private static final Logger log = LoggerFactory.getLogger(SuperNoteApplication.class);

  private static final String LOAD = "/user/load";
  private static final String USERINFO = "/user/info";
  private static final String UNLOAD = "/user/unload";

  private static final String DEF_VERSION = "v1";

  private String host = "http://supernote.lenovomm.com/supernote/";

  private String app;

  @Override
  public LoginResponse login(User user, LoginContext context, String apiVersion) {
    try {
      JSONObject param = new JSONObject();
      param.put("app", app);

      String result =
          HttpUtil.httpCall(Method.post, host + "v" + apiVersion + LOAD, getHttpHeaders(context), new StringBuffer(param.toString()), null)
              .toString();

      log.debug("load url:" + host + "v" + apiVersion + LOAD + ", result is " + result);

      if (StringUtils.isBlank(result)) {
        throw new RuntimeException("User login, call [supernote] 'load' error. Because the result is blank.");
      }

      return getLoginInfo(user, context, JSONObject.fromObject(result));
    } catch (Exception e) {
      log.error("user login, application [supernote] user init info error:", e);
      return getLoginErrorInfo(user, context);
    }
  }

  @Override
  public Object getUserInfo(User user, LoginUser current, String apiVersion) {
    try {
      String result =
          HttpUtil.httpCall(Method.post, host + "v" + apiVersion + USERINFO, ControllerUtils.getHttpHeaders(), null, null).toString();

      if (StringUtils.isBlank(result)) {
        throw new RuntimeException("User info, call [supernote] 'info' error. Because the result is blank.");
      }

      return getUserInfo(user, current, JSONObject.fromObject(result));
    } catch (Exception e) {
      log.error("user info, get application [supernote] user info error:", e);
      return getErrorUserInfo(user, current);
    }
  }

  @Override
  public Object logout(LoginUser user, String apiVersion) {
    try {
      String result =
          HttpUtil.httpCall(Method.post, host + "v" + apiVersion + DEF_VERSION + UNLOAD, ControllerUtils.getHttpHeaders(), null, null)
              .toString();

      if (StringUtils.isBlank(result)) {
        throw new RuntimeException("User logout, call [supernote] 'unload' error. Because the result is blank.");
      }

      return JSONObject.fromObject(result);
    } catch (Exception e) {
      log.error("user logout, application [supernote] user logout error:", e);
      return new JSONObject().put("loginStatus", -1);
    }
  }

  private LoginResponse getLoginInfo(User user, LoginContext context, JSONObject data) {
    SuperNoteLoginInfo info = new SuperNoteLoginInfo();
    info.setLoginStatus(1);
    info.setLenovoAccountID(context.getThirdPartyAccount());
    info.setUserID(user.getUserID());
    info.setUserName(context.getUserName());
    info.setOpenAuthName(context.getThirdName());
    info.setRootCategoryID((String) data.get("rootCategoryID"));
    info.setDefaultCategoryID((String) data.get("defaultCategoryID"));
    info.setUid((String) data.get("uid"));
    info.setOplogTable((String) data.get("oplogTable"));
    info.setLeNoteToken(context.getToken());
    info.setCosConnector(context.getCosConnector());
    info.setDefData(data.getJSONObject("defaultData"));

    SuperNoteLoginData ldata = new SuperNoteLoginData();
    ldata.setSuperNoteUserID((String) data.get("uid"));
    ldata.setUid((String) data.get("uid"));
    ldata.setRootCategoryID((String) data.get("rootCategoryID"));
    ldata.setDefaultCategoryID((String) data.get("defaultCategoryID"));
    ldata.setOplogTable((String) data.get("oplogTable"));

    return LoginResponse.fromSuccess(info, ldata);
  }

  private LoginResponse getLoginErrorInfo(User user, LoginContext context) {
    SuperNoteLoginInfo info = new SuperNoteLoginInfo();
    info.setLoginStatus(-1);
    info.setLenovoAccountID(context.getThirdPartyAccount());
    info.setUserID(user.getUserID());
    info.setUserName(context.getUserName());
    info.setOpenAuthName(context.getThirdName());
    info.setLeNoteToken(context.getToken());
    info.setCosConnector(context.getCosConnector());

    return LoginResponse.fromError(info);
  }

  private JSONObject getUserInfo(User user, LoginUser current, JSONObject data) {
    JSONObject result = new JSONObject();
    result.put("loginStatus", 1);
    result.put(LoginUser.USERID, user.getUserID());
    result.put("userToken", current.getToken());
    // result.put("cosConnector", current.getCosConnector());
    // result.put("lpsust", current.getLpsust());
    // result.put("realm", current.getRealm());
    result.put("account", current.getAccount());
    result.put("userType", user.getUserType());
    result.put("userName", user.getLoginName());
    result.put("nickName", user.getNickName());
    result.put("shortID", user.getShortID());
    result.put("lastLoginTime", user.getLastLoginTime().getTime());
    result.put("lastOperateTime", user.getLastOperateTime().getTime());
    result.put("totalSpace", data.get("totalSpace"));
    result.put("usedSpace", data.get("usedSpace"));
    result.put("defaultCategoryID", data.get("defaultCategoryID"));
    result.put("rootCategoryID", data.get("rootCategoryID"));
    result.put("noteCount", data.get("noteCount"));
    result.put("markedCount", data.get("markedCount"));
    result.put("recycleCount", data.get("recycleCount"));
    result.put("sharedCount", data.get("sharedCount"));
    result.put("oplogTable", (String) data.get("oplogTable"));
    result.put("superNoteUserID", data.get("userID"));
    return result;
  }

  private JSONObject getErrorUserInfo(User user, LoginUser current) {
    JSONObject result = new JSONObject();
    result.put("status", 0);
    result.put(LoginUser.USERID, user.getUserID());
    result.put("userType", user.getUserType());
    result.put("userName", user.getLoginName());
    result.put("nickName", user.getNickName());
    result.put("shortID", user.getShortID());
    result.put("lastLoginTime", user.getLastLoginTime().getTime());
    result.put("lastOperateTime", user.getLastOperateTime().getTime());
    result.put("userToken", current.getToken());
    return result;
  }

  private Map<String, String> getHttpHeaders(LoginContext context) {
    Map<String, String> headers = ControllerUtils.getHttpHeaders();
    headers.put(TokenUtil.AUTH_TOKEN, context.getToken());
    return headers;
  }

  public static class SuperNoteLoginInfo implements LoginInfo {
    private Integer loginStatus;
    private String lenovoAccountID;
    private String userName;
    private String openAuthName;
    private String rootCategoryID;
    private String defaultCategoryID;
    private String uid;
    private String oplogTable;
    private Long userID;
    private String leNoteToken;
    private String cosConnector;
    private JSONObject defData;

    public Integer getLoginStatus() {
      return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
      this.loginStatus = loginStatus;
    }

    public String getLenovoAccountID() {
      return lenovoAccountID;
    }

    public void setLenovoAccountID(String lenovoAccountID) {
      this.lenovoAccountID = lenovoAccountID;
    }

    public String getUserName() {
      return userName;
    }

    public void setUserName(String userName) {
      this.userName = userName;
    }

    public String getOpenAuthName() {
      return openAuthName;
    }

    public void setOpenAuthName(String openAuthName) {
      this.openAuthName = openAuthName;
    }

    public String getRootCategoryID() {
      return rootCategoryID;
    }

    public void setRootCategoryID(String rootCategoryID) {
      this.rootCategoryID = rootCategoryID;
    }

    public String getDefaultCategoryID() {
      return defaultCategoryID;
    }

    public void setDefaultCategoryID(String defaultCategoryID) {
      this.defaultCategoryID = defaultCategoryID;
    }

    public String getUid() {
      return uid;
    }

    public void setUid(String uid) {
      this.uid = uid;
    }

    public String getOplogTable() {
      return oplogTable;
    }

    public void setOplogTable(String oplogTable) {
      this.oplogTable = oplogTable;
    }

    public Long getUserID() {
      return userID;
    }

    public void setUserID(Long userID) {
      this.userID = userID;
    }

    public String getLeNoteToken() {
      return leNoteToken;
    }

    public void setLeNoteToken(String leNoteToken) {
      this.leNoteToken = leNoteToken;
    }

    public String getCosConnector() {
      return cosConnector;
    }

    public void setCosConnector(String cosConnector) {
      this.cosConnector = cosConnector;
    }

    public JSONObject getDefData() {
      return defData;
    }

    public void setDefData(JSONObject defData) {
      this.defData = defData;
    }
  }

  public static class SuperNoteLoginData implements LoginData {
    private static final long serialVersionUID = -8176372680672019912L;
    private String rootCategoryID;
    private String defaultCategoryID;
    private String uid;
    private String superNoteUserID;
    private String oplogTable;

    public String getRootCategoryID() {
      return rootCategoryID;
    }

    public void setRootCategoryID(String rootCategoryID) {
      this.rootCategoryID = rootCategoryID;
    }

    public String getDefaultCategoryID() {
      return defaultCategoryID;
    }

    public void setDefaultCategoryID(String defaultCategoryID) {
      this.defaultCategoryID = defaultCategoryID;
    }

    public String getSuperNoteUserID() {
      return superNoteUserID;
    }

    public void setSuperNoteUserID(String superNoteUserID) {
      this.superNoteUserID = superNoteUserID;
    }

    public String getUid() {
      return uid;
    }

    public void setUid(String uid) {
      this.uid = uid;
    }

    public String getOplogTable() {
      return oplogTable;
    }

    public void setOplogTable(String oplogTable) {
      this.oplogTable = oplogTable;
    }
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setApp(String app) {
    this.app = app;
  }
}
