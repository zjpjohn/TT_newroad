package com.lenovo.zy.info.crawler.manager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

  public static Logger logger = LoggerFactory.getLogger(Utils.class);

  public static Properties getProperties(String filePath) {
    Properties props = new Properties();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filePath));
      props.load(br);
      return props;
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage());
    } catch (IOException e) {
      logger.error(e.getMessage());
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        logger.error(e.getMessage());
      }
    }
    return null;
  }


  public static String getCurrentDateInfo() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
    Date currentDate = new Date();
    String dateHeader = format.format(currentDate);
    return dateHeader;
  }

  public static String getWPCurrentMonthInfo() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
    Date currentDate = new Date();
    String dateHeader = format.format(currentDate);
    return dateHeader;
  }

  public static File downloadFromUrl(String url, String dir) {
    try {
      URL httpurl = new URL(url);
      String fileName = getFileNameFromUrl(url);
      String destFilePath = dir + File.separator + fileName;
      logger.debug("Download file path:" + destFilePath);
      File f = new File(destFilePath);
      FileUtils.copyURLToFile(httpurl, f);
      return f;
    } catch (Exception e) {
      logger.error("Fail to download file from Url:", e);
    }
    return null;
  }


  public static String getFileNameFromUrl(String url) {
    String name = new Long(System.currentTimeMillis()).toString() + ".X";
    int index = url.lastIndexOf("/");
    int endIndex = url.lastIndexOf("?");
    if (index > 0) {
      if (endIndex > index) {
        name = url.substring(index + 1, endIndex);
      } else {
        name = url.substring(index + 1);
      }
      if (name.trim().length() > 0) {
        return name;
      }
    }
    return name;
  }

  public static void copyFileChannel(String srcFilePath, String destFilePath) {
    FileChannel in, out;
    try {
      in = new FileInputStream(srcFilePath).getChannel();
      out = new FileOutputStream(destFilePath).getChannel();
      in.transferTo(0, in.size(), out);
      out.transferFrom(in, 0, in.size());
      logger.info("Copy old file from " + srcFilePath + " to new file " + destFilePath);
    } catch (FileNotFoundException e) {
      logger.error(e.getMessage());
    } catch (IOException e) {
      logger.error(e.getMessage());
    }

  }

  public static InputStream filterBOMInputStream(InputStream in) throws IOException {
    PushbackInputStream testin = new PushbackInputStream(in);
    int ch = testin.read();
    if (ch != 0xEF) {
      testin.unread(ch);
    } else if ((ch = testin.read()) != 0xBB) {
      testin.unread(ch);
      testin.unread(0xef);
    } else if ((ch = testin.read()) != 0xBF) {
      throw new IOException("Error UTF-8 file!");
    } else {
    }
    return testin;
  }

  public static String inputStream2String(InputStream is) throws IOException {
    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
    int ch;
    while ((ch = is.read()) != -1) {
      bytestream.write(ch);
    }
    String imgdata = bytestream.toString();
    is.close();
    bytestream.close();
    return imgdata;
  }

  public static String processStringCode(String intro) {
    String encoding = Utils.getEncoding(intro);
    System.out.println("Encoding:" + encoding);
    if (!"UTF-8".equals(encoding)) {
      intro = Utils.parse2UTF(intro);
    }
    logger.info("Product Introduction:" + intro);
    System.out.println("Product Introduction:" + intro);
    return intro;
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
    encode = "GBK";
    try {
      if (str.equals(new String(str.getBytes(encode), encode))) {
        String s3 = encode;
        return s3;
      }
    } catch (Exception e3) {
    }
    encode = "UTF-8";
    try {
      if (str.equals(new String(str.getBytes(encode), encode))) {
        String s2 = encode;
        return s2;
      }
    } catch (Exception e2) {
    }
    return "";
  }

  public static String parse2UTF(String str) {
    String urlEncode = "";
    try {
      urlEncode = URLEncoder.encode(str, "UTF-8");
      return java.net.URLDecoder.decode(urlEncode, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static int getWordCount(String str, String sub) {
    int index = 0;
    int count = 0;

    while ((index = str.indexOf(sub)) != -1) {
      str = str.substring(index + sub.length());
      count++;
    }
    return count;
  }


}
