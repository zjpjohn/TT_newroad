package com.newroad.tripmaster.constant;

import java.text.SimpleDateFormat;

/**
 * 系统常量
 *
 */
public interface SystemConstant {
  public static final String KEYS = "keys";
  public static final String CACHE = "cache";
  public static final String CLEAR = "clear";
  public static final String VALIDATE = "validate";
  public static final String BEHAVIOR = "behavior";
  public static final String ERROR = "error";
  public static final String LOG = "log";
  
  public static final String ID = "id";
  public static final String RULE = "rule";
  public static final String SOURCE_SCOPES = "sourceScopes";
  public static final String SOURCE_SCOPE = "sourceScope";
  public static final String VERSION_SCOPES = "versionScopes";
  public static final String VERSION_SCOPE = "versionScope";
  public static final String OPERATOR = "operator";
  public static final String VALUE = "value";
  public static final String COLUNM = "column";
  public static final String COLUNM_TYPE = "columnType";
  public static final String QUERIES = "queries";
  public static final String QUERY = "query";
  public static final String DATA_TYPE = "dataType";
  public static final String ANDROID_VERSION_SUPPORT_NEW_AUDIO_NOTE = "3.0.0";
  public static final String ANDROID_VERSION_SUPPORT_NEW_RECOGNITION_AUDIO_NOTE= "3.0.9";
  public static final String ANDROID_PHONE = "android-phone";
  public static final String LOGICAL_RULE = "logical-rule";
  
  public static final long SLEEP_MILLISECONDS = 300;
  
  public static final String QINIU_DOMAIN="http://7xjqgn.com1.z0.glb.clouddn.com/";
  
  public static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
  
  public static SimpleDateFormat travelDateFormat = new SimpleDateFormat("yyyy-MM-dd");
  
}
