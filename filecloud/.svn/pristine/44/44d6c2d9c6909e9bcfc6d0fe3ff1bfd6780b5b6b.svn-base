package com.newroad.fileext.dao.qiniu;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class CloudStorageUtils {
  
  public static String filePath="test.log";
  private static FileWriter log;
  private static int fileCount = 0;
  
  static{
    try {
      log = new FileWriter(filePath);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static String generateKeyId() {
    UUID randomUUID = UUID.randomUUID();
    String key = randomUUID.toString().replaceAll("-", "");
    return key;
  }
  

  
  public static synchronized void log(String logData) {
      try {
          log.write(logData + " \r\n");
          log.flush();
          System.out.println(logData);
      } catch (Exception e) {
      }
  }
}
