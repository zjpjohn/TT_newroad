package com.lenovo.zy.info.crawler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.lenovo.zy.info.crawler.manager.Utils;

public class TestMain {
  public static String introFilePath = "C:\\Users\\tangzj1\\Desktop\\智呀\\上架产品库\\101-106\\103\\intro.txt";

  public static void main(String[] argv) {
    String intro = null;
    InputStream br = null;
    try {
      br = Utils.filterBOMInputStream( new BufferedInputStream(new FileInputStream(introFilePath)));
      intro=Utils.inputStream2String(br);
      checkStringCode(intro);
      //Utils.processStringCode(intro);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void checkStringCode(String str) {
    try {
      System.out.println("1:" + new String(str.getBytes("GBK"),"ISO8859_1"));
      System.out.println("2:" +new String(str.getBytes("GBK"),"utf-8"));
      System.out.println("3:" +new String(str.getBytes("GBK"),"GB2312"));
      System.out.println("4:" +new String(str.getBytes("GBK"),"GBK"));
      System.out.println("5:" +new String(str.getBytes("ISO8859_1"),"GBK"));
      System.out.println("6:" +new String(str.getBytes("ISO8859_1"),"ISO8859_1"));
      System.out.println("7:" +new String(str.getBytes("ISO8859_1"),"GB2312"));
      System.out.println("8:" +new String(str.getBytes("ISO8859_1"),"utf-8"));
      System.out.println("9:" +new String(str.getBytes("utf-8"),"GBK"));
      System.out.println("10:" +new String(str.getBytes("utf-8"),"utf-8"));
      System.out.println("11:" +new String(str.getBytes("utf-8"),"GB2312"));
      System.out.println("12:" +new String(str.getBytes("utf-8"),"ISO8859_1"));
      System.out.println("13:" +new String(str.getBytes("GB2312"),"GB2312"));
      System.out.println("14:" +new String(str.getBytes("GB2312"),"ISO8859_1"));
      System.out.println("15:" +new String(str.getBytes("GB2312"),"utf-8"));
      System.out.println("16:" +new String(str.getBytes("GB2312"),"GBK"));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
