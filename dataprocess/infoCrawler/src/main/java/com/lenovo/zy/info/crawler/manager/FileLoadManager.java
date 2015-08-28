package com.lenovo.zy.info.crawler.manager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.constants.Constants;
import com.lenovo.zy.info.crawler.domain.FileData;
import com.lenovo.zy.info.crawler.domain.Photo;
import com.lenovo.zy.info.crawler.domain.Product;
import com.lenovo.zy.info.crawler.domain.ProductBanner;
import com.lenovo.zy.info.crawler.domain.ProductCategory;
import com.lenovo.zy.info.crawler.manager.db.SqlSessionManager;
import com.lenovo.zy.info.crawler.manager.db.ZYProductDao;

public class FileLoadManager {

  public static Logger logger = LoggerFactory.getLogger(FileLoadManager.class);

  private ZYProductDao dataManager;

  public FileLoadManager() {
    super();
    this.dataManager = new ZYProductDao(SqlSessionManager.getSqlSession());
  }

  public Product loadProductInfo(String baseFilePath, String introFilePath) {
    Product product = null;
    String productName = null;
    String summary = null;
    Long categoryId = 0L;
    Long publishUserId = 186842l;

    Properties props = new Properties();
    BufferedReader br = null;
    InputStream is=null;
    try {
      br = new BufferedReader(new FileReader(baseFilePath));
      props.load(br);
      productName = props.getProperty("Name").replaceAll(" ", "");
      summary = props.getProperty("Summary");
      categoryId = Long.valueOf(props.getProperty("Category"));

      is = Utils.filterBOMInputStream( new BufferedInputStream(new FileInputStream(introFilePath)));
      String introduction=Utils.inputStream2String(is);

      product = new Product(productName,"2", summary, introduction, publishUserId);
      product.setCategoryId(categoryId);
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
    return product;
  }

  public Map<Long, String> saveResourceGroup(String folderPath, Integer fileType, String shortDestFolderPath) {
    Map<Long, String> pictureMap = new TreeMap<Long, String>();
    String localDestFolderPath = Constants.ZY_FILE_STORE_HEADER + shortDestFolderPath;
    String linkUrlPath = Constants.LINK_HEADER + shortDestFolderPath;
    File destFolder = new File(localDestFolderPath);
    if (!destFolder.exists())
      destFolder.mkdir();
    logger.info("Create local folder path:" + destFolder.getAbsolutePath());
    File bannerFolder = new File(folderPath);
    if (bannerFolder.isDirectory()) {
      String oldfPath = bannerFolder.getPath();
      for (String filePath : bannerFolder.list()) {
        File file = new File(filePath);
        String newFileName = UUID.randomUUID().toString() + "_" + file.getName();
        // File newFile = new File(localDestFolderPath + File.separator + newFileName);
        String srcPath = oldfPath + File.separator + filePath;
        String destPath = localDestFolderPath + File.separator + newFileName;
        Utils.copyFileChannel(srcPath, destPath);
        String fileLink = linkUrlPath + "/" + newFileName;
        logger.info("FileLink:" + fileLink);
        FileData fileData = new FileData(file.getName(), fileType, "", fileLink, destPath, file.length());
        dataManager.saveResource(fileData);
        Photo photo = new Photo(fileLink);
        dataManager.savePhoto(photo);
        pictureMap.put(photo.getPhotoId(), fileLink);
      }
    }
    return pictureMap;
  }

  public Map<String, String> saveProductResourceGroup(String folderPath, String shortDestFolderPath, String productName) {
    Map<String, String> pictureMap = new TreeMap<String, String>();
    String localDestFolderPath = Constants.ZY_FILE_STORE_HEADER + shortDestFolderPath;
    String linkUrlPath = Constants.LINK_HEADER + shortDestFolderPath;
    File destFolder = new File(localDestFolderPath);
    if (!destFolder.exists())
      destFolder.mkdir();
    logger.info("Create local folder path:" + destFolder.getAbsolutePath());
    File bannerFolder = new File(folderPath);
    if (bannerFolder.isDirectory()) {
      String oldfPath = bannerFolder.getPath();
      for (String filePath : bannerFolder.list()) {
        File file = new File(filePath);
        String shortName = file.getName();
        String newFileName = UUID.randomUUID().toString() + "_" + shortName;
        // File newFile = new File(localDestFolderPath + File.separator + newFileName);
        String srcPath = oldfPath + File.separator + filePath;
        String destPath = localDestFolderPath + File.separator + newFileName;
        Utils.copyFileChannel(srcPath, destPath);
        String fileLink = linkUrlPath + "/" + newFileName;
        logger.info("Product FileLink:" + fileLink);
        FileData fileData = new FileData(shortName, 3, "", fileLink, destPath, file.length());
        dataManager.saveResource(fileData);
        Photo photo = new Photo(fileLink);
        dataManager.savePhoto(photo);
        pictureMap.put(shortName, fileLink);
      }
    }
    return pictureMap;
  }

  public void mixProductResource(Product product, Map<Long, String> beginResourceMap, Map<Long, String> bannerResourceMap,
      Map<String, String> productResourceMap) {
    Set<Entry<Long, String>> beginSet = beginResourceMap.entrySet();
    Iterator<Entry<Long, String>> iter = beginSet.iterator();
    Entry<Long, String> entry = iter.next();
    product.setProductLink(entry.getValue());

    String intro=product.getIntroduction();
    product.setIntroduction(replaceIntroductionLink(Utils.processStringCode(intro), productResourceMap));

    dataManager.saveProduct(product);
    Long productId = product.getProductId();
    dataManager.saveProductCategory(new ProductCategory(product.getCategoryId(), productId));
    dataManager.saveProductTagScope(productId, 9l);

    Set<Entry<Long, String>> bannerSet = bannerResourceMap.entrySet();
    Iterator<Entry<Long, String>> iter2 = bannerSet.iterator();
    while (iter2.hasNext()) {
      Entry<Long, String> entry2 = iter2.next();
      dataManager.saveProductBanner(new ProductBanner(productId, entry2.getKey()));
    }
  }

  private String replaceIntroductionLink(String introduction, Map<String, String> productResourceMap) {
    // Set<Entry<String, String>> productSet = productResourceMap.entrySet();
    // Iterator<Entry<String, String>> iter = productSet.iterator();
    // int length = introduction.length();
    int offset = 0;
    int count = 0;
    String key = "src=";
    while ((offset = introduction.indexOf(key, offset)) != -1) {
      int beginIndex = introduction.indexOf(key, offset) + 4;
      int endIndex = introduction.indexOf("\"", beginIndex + 1) + 1;
      String regex = introduction.substring(beginIndex + 1, endIndex - 1);
      int index = regex.lastIndexOf("/");
      String pictureName = regex.substring(index + 1);
      String pictureLink = productResourceMap.get(pictureName);
      logger.info("Update Introduction picture " + count + " " + pictureName + " OldLink:" + regex + ",NewLink:" + pictureLink);
      if (pictureLink != null) {
        introduction = introduction.replaceFirst(regex, pictureLink);
      }
      offset = offset + key.length();
      count++;
    }
    logger.info("Introduction:" + introduction);
    return introduction;
  }
  
}
