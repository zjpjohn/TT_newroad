package com.newroad.util.validate;

import java.util.Collection;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.newroad.util.exception.CommonError;

/**
 * 简单的校验器
 * 
 * @author xupeng3
 * 
 */
public class Validator {

  private Validator() {}

  /**
   * 校验参数对象target是否是空值, 当target是String型时,是否是空串""; 当target是Map型时,是否是未包含任何元素的空Map;
   * 当target是Collection型时,是否是未包含任何元素的空Collection;
   * 
   * @param target
   * @return
   */
  @SuppressWarnings("rawtypes")
  public static boolean isNull(Object target) {
    if (target == null) {
      return true;
    }
    if (target instanceof String && StringUtils.isBlank((String) target)) {
      return true;
    }
    if (target instanceof Map && MapUtils.isEmpty((Map) target)) {
      return true;
    }
    if (target instanceof Collection && CollectionUtils.isEmpty((Collection) target)) {
      return true;
    }
    if (target instanceof Object[] && ((Object[]) target).length == 0) {
      return true;
    }
    return false;
  }

  /**
   * 验证json对象是否有效, 仅当参数js对象不为空且包含error字段时,返回true
   * 
   * @param js
   * @return 当js中包含"error"的key时,返回true
   */
  public static boolean isErrorJson(JSONObject js) {
    if (js != null && js.containsKey(CommonError.ERROR_KEY)) {
      return true;
    }
    return false;
  }

  /**
   * 用{@link#isErrorJson}判断参数js对象是否有效,当无效时,抛出Exception对象
   * 
   * @param js
   * @throws Exception
   */
  public static void throwExceptionIfErrorJson(JSONObject js) throws Exception {
    if (isErrorJson(js)) {
      throw new Exception(js.toString());
    }
  }

}
