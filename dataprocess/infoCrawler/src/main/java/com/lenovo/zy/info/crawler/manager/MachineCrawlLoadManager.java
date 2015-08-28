package com.lenovo.zy.info.crawler.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.constants.Constants;
import com.lenovo.zy.info.crawler.domain.FileData;
import com.lenovo.zy.info.crawler.domain.Photo;
import com.lenovo.zy.info.crawler.domain.Product;
import com.lenovo.zy.info.crawler.domain.ProductBanner;
import com.lenovo.zy.info.crawler.domain.ProductCapture;
import com.lenovo.zy.info.crawler.domain.ProductCategory;
import com.lenovo.zy.info.crawler.manager.db.SqlSessionManager;
import com.lenovo.zy.info.crawler.manager.db.ZYProductDao;

public class MachineCrawlLoadManager {

  public static Logger logger = LoggerFactory.getLogger(MachineCrawlLoadManager.class);

  private ZYProductDao dataManager;

  private String dateHeader;

  public MachineCrawlLoadManager(String dateHeader) {
    super();
    this.dataManager = new ZYProductDao(SqlSessionManager.getSqlSession());
    this.dateHeader = dateHeader;
  }

  public void loadWebCrawlData() {
    int status = 0;
    int num = dataManager.countProductCaptures(status);

    int offset = 0;
    int limit = 10;
    List<Long> captureIdUpdateList = new ArrayList<Long>();
    while (offset < num) {
      List<ProductCapture> captureList = dataManager.selectProductCaptureList(status, offset, limit);
      // processProductData(captureList.get(0));
      for (ProductCapture cProduct : captureList) {
        Long captureId = cProduct.getCaptureId();
        Boolean result = false;
        int type = cProduct.getType();
        switch (type) {
          case 1:
            result = processProductData(cProduct);
            break;
          case 2:
            result = processArticleData(cProduct);
            break;
          case 3:
            break;
        }
        if (result) {
          captureIdUpdateList.add(captureId);
        }
      }
      offset += limit;
      logger.info("Data Transfer from product_capture to zy_product has been completed " + offset + " records!");
    }
    for (Long captureId : captureIdUpdateList) {
      updateProductCaptureStatus(captureId);
    }
  }

  public String processResourceData(String pictureUrl, Integer fileType, String destFilePathSuffix) {
    String fileLink = null;
    String localDestFolderPathPrefix = Constants.ZY_FILE_STORE_HEADER + destFilePathSuffix;
    String linkUrlPathPrefix = Constants.LINK_HEADER + destFilePathSuffix;
    File destFolder = new File(localDestFolderPathPrefix);
    if (!destFolder.exists())
      destFolder.mkdir();

    File file = Utils.downloadFromUrl(pictureUrl, localDestFolderPathPrefix);
    if (file != null) {
      String fileName = file.getName();
      String destFilePath = localDestFolderPathPrefix + File.separator + fileName;
      fileLink = linkUrlPathPrefix + "/" + fileName;
      logger.info("SourceLink:"+pictureUrl+",FileLink:" + fileLink + ",localFilePath:" + destFilePath);
      FileData fileData = new FileData(fileName, fileType, "", fileLink, destFilePath, file.length());
      dataManager.saveResource(fileData);
    }
    return fileLink;
  }

