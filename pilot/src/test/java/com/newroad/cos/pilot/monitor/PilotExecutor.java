package com.newroad.cos.pilot.monitor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cos.pilot.PilotException;
import com.newroad.cos.pilot.PilotOssCloud;
import com.newroad.cos.pilot.PilotOssEx;
import com.newroad.cos.pilot.PilotOssObjectBaseEx;
import com.newroad.cos.pilot.SystemProperties;

/**
 * @author tangzj1
 * @version 2.0
 * @since May 12, 2014
 */
public class PilotExecutor {

  private final Logger logger = LoggerFactory.getLogger(PilotExecutor.class);

  private static PilotOssEx cos;
  private static FileWriter log;

  public void connectByConnector() {
    cos = PilotOssCloud.CreateOssCloudEx();
    boolean isLogin = false;
    try {
      isLogin = cos.loginByConnector(SystemProperties.devID, SystemProperties.devKey, SystemProperties.kkconnector);
      logger.info("connect by Connector is " + isLogin);
    } catch (PilotException e) {
      logger.error("connectByConnector Exception:", e);
    }
  }

  public String connectByToken() {
    cos = PilotOssCloud.CreateOssCloudEx();
    try {
      long expire = 3600 * 12;
      String token =
          cos.generateToken(SystemProperties.appID, SystemProperties.devID, SystemProperties.devKey, SystemProperties.kkaccountID, expire);
      // cos.useToken(token);
      return token;
    } catch (PilotException e) {
      logger.error("connectByToken Exception:", e);
    }
    return null;
  }

  public FileObject uploadFile(String sourceFilePath, String keyID) {
    FileObject fileObject = new FileObject();
    boolean isUpload = false;
    File testFile = new File(sourceFilePath);
    long len = testFile.length();
    fileObject.setKey(keyID);
    fileObject.setFileSize(len);
    // OutputStream outStream = null;
    // File tempFile = new File(outputFilePath);
    try {
      long start = System.currentTimeMillis();
      PilotOssObjectBaseEx cosObj = cos.createObject(SystemProperties.bucketName, keyID, null);
      isUpload = cosObj.putObject(testFile, "application/octet-stream", 0, len, null, null);
      long end = System.currentTimeMillis() - start;
      fileObject.setUploadExecuteTime(end);
      if (isUpload) {
        fileObject.setExecuteStatus(true);
      } else {
        fileObject.setExecuteStatus(false);
      }
    } catch (PilotException e) {
      logger.error("UploadFile PilotException:", e);
    }
    return fileObject;
  }

  public FileObject downLoadFile(String outputFilePath) {
    List<String> keyList =
        Arrays.asList("00407ebfa5b80100", "00407ebfa2260300", "00407ebfa9f80400", "00407ebf9ae90300", "00407ebf9e6e0100",
            "88eb67841cbe484291356eea47ad884a", "68f4af9e1b6b43edaede2e43e5c21a8a", "084341ad03ce4f969c9e17f3272b9599",
            "dde1916d3a3a44d99003f0192aa93cda", "42f553a932be4a37ab14bb17b04e71eb", "aceb8babb68b485c95ec23ee6e20f2a2", "0040850ea5260100",
            "cf5922249ce04ccbadf1fc290b710bae", "7ce4dd12c9cb4821af31d95473798b08", "f7edbb295a144055863ef33bd6703e2d",
            "4226daaa09764fe5b9da2182e5e44d5a", "bfd3427501e24b9eb0d80ca71699dc31", "a5fee5a3a80c42518864aa471d6b4a5b",
            "4d833a1f84124422a6eab1ce106f10db", "e33b42a0d17c4a6390ae802d4646a8db");

    Random random = new Random();
    int randomNum = random.nextInt(keyList.size());

    FileObject fileObject = new FileObject();
    PilotOssObjectBaseEx ossObject = null;
    String key = null;
    try {
      log = new FileWriter("D:\\usr\\COSLog.log", true);

      key = keyList.get(randomNum);
      ossObject = cos.getOssObject(SystemProperties.bucketName, key);
      // ossObject.registerListener(ossCallback);
      String keyID = ossObject.getKeyID();
      if (keyID == null) {
        System.out.println("error key:" + key);
        return null;
      }
      fileObject.setKey(keyID);

      Map<String, String> objectInfoMap = ossObject.getObjectInfoMap();
      String size = objectInfoMap.get("size");

      fileObject.setFileSize(Long.valueOf(size));
      long start = System.currentTimeMillis();
      boolean isDownLoad = ossObject.getObject(new FileOutputStream(new File(outputFilePath + key)), 0, -1, null);
      long end = (System.currentTimeMillis() - start);
      fileObject.setUploadExecuteTime(end);
      if (isDownLoad) {
        fileObject.setExecuteStatus(true);
      } else {
        fileObject.setExecuteStatus(false);
      }
      writeLog("Down load file keyID:" + key + ", size:" + size + ", time in:" + (System.currentTimeMillis() - start) + "ms \r\n");
    } catch (Exception e) {
      writeLog("ERROR 404: Down load file keyID:" + key + " \r\n");
    } finally {
      try {
        log.flush();
        log.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return fileObject;
  }

  private static void writeLog(String str) {
    try {
      log.write(str);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
