package com.newroad.util.wrapper;

/**
 * 包装工具类
 * @author xupeng
 *
 */
public class WrapperUtils {
	private WrapperUtils(){
	}
	
	/**
	 * 将Ojbect对象转换成Integer对象,若参数为非整型值,返回null
	 * @param obj
	 * @return
	 */
	public static Integer parseInteger(Object obj) {
    if(obj == null) {
      return null;
    }
    try {
      return Integer.valueOf(String.valueOf(obj));
    } catch (Exception e) {
      return null;
    }
	}
	
	/**
	 * 将Ojbect对象转换成Double,若参数为非浮点值,返回null
	 * @param obj
	 * @return
	 */
	public static Double parseDouble(Object obj) {
		Double i = null;
		try {
			i = Double.valueOf(String.valueOf(obj));
		} catch (Exception e) {
			i = null;
		}
		return i;
	}
	
	/**
	 * 将Ojbect对象转换成Boolean,若参数为非布尔值,返回false
	 * @param obj
	 * @return
	 */
	public static Boolean parseBoolean(Object obj) {
		if(obj == null) {
			return false;
		}
		try {
			return Boolean.valueOf(String.valueOf(obj));
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 将Ojbect对象转换成Long,若参数为非数值值,返回null
	 * @param obj
	 * @return
	 */
	public static Long parseLong(Object obj) {
		if(obj == null) {
			return null;
		}
		try {
			return Long.valueOf(String.valueOf(obj));
		} catch (Exception e) {
			return null;
		}
	}
	
}