  public Boolean processProductData(ProductCapture cProduct) {
    Boolean result = false;
    Product product = null;
    String productName = cProduct.getName();
    String summary = "";
    Long categoryCode = 37L;
    Long publishUserId = 186842l;
    String content = cProduct.getContent();

    Map<Long, String> pictureMap = new TreeMap<Long, String>();
    product = new Product(productName, "0", summary, replaceContentLink(content), publishUserId);
    product.setCategoryId(categoryCode);
    product.setSource(cProduct.getLink());
    String pictureLink = cProduct.getPictureLink();

    if (pictureLink != null) {
      String fileLink = processResourceData(pictureLink, 1, dateHeader);
      if (fileLink != null) {
        Photo photo = new Photo(fileLink);
        dataManager.savePhoto(photo);
        pictureMap.put(photo.getPhotoId(), fileLink);
        // product.setProductLink(pictureLink);
      }
    }

    dataManager.saveProduct(product);
    Long productId = product.getProductId();
    logger.info("Insert Product into zy DB & productId:" + productId);
    if (productId != 0l) {
      dataManager.saveProductCategory(new ProductCategory(categoryCode, productId));
      dataManager.saveProductTagScope(productId, 9l);
      if (pictureMap != null && pictureMap.size() > 0) {
        Set<Entry<Long, String>> bannerSet = pictureMap.entrySet();
        Iterator<Entry<Long, String>> iter = bannerSet.iterator();
        while (iter.hasNext()) {
          Entry<Long, String> entry = iter.next();
          dataManager.saveProductBanner(new ProductBanner(productId, entry.getKey()));
        }
      }
      result = true;
    }
    return result;
  }

  public String replaceContentLink(String content) {
    Pattern pattern = Pattern.compile("<\\s*img\\s*(?:[^>]*)src\\s*=\\s*([^>]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(content);
    StringBuffer sb = new StringBuffer();
    while (matcher.find()) {
      String matchInfo = matcher.group(1).replace("\"", "");
      String fileLink = processResourceData(matchInfo, 3, dateHeader);
      // String fileLink = "link";
      if (fileLink != null) {
        String htmlLink = "<img src=\"" + fileLink + "\"";
        matcher.appendReplacement(sb, htmlLink);
      }
    }
    matcher.appendTail(sb);
    // logger.info("ProductContent:"+sb.toString());
    return sb.toString();
  }

  public Boolean processArticleData(ProductCapture cProduct) {
    return null;
  }

  private void updateProductCaptureStatus(Long captureId) {
    if (captureId != null && !"".equals(captureId)) {
      dataManager.updateProductCaptureStatus(1, captureId);
      logger.info("Update Product Capture status & captureId:" + captureId);
    }
  }

  public static void main(String[] args) {
    MachineCrawlLoadManager machineManager = new MachineCrawlLoadManager("test");
    String content =
        "<p>上市日期：2015年</p><p>价格：170欧元（折合RMB 1149元 ）</p><p>类型：智能手机</p><p>颜色：黑色</p><p>实际上线时间：关注量超过500时将有机会随时进入闪电特卖环节。具体上线日期待定，以实际页面说明为准。</p><p><br></p><p>Canonical Ubuntu智能手机采用基于手势的用户界面-Scope，其中含有采用HTML5构建内容卡片，支持用户定制，这种设计节省了用户开启不同应用程序获得信息的时间。开机后看到的第一个画面是显示日期的Scope卡片，其中包括今天的天气和日历中的任何即将发生事件，日期卡片左右还有额外的主题，比如附近的餐馆信息卡片，这些额外主题卡片也支持用户定制。</p><p><br></p><p>用户还可以通过主题列表，指定卡片排序的规则，例如， \"我饿了\"，那么Canonical Ubuntu智能手机就会自动在卡片当中显示附近的餐厅。</p><p><br></p><p>目前，Ubuntu移动平台并没有很多的应用程序，总计在1000个左右。所以是建立一个用户界面智能提供应用程序，可以行之有效的工作。 Canonical公司表示，它自己的开发者社区也开始移植应用程序，比如将Android版本Evernote成功移植到Canonical Ubuntu智能手机。Canonical公司表示，它要求它的社区可以有5名志愿者开发或者移植应用程序，100名志愿者主动接听电话。</p><h3><img src=\"http://aliyun.demohour.com/project_photos-files-000-097-221-97221-large.jpg?1425979305\"><br><img src=\"http://aliyun.demohour.com/project_photos-files-000-097-222-97222-large.jpg?1425979322\"><br><br></h3>";
    machineManager.replaceContentLink(content);
  }

}
