package com.newroad.util.apiresult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * service api的返回结果值类
 * 
 */
public class ServiceResult<T> implements Serializable {
  
  private static final long serialVersionUID = 3795043653427820855L;

  public static final String KEY_BUSINESS_DATA = "data";

  private static final JSONObject EMPTY = new JSONObject();

  // default value
  private ReturnCode returnCode = ReturnCode.OK;
  private StringBuffer returnMessage = new StringBuffer();
  private T businessResult;
  private Long timeStamp;

  private String businessKey;

  public ServiceResult() {}

  /**
   * returnCode和returnMessage采用缺省的{@link ReturnCode#OK}
   * 
   * @param businessResult
   */
  public ServiceResult(T businessResult) {
    this.businessResult = businessResult;
  }

  public ServiceResult(String key, T value) {
    if (StringUtils.isBlank(key)) {
      this.businessKey = KEY_BUSINESS_DATA;
    } else {
      this.businessKey = key;
    }
    this.businessResult = value;
  }

  public ServiceResult(ReturnCode returnCode, StringBuffer returnMessage, T businessResult) {
    this.returnCode = returnCode;
    this.returnMessage = returnMessage;
    this.businessResult = businessResult;
  }

  public ServiceResult(String returnCode, StringBuffer returnMessage, T businessResult) {
    this.returnCode = ReturnCode.valueOf(returnCode);
    this.returnMessage = returnMessage;
    this.businessResult = businessResult;
  }

  @SuppressWarnings("rawtypes")
  public void copyCodeAndMessage(ServiceResult src) {
    if (src != null) {
      this.returnCode = src.gainReturnCode();
      this.returnMessage = src.gainReturnMessage();
      this.timeStamp = src.timeStamp;
    }
  }

  public ReturnCode gainReturnCode() {
    return returnCode;
  }

  public int getReturnCode() {
    return returnCode.getValue();
  }

  public void setReturnCode(ReturnCode returnCode) {
    this.returnCode = returnCode;
  }

  public StringBuffer gainReturnMessage() {
    return returnMessage;
  }

  public String getReturnMessage() {
    return returnMessage.toString();
  }

  public void setReturnMessage(StringBuffer returnMessage) {
    this.returnMessage = returnMessage;
  }
  
  public void setReturnMessage(String returnMessage) {
    this.returnMessage.append(returnMessage);
  }

  public T getBusinessResult() {
    return businessResult;
  }

  public void setBusinessResult(T businessResult) {
    this.businessResult = businessResult;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  public boolean checkOK() {
    if (this.returnCode == ReturnCode.OK)
      return true;
    return false;
  }

  public Long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(Long timeStamp) {
    this.timeStamp = timeStamp;
  }

  @SuppressWarnings("rawtypes")
  public static boolean isFullParam(Map param, ServiceResult res, String... args) {
    if (param == null) {
      res.setReturnCode(ReturnCode.BAD_REQUEST);
      res.gainReturnMessage().append(" param map is null.");
    } else {
      for (String arg : args) {
        Object value = param.get(arg);
        if (value == null || (value instanceof String && StringUtils.isBlank((String) value)) || JSONNull.getInstance().equals(value)) {
          res.setReturnCode(ReturnCode.BAD_REQUEST);
          res.gainReturnMessage().append(" No param [" + arg + "]!");
        }
      }
    }
    return res.checkOK();
  }

  /**
   * 转成json对象
   * 
   * @return
   */
  @SuppressWarnings("rawtypes")
  public JSONObject toJSONObject() {
    if (returnMessage == null) {
      returnMessage = new StringBuffer();
      returnMessage.append(returnCode.getReturnMessage());
    }
    JSONObject returnObj = ApiReturnObjectUtil.getReturnObj(returnCode, returnMessage.toString());
    returnObj.put("timeStamp", timeStamp);
    if (!StringUtils.isBlank(businessKey)) {
      returnObj.put(businessKey, businessResult);
    } else if (businessResult != null) {
      if (businessResult instanceof Map || businessResult instanceof JSONObject) {
        //returnObj.putAll((Map) businessResult);
        returnObj.put(KEY_BUSINESS_DATA,(Map) businessResult);
      } else if (businessResult instanceof String) {
        returnObj.put(KEY_BUSINESS_DATA, businessResult);
      } else if (businessResult instanceof List) {
        returnObj.put(KEY_BUSINESS_DATA, JSONArray.fromObject(businessResult));
      } else {
        try {
          if (EMPTY.equals(JSONObject.fromObject(businessResult))) {
            returnObj.put(KEY_BUSINESS_DATA, businessResult);
          } else {
            returnObj.putAll(JSONObject.fromObject(businessResult));
          }
        } catch (Exception e) {
          returnObj.put(KEY_BUSINESS_DATA, businessResult);
        }
      }
    }
    return returnObj;
  }

  public ServiceResult<T> to400(String returnMsg) {
    this.returnCode = ReturnCode.BAD_REQUEST;
    this.returnMessage.append(returnMsg);
    return this;
  }

  public ServiceResult<T> to402() {
    this.returnCode = ReturnCode.AUTH_BAD;
    this.returnMessage.append("auth failed.");
    return this;
  }

  public ServiceResult<T> to202() {
    this.returnCode = ReturnCode.SPACE_OVERFLOW;
    this.returnMessage.append("The user space is overflow.");
    return this;
  }

  /**
   * 返回json格式的数据, 通过this.{@link #toJSONObject()}.toString()实现
   * 
   */
  public String toString() {
    return toJSONObject().toString();
  }

}
