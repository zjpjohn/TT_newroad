package com.newroad.cos.pilot.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cos.pilot.PilotException;
import com.newroad.cos.pilot.PilotOssCloud;
import com.newroad.cos.pilot.PilotOssEx;
import com.newroad.cos.pilot.PilotOssObjectBaseEx;
import com.newroad.cos.pilot.SystemProperties;

/**
 * @info
 * @author tangzj1
 * @date Feb 10, 2014
 * @version
 */
public class PilotTest extends TestCase {

  private static Logger logger = LoggerFactory.getLogger(PilotTest.class);

  private static FileWriter log;

  private static PilotOssEx cos;

  @Before
  public void setUp() {
    cos = PilotOssCloud.CreateOssCloudEx();
    try {
      // boolean isLogin = cos.loginByConnector(SystemProperties.devID, SystemProperties.devKey,
      // "http://iam.lenovows.com/v1/connector/00408a59e71d3400");
      // assertTrue("Login COS status:", isLogin);
      long expire = 3600 * 12;
      String token = cos.generateToken(SystemProperties.appID, SystemProperties.devID, SystemProperties.devKey, "10026280048", expire);
      cos.useToken(token);
    } catch (PilotException e) {
      logger.error("setup PilotException:" + e);
    } catch (IOException e) {
      logger.error("setup IOException:" + e);
    }
  }

  @Test
  public void testPutObject() {
    boolean isUpload = false;
    File testFile = new File("D:\\supernote\\return1.jpg");
    long len = testFile.length();
    // OutputStream outStream = null;
    // File tempFile = new File(outputFilePath);
    try {
      long start = System.currentTimeMillis();
      PilotOssObjectBaseEx cosObj = cos.createObject(SystemProperties.bucketName, "22312412423", null);
      isUpload = cosObj.putObject(testFile, "application/octet-stream", 0, len, null, null);
      long end = System.currentTimeMillis() - start;
      logger.info("Upload File Execute Time:" + end);
    } catch (PilotException e) {
      e.printStackTrace();
    }
  }


  @Test
  public void testDownLoad() {

    try {
      log = new FileWriter("D:\\usr\\COSLog.log", true);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    // List<String> keyList =
    // Arrays.asList("00407ebfa5b80100", "00407ebfa2260300", "00407ebfa9f80400", "00407ebf9ae90300",
    // "00407ebf9e6e0100",
    // "88eb67841cbe484291356eea47ad884a", "68f4af9e1b6b43edaede2e43e5c21a8a",
    // "084341ad03ce4f969c9e17f3272b9599",
    // "dde1916d3a3a44d99003f0192aa93cda", "42f553a932be4a37ab14bb17b04e71eb",
    // "aceb8babb68b485c95ec23ee6e20f2a2", "0040850ea5260100",
    // "cf5922249ce04ccbadf1fc290b710bae", "7ce4dd12c9cb4821af31d95473798b08",
    // "f7edbb295a144055863ef33bd6703e2d",
    // "4226daaa09764fe5b9da2182e5e44d5a", "bfd3427501e24b9eb0d80ca71699dc31",
    // "a5fee5a3a80c42518864aa471d6b4a5b",
    // "4d833a1f84124422a6eab1ce106f10db", "e33b42a0d17c4a6390ae802d4646a8db");

    // OssCallbacks ossCallback = new OssCallbacks();
    PilotOssObjectBaseEx ossObject = null;
    String key = "00413fef06220300";
    // out: for (String key : keyList) {
    // for (int i = 1; i <= 3; i++) {
    long start = System.currentTimeMillis();
    try {
      ossObject = cos.getOssObject(SystemProperties.bucketName, key);
      // ossObject.registerListener(ossCallback);
      String keyID = ossObject.getKeyID();
      if (keyID == null) {
        System.out.println("error key:" + key);
      }

      Map<String, String> objectInfoMap = ossObject.getObjectInfoMap();
      String size = objectInfoMap.get("size");

      ossObject.getObject(new FileOutputStream(new File("D:\\usr\\" + key)), 0, -1, null);

      writeLog("Down load file keyID:" + key + ", size:" + size + ", time in:" + (System.currentTimeMillis() - start) + "ms \r\n");
    } catch (Exception e) {
      writeLog("ERROR 404: Down load file keyID:" + key + " \r\n");
      // continue out;
    } finally {
      // if(ossObject!=null){
      // ossObject.unregisterListener(ossCallback);
      // }
    }
    // }
    // }

    try {
      log.flush();
      log.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private static void writeLog(String str) {
    try {
      log.write(str);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static String getPercent(long a, long b) {
    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(2);
    String result = numberFormat.format((float) a / (float) b * 100);
    return result;
  }

}
