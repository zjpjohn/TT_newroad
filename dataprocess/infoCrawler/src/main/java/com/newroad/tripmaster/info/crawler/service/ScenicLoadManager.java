package com.newroad.tripmaster.info.crawler.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.info.crawler.SpringContextUtil;
import com.newroad.tripmaster.info.crawler.cloud.QiniuUploadManager;
import com.newroad.tripmaster.info.crawler.dao.ScenicDao;
import com.newroad.tripmaster.info.crawler.domain.Scenic;

public class ScenicLoadManager {

  public static Logger logger = LoggerFactory.getLogger(ScenicLoadManager.class);

  public static String MAIN_PICTURE = "mainPicture";

  public static String MAIN_THUMB = "mainThumb";

  public static String BANNER_PICTURES = "bannerPictures";

  public Map<String, Object> loadPictureResource(String beginRFilePath, String bannerRFolder, String thumbFilePath) {
    Map<String, Object> map = new HashMap<String, Object>();
    String mainPicURL = commitPictureCloud(beginRFilePath);
    logger.info("MainPictureURL:" + mainPicURL);
    map.put(MAIN_PICTURE, mainPicURL);
    String mainThumb = commitPictureCloud(thumbFilePath);
    logger.info("MainThumbURL:" + mainThumb);
    map.put(MAIN_THUMB, mainThumb);
    File bannerFolder = new File(bannerRFolder);

    if (bannerFolder.isDirectory()) {
      String[] filePaths = bannerFolder.list();
      String[] bannerURLs = new String[filePaths.length];
      for (int i = 0; i < filePaths.length; i++) {
        String absoluteFilePath = bannerFolder + File.separator + filePaths[i];
        bannerURLs[i] = commitPictureCloud(absoluteFilePath);
      }
      logger.info("BannerPicURLs:" + bannerURLs);
      map.put(BANNER_PICTURES, bannerURLs);
    }
    return map;
  }

  public String commitPictureCloud(String localFilePath) {
    File file = new File(localFilePath);
    QiniuUploadManager uploadManager = new QiniuUploadManager();
    uploadManager.upload(file);
    return uploadManager.getFileCloudURL();
  }

  public Scenic loadScenicInfo(String contentInfo, Map<String, Object> pictureURLs) {
    Scenic scenic = null;
    String hashid = null;
    String name = null;
    String summary = null;
    String description = null;
    Integer type = 4;

    Properties props = new Properties();
    BufferedReader br = null;
    InputStream is = null;
    try {
      br = new BufferedReader(new FileReader(contentInfo));
      props.load(br);
      Set<Entry<Object, Object>> set = props.entrySet();
      Iterator<Entry<Object, Object>> iter = set.iterator();
      while (iter.hasNext()) {
        Entry<Object, Object> entry = iter.next();
        logger.debug("Property Info:" + entry);
        //name key contain blank char
        String key = (String) entry.getKey();
        if (key.indexOf("name") >= 0) {
          name = (String) entry.getValue();
          break;
        }
      }
      hashid = props.getProperty("hashid");
      summary = props.getProperty("summary");
      description = props.getProperty("description");
      type = Integer.valueOf(props.getProperty("type"));

      // is = Utils.filterBOMInputStream( new BufferedInputStream(new
      // FileInputStream(introFilePath)));
      // String introduction=Utils.inputStream2String(is);

      scenic = new Scenic(hashid, name, summary, description, type);
      scenic.setPicture((String) pictureURLs.get(MAIN_PICTURE));
      scenic.setThumbpic((String) pictureURLs.get(MAIN_THUMB));
      scenic.setCarouselpic((String[]) pictureURLs.get(BANNER_PICTURES));
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
    return scenic;
  }

  public static String parse2UTF(String str) {
    String urlEncode = "";
    try {
      urlEncode = URLEncoder.encode(str, "UTF-8");
      String result = java.net.URLDecoder.decode(urlEncode, "UTF-8");
      logger.info("UTFEncode:" + urlEncode + ",parse2UTF:" + result);
      return result;
    } catch (UnsupportedEncodingException e) {
      logger.error("parse2UTF Exception:", e);
      return null;
    }
  }

  public boolean saveScenicInfo(Scenic scenic) {
    boolean result = false;
    ScenicDao scenicDao = SpringContextUtil.getBean(ScenicDao.class);
    Object id = scenicDao.save(scenic);
    if (id != null) {
      result = true;
    }
    return result;
  }

}
