package com.lenovo.zy.info.crawler.manager.db;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.dao.FileDataDaoIf;
import com.lenovo.zy.info.crawler.dao.PhotoDaoIf;
import com.lenovo.zy.info.crawler.dao.ProductBannerDaoIf;
import com.lenovo.zy.info.crawler.dao.ProductCaptureDaoIf;
import com.lenovo.zy.info.crawler.dao.ProductCategoryDaoIf;
import com.lenovo.zy.info.crawler.dao.ProductDaoIf;
import com.lenovo.zy.info.crawler.dao.ProductTagScopeDaoIf;
import com.lenovo.zy.info.crawler.domain.FileData;
import com.lenovo.zy.info.crawler.domain.Photo;
import com.lenovo.zy.info.crawler.domain.Product;
import com.lenovo.zy.info.crawler.domain.ProductBanner;
import com.lenovo.zy.info.crawler.domain.ProductCapture;
import com.lenovo.zy.info.crawler.domain.ProductCategory;
import com.lenovo.zy.info.crawler.domain.ProductTagScope;

public class ZYProductDao {

  public static Logger logger = LoggerFactory.getLogger(ZYProductDao.class);

  private SqlSession session;

  public ZYProductDao(SqlSession session) {
    this.session = session;
  }

  public int countProductCaptures(int status) {
    ProductCaptureDaoIf productCaptureDao = session.getMapper(ProductCaptureDaoIf.class);
    Integer count = productCaptureDao.countProductCaptures(status);
    session.commit();
    logger.debug("Count product capture list:" + count);
    return count;
  }
  
  public ProductCapture selectOneProductCapture(long id) {
    ProductCaptureDaoIf productCaptureDao = session.getMapper(ProductCaptureDaoIf.class);
    ProductCapture captureProduct = productCaptureDao.selectOneProductCapture(id);
    session.commit();
    logger.debug("Select product capture Count:" + captureProduct);
    return captureProduct;
  }

  public List<ProductCapture> selectProductCaptureList(int status, int offset, int limit) {
    ProductCaptureDaoIf productCaptureDao = session.getMapper(ProductCaptureDaoIf.class);
    List<ProductCapture> productList = productCaptureDao.selectProductCaptures(status, offset, limit);
    session.commit();
    logger.debug("Select product capture list Count:" + productList.size());
    return productList;
  }

  public void updateProductCaptureStatus(int status, long captureId) {
    ProductCaptureDaoIf productCaptureDao = session.getMapper(ProductCaptureDaoIf.class);
    int updateNum = productCaptureDao.updateProductCaptureStatus(status, captureId);
    session.commit();
    logger.debug("Update Product Capture Status:" + updateNum);
  }

  public long saveResource(FileData fileData) {
    FileDataDaoIf fileDataDao = session.getMapper(FileDataDaoIf.class);
    int id = fileDataDao.insertFileData(fileData);
    session.commit();
    logger.debug("Resource Insert Count:" + id);
    return id;
  }

  public long savePhoto(Photo photo) {
    PhotoDaoIf photoDao = session.getMapper(PhotoDaoIf.class);
    int id = photoDao.insertPhoto(photo);
    session.commit();
    logger.debug("Photo Insert Count:" + id);
    return id;
  }

  public long saveProduct(Product product) {
    ProductDaoIf productDao = session.getMapper(ProductDaoIf.class);
    long id = productDao.insertProduct(product);
    session.commit();
    logger.debug("Product Insert Count:" + id);
    return id;
  }

  public long saveProductCategory(ProductCategory productCategory) {
    ProductCategoryDaoIf productCategoryDao = session.getMapper(ProductCategoryDaoIf.class);
    long id = productCategoryDao.insertProductCategory(productCategory);
    session.commit();
    logger.debug("Product Category Insert Count:" + id);
    return id;
  }

  public long saveProductTagScope(Long productId, Long tagId) {
    ProductTagScopeDaoIf productTagScopeDao = session.getMapper(ProductTagScopeDaoIf.class);
    long id = productTagScopeDao.insertProductTagScope(new ProductTagScope(productId, tagId));
    session.commit();
    logger.debug("Product Tag Scope Insert Count:" + id);
    return id;
  }

  public long saveProductBanner(ProductBanner productBanner) {
    ProductBannerDaoIf productBannerDao = session.getMapper(ProductBannerDaoIf.class);
    long id = productBannerDao.insertProductBanner(productBanner);
    session.commit();
    logger.debug("Product Banner Insert Count:" + id);
    return id;
  }
}
