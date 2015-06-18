package com.newroad.util.apiresult;


import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

/**
 * 对于业务接口返回的数据本身的工具类
 * 
 */
public class ApiReturnObjectUtil {

  public static final String KEY_RETURN_CODE = "code";
  public static final String KEY_RETURN_MESSAGE = "message";

  private ApiReturnObjectUtil() {};

  /**
   * 根据指定异常对象,返回一个包含返回码,异常信息的JSONObject对象
   * 
   * @param e
   * @return 形如:{"returnCode":400,"returnMessage:"noteID is null"}
   */
  public static JSONObject getReturnObjFromException(Exception e) {
    JSONObject obj = new JSONObject();
    if (e instanceof ApiException) {
      return ((ApiException) e).toJSONObject();
    }

    // 可拓展其他异常类
    if (e.getClass() == NullPointerException.class) {
      obj.put(KEY_RETURN_CODE, ReturnCode.BAD_REQUEST.getValue());
    } else if (e.getClass() == IllegalArgumentException.class) {
      obj.put(KEY_RETURN_CODE, ReturnCode.BAD_REQUEST.getValue());
    } else {
      obj.put(KEY_RETURN_CODE, ReturnCode.SERVER_ERROR.getValue());
    }
    if (StringUtils.isBlank(e.getMessage())) {
      obj.put(KEY_RETURN_MESSAGE, "BAD REQUEST.");
    } else {
      obj.put(KEY_RETURN_MESSAGE, e.getMessage());
    }
    return obj;
  }

  /**
   * 根据指定的返回码和返回值,生成JSONObject
   * 
   * @param returnCode
   * @param returnMessage
   * @return
   */
  public static JSONObject getReturnObj(ReturnCode returnCode, String returnMessage) {
    JSONObject obj = new JSONObject();
    obj.put(KEY_RETURN_CODE, returnCode.getValue());
    obj.put(KEY_RETURN_MESSAGE, returnMessage);
    return obj;
  }

  /**
   * 使用缺省的{@link ReturnCode#OK}获得返回码200的对象
   * 
   * @return 形如:{"returnCode":200,"returnMessage:"OK"}
   */
  public static JSONObject getReturn200() {
    return getReturnObj(ReturnCode.OK, ReturnCode.OK.name());
  }

  /**
   * 使用缺省的{@link ReturnCode#NO_DATA_RETURN}获得返回码204的对象
   * 
   * @return 形如:{"returnCode":204,"returnMessage:"NO_DATA_RETURN"}
   */
  public static JSONObject getReturn204() {
    return getReturnObj(ReturnCode.NO_DATA_RETURN, ReturnCode.NO_DATA_RETURN.name());
  }

  /**
   * 使用缺省的{@link ReturnCode#BAD_REQUEST}获得返回码400对象
   * 
   * @return 形如:{"returnCode":400,"returnMessage:"BAD_REQUEST"}
   */
  public static JSONObject getReturn400() {
    return getReturnObj(ReturnCode.BAD_REQUEST, ReturnCode.BAD_REQUEST.name());
  }

  /**
   * 生成指定返回信息内容的提交数据格式错误400结果对象
   * 
   * @param returnMessage
   * @return
   */
  public static JSONObject getReturn400(String returnMessage) {
    return getReturnObj(ReturnCode.BAD_REQUEST, returnMessage);
  }

  /**
   * 使用缺省的{@link ReturnCode#NO_TOKEN}获得返回码401对象
   * 
   * @return 形如:{"returnCode":401,"returnMessage:"NO_TOKEN"}
   */
  public static JSONObject getReturn401() {
    return getReturnObj(ReturnCode.NO_TOKEN, ReturnCode.NO_TOKEN.name());
  }

  /**
   * 使用缺省的{@link ReturnCode#ILLEGAL_TOKEN}获得返回码403对象
   * 
   * @return 形如:{"returnCode":403,"returnMessage:"ILLEGAL_TOKEN"}
   */
  public static JSONObject getReturn403() {
    return getReturnObj(ReturnCode.ILLEGAL_TOKEN, ReturnCode.ILLEGAL_TOKEN.name());
  }

  /**
   * 使用缺省的{@link ReturnCode#DATA_NOT_FOUND}获得返回码404对象
   * 
   * @return 形如:{"returnCode":404,"returnMessage:"DATA_NOT_FOUND"}
   */
  public static JSONObject getReturn404() {
    return getReturnObj(ReturnCode.DATA_NOT_FOUND, ReturnCode.DATA_NOT_FOUND.name());
  }

  /**
   * 生成指定返回信息内容的检索不到指定的数据404结果对象
   * 
   * @param returnMessage
   * @return
   */
  public static JSONObject getReturn404(String returnMessage) {
    return getReturnObj(ReturnCode.DATA_NOT_FOUND, returnMessage);
  }

  /**
   * 使用缺省的{@link ReturnCode#NO_PERMISSION}获得返回码405对象
   * 
   * @return 形如:{"returnCode":405,"returnMessage:"NO_PERMISSION"}
   */
  public static JSONObject getReturn405() {
    return getReturnObj(ReturnCode.NO_PERMISSION, ReturnCode.NO_PERMISSION.name());
  }

  /**
   * 使用缺省的{@link ReturnCode#ILLEGAL_DATA}获得返回码409对象
   * 
   * @return 形如:{"returnCode":409,"returnMessage:"ILLEGAL_DATA"}
   */
  public static JSONObject getReturn409() {
    return getReturnObj(ReturnCode.ILLEGAL_DATA, ReturnCode.ILLEGAL_DATA.name());
  }

  /**
   * 生成指定返回信息内容的非法的参数409结果对象
   * 
   * @param returnMessage
   * @return
   */
  public static JSONObject getReturn409(String returnMessage) {
    return getReturnObj(ReturnCode.ILLEGAL_DATA, returnMessage);
  }

  /**
   * 使用缺省的{@link ReturnCode#VERSION_MISMATCHING}获得返回码412对象
   * 
   * @return 形如:{"returnCode":412,"returnMessage:"VERSION_MISMATCHING"}
   */
  public static JSONObject getReturn412() {
    return getReturnObj(ReturnCode.VERSION_MISMATCHING, ReturnCode.VERSION_MISMATCHING.name());
  }

  /**
   * 生成指定返回信息内容的版本不匹配412结果对象
   * 
   * @param returnMessage
   * @return
   */
  public static JSONObject getReturn412(String returnMessage) {
    return getReturnObj(ReturnCode.VERSION_MISMATCHING, returnMessage);
  }

  /**
   * 使用缺省的{@link ReturnCode#SERVER_ERROR}获得返回码500对象
   * 
   * @return 形如:{"returnCode":500,"returnMessage:"SERVER_ERROR"}
   */
  public static JSONObject getReturn500() {
    return getReturnObj(ReturnCode.SERVER_ERROR, ReturnCode.SERVER_ERROR.name());
  }

  /**
   * 生成指定返回信息内容的服务器错误500结果对象
   * 
   * @param returnMessage
   * @return
   */
  public static JSONObject getReturn500(String returnMessage) {
    return getReturnObj(ReturnCode.SERVER_ERROR, returnMessage);
  }

  /**
   * 使用缺省的{@link ReturnCode#SERVER_UNAVAILABLE}获得返回码503对象
   * 
   * @return 形如:{"returnCode":503,"returnMessage:"SERVER_UNAVAILABLE"}
   */
  public static JSONObject getReturn503() {
    return getReturnObj(ReturnCode.SERVER_UNAVAILABLE, ReturnCode.SERVER_UNAVAILABLE.name());
  }

}
