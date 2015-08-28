package com.lenovo.zy.info.crawler;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.domain.Product;
import com.lenovo.zy.info.crawler.manager.FileLoadManager;
import com.lenovo.zy.info.crawler.manager.MachineCrawlLoadManager;
import com.lenovo.zy.info.crawler.manager.MachineCrawlWPManager;
import com.lenovo.zy.info.crawler.manager.Utils;

public class MainAction {

  public static Logger logger = LoggerFactory.getLogger(MainAction.class);

  private FileLoadManager fileManager;

  private MachineCrawlLoadManager crawlManager;

  private MachineCrawlWPManager crawlWPManager;

  public void processProductInfo(String currentDateHeader, String beginRFolder, String bannerRFolder, String productRFolder,
      String baseFilePath, String introFilePath) {
    fileManager = new FileLoadManager();

    Product product = fileManager.loadProductInfo(baseFilePath, introFilePath);
    String productName = product.getProductName();

    Map<Long, String> beginResourceMap = fileManager.saveResourceGroup(beginRFolder, 1, currentDateHeader);
    Map<Long, String> bannerResourceMap = fileManager.saveResourceGroup(bannerRFolder, 2, currentDateHeader);
    Map<String, String> productResourceMap = fileManager.saveProductResourceGroup(productRFolder, currentDateHeader, productName);

    fileManager.mixProductResource(product, beginResourceMap, bannerResourceMap, productResourceMap);
  }

  public void processProductCrawlInfo(String currentDateHeader) {
    crawlManager = new MachineCrawlLoadManager(currentDateHeader);
    crawlManager.loadWebCrawlData();
  }

  public void processProductCrawlInfoWP() {
    crawlWPManager = new MachineCrawlWPManager();
    crawlWPManager.loadWebCrawlData();
  }

  public static void main(String[] args) {
    String currentDateHeader = Utils.getCurrentDateInfo();
    logger.info("Current Info Crawller Date:" + currentDateHeader);
    args=new String[1];
    args[0]="zy";
    MainAction action = new MainAction();
    if (args == null || args.length == 0) {
      logger.info("Content crawling from machine to wordpress...");
      action.processProductCrawlInfoWP();
    } else if ("zy".equals(args[0])) {
      logger.info("Content crawling from machine to zy_product...");
      action.processProductCrawlInfo(currentDateHeader);
    } else if ("f".equals(args[0])) {
      String productFolder = args[0];
      String beginRFolder = productFolder + File.separator + "begin";
      String bannerRFolder = productFolder + File.separator + "banner";
      String productRFolder = productFolder + File.separator + "product";
      String baseFilePath = productFolder + File.separator + "base.txt";
      String introFilePath = productFolder + File.separator + "intro.txt";
      logger.info("Content crawlling from file...");
      action.processProductInfo(currentDateHeader, beginRFolder, bannerRFolder, productRFolder, baseFilePath, introFilePath);
    }
  }



}
