package com.newroad.user.sns.model.login;

import com.newroad.user.sns.model.user.User;
import com.newroad.util.apiresult.ReturnCode;

/**
 * @info : 登录上下文 (User Login Context)
 * @author: xiangping_yu
 * @data : 2013-10-30
 * @since : 1.5
 */
public class LoginContext {

  private String returnMsg;
  private ReturnCode returnCode = ReturnCode.OK;

  // LenovoID info
  private String lpsust;
  private String realm;
  private String cosConnector;
  private boolean skipCloudAccess = false;

  // Common user info
  private long userID;
  private String token;
  //LenovoAccountID,OpenID(user unique id)
  private Integer userAuthType;
  private String thirdPartyAccount;
  private String appUniqueID;
  private String userName;
  private String deviceID;
  private String verified;
  private String thirdName;
  private User user;

  public boolean isSuccess() {
    return ReturnCode.OK.equals(returnCode);
  }

  public String getReturnMsg() {
    return returnMsg;
  }

  public void setReturnMsg(String returnMsg) {
    this.returnMsg = returnMsg;
  }

  public ReturnCode getReturnCode() {
    return returnCode;
  }

  public void setReturnCode(ReturnCode returnCode) {
    this.returnCode = returnCode;
  }

  public String getLpsust() {
    return lpsust;
  }

  public void setLpsust(String lpsust) {
    this.lpsust = lpsust;
  }

  public String getRealm() {
    return realm;
  }

  public void setRealm(String realm) {
    this.realm = realm;
  }
  
  public Integer getUserAuthType() {
    return userAuthType;
  }

  public void setUserAuthType(Integer userAuthType) {
    this.userAuthType = userAuthType;
  }

  public String getThirdPartyAccount() {
    return thirdPartyAccount;
  }

  public void setThirdPartyAccount(String thirdPartyAccount) {
    this.thirdPartyAccount = thirdPartyAccount;
  }
  
  public String getAppUniqueID() {
    return appUniqueID;
  }

  public void setAppUniqueID(String appUniqueID) {
    this.appUniqueID = appUniqueID;
  }

  public long getUserID() {
    return userID;
  }

  public void setUserID(long userID) {
    this.userID = userID;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getDeviceID() {
    return deviceID;
  }

  public void setDeviceID(String deviceID) {
    this.deviceID = deviceID;
  }

  public String getVerified() {
    return verified;
  }

  public void setVerified(String verified) {
    this.verified = verified;
  }

  public String getThirdName() {
    return thirdName;
  }

  public void setThirdName(String thirdName) {
    this.thirdName = thirdName;
  }

  public String getCosConnector() {
    return cosConnector;
  }

  public void setCosConnector(String cosConnector) {
    this.cosConnector = cosConnector;
  }

  public boolean isSkipCloudAccess() {
    return skipCloudAccess;
  }

  public void setSkipCloudAccess(boolean skipCloudAccess) {
    this.skipCloudAccess = skipCloudAccess;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
  

  @Override
  public String toString() {
    return "LoginContext [userID=" + userID + ", token=" + token + ", userAuthType=" + userAuthType + ", thirdPartyAccount="
        + thirdPartyAccount + ", appUniqueID=" + appUniqueID + ", userName=" + userName + ", deviceID=" + deviceID + ", verified="
        + verified + ", thirdName=" + thirdName + ", user=" + user + "]";
  }
}
