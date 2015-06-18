package com.newroad.util.text;

import org.apache.commons.lang.StringUtils;

/**
 * 转换器
 * @author xupeng3
 *
 */
public class Converter {

  private Converter() {}

  /*
   * 版本权值,支持最多4级,每级对多3位的版本,如:1.23.456.789
   */
  private static final int VERSION_POWER_BASE = 1000;

  /**
   * 将版本号换算成数值,版本每位数字与对应的权值的积的和. 例如,text=1.23.456.789,则,计算值= 1 * VERSION_POWER_BASE^3 + 23 *
   * VERSION_POWER_BASE^2 + 456 * VERSION_POWER_BASE^1 + 789 * VERSION_POWER_BASE^0 <br>
   * 如果,text不满足表达式:^\d{1,3}(\.\d{1,3}){0,3}$,则返回-1
   * 
   * @param text
   * @return
   */
  public static long calcVersion(String text) {
    if (StringUtils.isBlank(text)) {
      return -1L;
    }
    if (text.startsWith("v")) {
      text = text.substring(1, text.length());
    }
    if (!text.matches("^\\d{1,3}(\\.\\d{1,3}){0,3}$")) {
      return -1L;
    }
    String[] number = text.split("\\.");
    if (number == null || number.length == 0) {
      return -1L;
    }
    long version = 0;
    for (int i = 0; i < number.length; i++) {
      version += Long.parseLong(number[i]) * Math.pow(VERSION_POWER_BASE, 3 - i);
    }
    return version;
  }

}
