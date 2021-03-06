package com.newroad.user.sns.service;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cache.common.Cache;
import com.newroad.cache.common.basic.SimpleCache;
import com.newroad.user.sns.model.login.LoginResponse.LoginData;
import com.newroad.user.sns.model.login.LoginUser;
import com.newroad.user.sns.service.message.sms.MessageConstant;
import com.newroad.user.sns.service.message.sms.VerificationCode;

/**
 * @info : 会话服务、可供多服务器共享会话信息
 * @author: xiangping_yu
 * @data : 2013-11-8
 * @since : 1.5
 */
public class SessionService {

  private static final Logger log = LoggerFactory.getLogger(SessionService.class);

  private static final String PREFIX = "sns.session.";
  private static final String LOGINPREFIX = "sns.login.user.";
  private static final String LOGINDATAPREFIX = "sns.login.user.data.";

  private int sessionTimeOutMinutes = 60 * 24; // 一天
  private int loginTimeOutMinutes = 60 * 24 * 15; // 半个月

  private Cache<String, Serializable> snsCache;

  /**
   * 获取会话值
   */
  public Object getSession(String key) {
    touch(key);
    return snsCache.get(PREFIX + key);
  }

  /**
   * 设置会话值
   */
  public boolean setSession(String key, Serializable value) {
    String _key = PREFIX + key;
    int time = getTimeOut();
    // return snsCache.set(_key, value, time, true);
    return true;
  }

  /**
   * 清除会话值
   */
  public boolean clearSession(String key) {
    log.info("clear session value, key=" + key);
    return snsCache.delete(PREFIX + key);
  }

  /**
   * 取系统登录用户
   */
  public LoginUser getLoginUser(String token) {
    return (LoginUser) snsCache.get(LOGINPREFIX + token);
  }

  public boolean setLoginUser(String token, LoginUser user) {
    String key = LOGINPREFIX + token;
    return snsCache.set(key, user);
    // return true;
  }

  public boolean setLoginUser(String token, LoginUser user, int minutes) {
    String key = LOGINPREFIX + token;
    int time = getLoginTimeOut(minutes);
    return snsCache.set(key, user, time);
    // return true;
  }

  public VerificationCode getVerificationCode(String phone) {
    return (VerificationCode) snsCache.get(MessageConstant.VERIFICATION_CODE_CACHE_KEY + phone);
  }

  public boolean setVerificationCode(String phone, VerificationCode code) {
    String key = MessageConstant.VERIFICATION_CODE_CACHE_KEY + phone;
    // return snsCache.set(key, code, true);
    return true;
  }

  public boolean setVerificationCode(String phone, VerificationCode code, int minutes) {
    String key = MessageConstant.VERIFICATION_CODE_CACHE_KEY + phone;
    int time = getLoginTimeOut(minutes);
    // return snsCache.set(key, code, time, true);
    return true;
  }

  public boolean clearLoginUser(String token) {
    String key = LOGINPREFIX + token;
    return snsCache.delete(key);
  }

  public LoginData getLoginUserApplicationData(String app, String token) {
    String key = LOGINDATAPREFIX + app + "." + token;
    return (LoginData) snsCache.get(key);
  }

  public boolean setLoginUserApplicationData(String app, String token, LoginData data) {
    String key = LOGINDATAPREFIX + app + "." + token;
    // return snsCache.set(key, data, true);
    return true;
  }

  public boolean clearLoginUserApplicationData(String app, String token) {
    String key = LOGINDATAPREFIX + app + "." + token;
    return snsCache.delete(key);
  }

  public boolean isAvailable() {
    // return snsCache.isAvailable();
    return true;
  }

  /**
   * 触发缓存,延长session时效
   */
  private void touch(String key) {
    String _key = PREFIX + key;
    int time = getTimeOut();
    //snsCache.touch(_key, time);
  }

  /**
   * 触发缓存,延长session时效
   */
  @SuppressWarnings("unused")
  private void touchUser(String token) {
    String _key = LOGINPREFIX + token;
    int time = getLoginTimeOut();
    // snsCache.touch(_key, time);
  }

  private int getTimeOut() {
    return (int) System.currentTimeMillis() / 1000 + (sessionTimeOutMinutes * 60);
  }

  private int getLoginTimeOut() {
    return (int) System.currentTimeMillis() / 1000 + (loginTimeOutMinutes * 60);
  }

  private int getLoginTimeOut(int time) {
    return (int) System.currentTimeMillis() / 1000 + (time * 60);
  }

  public void setSnsCache(SimpleCache snsCache) {
    this.snsCache = snsCache;
  }

  public void setSessionTimeOutMinutes(int sessionTimeOutMinutes) {
    this.sessionTimeOutMinutes = sessionTimeOutMinutes;
  }


  public void setLoginTimeOutMinutes(int loginTimeOutMinutes) {
    this.loginTimeOutMinutes = loginTimeOutMinutes;
  }
}
