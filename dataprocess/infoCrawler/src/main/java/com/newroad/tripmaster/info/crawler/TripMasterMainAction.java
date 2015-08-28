package com.newroad.tripmaster.info.crawler;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.manager.Utils;
import com.newroad.tripmaster.info.crawler.domain.Scenic;
import com.newroad.tripmaster.info.crawler.service.ScenicLoadManager;

public class TripMasterMainAction {

  public static Logger logger = LoggerFactory.getLogger(TripMasterMainAction.class);

  private ScenicLoadManager fileManager;

  private void processTripMasterInfo(String currentDateHeader, String beginRFilePath, String bannerRFolder, String thumbFilePath,
      String contentFilePath) {
    fileManager = new ScenicLoadManager();
    Map<String, Object> pictureURLs = fileManager.loadPictureResource(beginRFilePath, bannerRFolder, thumbFilePath);
    Scenic scenic = fileManager.loadScenicInfo(contentFilePath, pictureURLs);
    if (fileManager.saveScenicInfo(scenic)) {
      logger.info("The scenice save task from " + contentFilePath + " has been completed!");
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    String currentDateHeader = Utils.getCurrentDateInfo();
    logger.info("Current Info Crawller Date:" + currentDateHeader);

    String folderPath = args[0];
    File resouceFolder = new File(folderPath);
    TripMasterMainAction action = new TripMasterMainAction();
    if (args == null || args.length == 0) {
      logger.info("Content crawling from machine to wordpress...");
    } else {
      if (resouceFolder.isDirectory()) {
        String[] filePaths = resouceFolder.list();
        for (int i = 0; i < filePaths.length; i++) {
          String productFolder = args[0] + File.separator + filePaths[i];
          String beginFile = productFolder + File.separator + "main.jpg";
          String bannerRFolder = productFolder + File.separator + "lb";
          String thumbFile = productFolder + File.separator + "mainthumb.jpg";
          String contentFilePath = productFolder + File.separator + "content.txt";
          logger.info("Content crawlling from file path:" + productFolder);
          action.processTripMasterInfo(currentDateHeader, beginFile, bannerRFolder, thumbFile, contentFilePath);
        }
      }

      // String productFolder = args[0];
      // String beginFile = productFolder + File.separator + "main.jpg";
      // String bannerRFolder = productFolder + File.separator + "lb";
      // String thumbFile = productFolder + File.separator + "mainthumb.jpg";
      // String contentFilePath = productFolder + File.separator + "conten.txt";
      // logger.info("Content crawlling from file path:" + productFolder);
      // action.processTripMasterInfo(currentDateHeader, beginFile, bannerRFolder, thumbFile,
      // contentFilePath);
    }
  }
}
