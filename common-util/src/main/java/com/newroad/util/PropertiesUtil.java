package com.newroad.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载类properties文件工具类<br>
 * 类似properties文件内容格式,即键-值对格式的文本文件的读取
 * 
 */
public final class PropertiesUtil {

  private PropertiesUtil() {}

  /**
   * logger
   */
  private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

  /**
   * 从包路径加载properties文件
   * 
   * @param classpath 文件的包路径
   * @return
   */
  public static Properties getProperties(String classpath) {
    Properties p = new Properties();
    InputStream is = null;
    try {
      is = PropertiesUtil.class.getClassLoader().getResourceAsStream(classpath);
      p.load(is);
      is.close();
    } catch (Exception e) {
      logger.error("load properties file exception:", e);
    } finally {
      try {
        is.close();
      } catch (Exception e) {
        logger.error("load properties close exception:", e);
      }
    }
    return p;
  }
}
