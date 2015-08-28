package com.lenovo.zy.info.crawler.manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.constants.Constants;
import com.lenovo.zy.info.crawler.domain.FileData;
import com.lenovo.zy.info.crawler.domain.Photo;
import com.lenovo.zy.info.crawler.domain.ProductCapture;
import com.lenovo.zy.info.crawler.manager.db.SqlSessionManager;
import com.lenovo.zy.info.crawler.manager.db.WPPostDao;
import com.lenovo.zy.info.crawler.manager.db.ZYProductDao;
import com.lenovo.zy.info.crawler.wp.domain.WPPost;
import com.lenovo.zy.info.crawler.wp.domain.WPPostMeta;


public class MachineCrawlWPManager {

  public static Logger logger = LoggerFactory.getLogger(MachineCrawlWPManager.class);

  private ZYProductDao dataManager;

  private WPPostDao wpDataManager;

  private String dateHeader;

  public MachineCrawlWPManager() {
    super();
    this.dataManager = new ZYProductDao(SqlSessionManager.getSqlSession());
    this.wpDataManager = new WPPostDao(SqlSessionManager.getWPSqlSession());
    this.dateHeader = Utils.getWPCurrentMonthInfo();
  }

  public void loadWebCrawlData() {
    int status = 0;
    int num = dataManager.countProductCaptures(status);

    int offset = 0;
    int limit = 10;
    List<Long> captureIdUpdateList = new ArrayList<Long>();
    while (offset < num) {
       List<ProductCapture> captureList = new ArrayList<ProductCapture>();
       ProductCapture captureProduct = dataManager.selectOneProductCapture(2698l);
       captureList.add(captureProduct);
      //List<ProductCapture> captureList = dataManager.selectProductCaptureList(status, offset, limit);

      for (ProductCapture cProduct : captureList) {
        Long captureId = cProduct.getCaptureId();
        Boolean result = false;
        // int type = cProduct.getType();
        result = processWPPost(cProduct);
        if (result) {
          captureIdUpdateList.add(captureId);
        }
      }
      offset += limit;
      logger.info("Data Transfer from product_capture to wordpress has been completed " + offset + " records!");
    }
    for (Long captureId : captureIdUpdateList) {
      updateProductCaptureStatus(captureId);
    }
  }

  public Boolean processWPPost(ProductCapture cProduct) {
    Boolean result = false;
    // Product
    String name = cProduct.getName();
    WPPost post = new WPPost(cProduct.getContent(), name, name, 0L, "post", "1");
    wpDataManager.savePost(post);
    Long postId = post.getId();

    // Picture&Attachment
    String pictureLink = cProduct.getPictureLink();
    if (pictureLink != null) {
      List<String> pictureList = getPictureList(pictureLink);
      for (String picture : pictureList) {
        FileData fileData = processResourceData(picture, 1, dateHeader);
        if (fileData != null) {
          String fileName = fileData.getFileName();
          String link = fileData.getLink();
          Photo photo = new Photo(link);
          dataManager.savePhoto(photo);

          WPPost picturePost = new WPPost(" ", fileName, fileName, postId, link, "attachment", "image/jpeg");
          wpDataManager.savePost(picturePost);
          Long picturePostId = picturePost.getId();

          String linkFileName = dateHeader + "/" + fileName;
          WPPostMeta postMeta = new WPPostMeta(picturePostId, "_wp_attached_file", linkFileName);
          wpDataManager.savePostMeta(postMeta);
          WPPostMeta postMeta2 =
              new WPPostMeta(picturePostId, "_wp_attachment_metadata", getAttachmentMetaData(fileData.getWidth(), fileData.getHeight(),
                  linkFileName.length(), linkFileName));
          wpDataManager.savePostMeta(postMeta2);
        }
      }
      result = true;
    }
    return result;
  }

  public FileData processResourceData(String pictureUrl, Integer fileType, String destFilePathSuffix) {
    String fileLink = null;
    String localDestFolderPathPrefix = Constants.WP_FILE_STORE_HEADER + destFilePathSuffix;
    String linkUrlPathPrefix = Constants.WPLINK_HEADER + destFilePathSuffix;
    File destFolder = new File(localDestFolderPathPrefix);
    if (!destFolder.exists())
      destFolder.mkdir();

    FileData fileData = null;
    File file = Utils.downloadFromUrl(pictureUrl, localDestFolderPathPrefix);
    if (file != null) {
      String fileName = file.getName();
      String destFilePath = localDestFolderPathPrefix + File.separator + fileName;
      fileLink = linkUrlPathPrefix + "/" + fileName;
      logger.info("SourceLink:" + pictureUrl + ",FileLink:" + fileLink + ",localFilePath:" + destFilePath);
      fileData = new FileData(fileName, fileType, "", fileLink, destFilePath, file.length());
      getImageWidthHeight(file, fileData);
      dataManager.saveResource(fileData);
    }
    return fileData;
  }

  private String getAttachmentMetaData(Integer width, Integer height, Integer fileNameLength, String linkFileName) {
    String metaValue =
        "a:5:{s:5:\"width\";i:"
            + width
            + ";s:6:\"height\";i:"
            + height
            + ";s:4:\"file\";s:"
            + fileNameLength
            + ":\""
            + linkFileName
            + "\";s:5:\"sizes\";a:0:{}s:10:\"image_meta\";a:11:{s:8:\"aperture\";i:0;s:6:\"credit\";s:0:\"\";s:6:\"camera\";s:0:\"\";s:7:\"caption\";s:0:\"\";s:17:\"created_timestamp\";i:0;s:9:\"copyright\";s:0:\"\";s:12:\"focal_length\";i:0;s:3:\"iso\";i:0;s:13:\"shutter_speed\";i:0;s:5:\"title\";s:0:\"\";s:11:\"orientation\";i:0;}}";
    return metaValue;
  }

  private void getImageWidthHeight(File file, FileData fileData) {
    BufferedImage bufimg;
    try {
      bufimg = ImageIO.read(file);
      fileData.setWidth(bufimg.getWidth());
      fileData.setHeight(bufimg.getHeight());
    } catch (IOException e) {
      logger.error("Fail to get image info:", e);
    }
  }

  public static List<String> getPictureList(String pictureLink) {
    List<String> list = new ArrayList<String>();
    Pattern p = Pattern.compile("\\'(.+?)\\'");
    Matcher m = p.matcher(pictureLink);
    while (m.find()) {
      String mg = m.group(1);
      if (!list.contains(mg)) {
        list.add(mg);
      }
    }
    return list;
  }

  private void updateProductCaptureStatus(Long captureId) {
    if (captureId != null && !"".equals(captureId)) {
      dataManager.updateProductCaptureStatus(1, captureId);
      logger.info("Update Product Capture status & captureId:" + captureId);
    }
  }
}
