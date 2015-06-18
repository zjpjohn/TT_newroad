package com.newroad.util.encoding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringEncodeUtil {
  
  private static Logger logger = LoggerFactory.getLogger(StringEncodeUtil.class);
  /**
   * ISO-8859-1转utf-8
   * 
   * @param isoStr
   * @return
   */
  public static String parseISO2UTF(String isoStr) {
    try {
      if ("ISO-8859-1".equals(getEncoding(isoStr))) {
        byte bb[];
        bb = isoStr.getBytes("ISO-8859-1"); // 以"ISO-8859-1"方式解析name字符串
        return new String(bb, "UTF-8"); // 再用"utf-8"格式表示name
      }
      return isoStr;
    } catch (Exception e) {
      logger.error("parseISO2UTF Exception:", e);
      return null;
    }
  }

  /**
   * ISO-8859-1转GBK
   * 
   * @param isoStr
   * @return
   */
  public static String parseISO2GBK(String isoStr) {
    try {
      return new String(isoStr.getBytes("ISO-8859-1"), "GBK");
    } catch (Exception e) {
      logger.error("parseISO2GBK Exception:", e);
      return null;
    }
  }

  public static String parse2UTF(String str) {
    String urlEncode = "";
    try {
      urlEncode = URLEncoder.encode(str, "UTF-8");
      return java.net.URLDecoder.decode(urlEncode, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      logger.error("parse2UTF Exception:", e);
      return null;
    }
  }
  
  /**
   * 判断字符串的编码
   * 
   * @param str
   * @return
   */
  public static String getEncoding(String str) {
    String encode = "GB2312";
    try {
      if (str.equals(new String(str.getBytes(encode), encode))) {
        String s = encode;
        return s;
      }
    } catch (Exception e) {
    }
    encode = "ISO-8859-1";
    try {
      if (str.equals(new String(str.getBytes(encode), encode))) {
        String s1 = encode;
        return s1;
      }
    } catch (Exception e1) {
    }
    encode = "UTF-8";
    try {
      if (str.equals(new String(str.getBytes(encode), encode))) {
        String s2 = encode;
        return s2;
      }
    } catch (Exception e2) {
    }
    encode = "GBK";
    try {
      if (str.equals(new String(str.getBytes(encode), encode))) {
        String s3 = encode;
        return s3;
      }
    } catch (Exception e3) {
    }
    return "";
  }
}
