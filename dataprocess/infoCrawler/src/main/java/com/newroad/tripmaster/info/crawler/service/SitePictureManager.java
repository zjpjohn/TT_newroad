package com.newroad.tripmaster.info.crawler.service;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.info.crawler.SpringContextUtil;
import com.newroad.tripmaster.info.crawler.dao.ScenicDao;
import com.newroad.tripmaster.info.crawler.dao.SiteDao;
import com.newroad.tripmaster.info.crawler.domain.Scenic;

public class SitePictureManager {
  
  private static Logger logger = LoggerFactory.getLogger(SitePictureManager.class);

  public boolean updateSitePictureInfo() {
    boolean result = false;
    ScenicDao scenicDao = SpringContextUtil.getBean(ScenicDao.class);
    SiteDao siteDao=SpringContextUtil.getBean(SiteDao.class);
    Iterator<Scenic> queryresult= scenicDao.getDs().find(Scenic.class).iterator();
    while(queryresult.hasNext()){
      Scenic scenic=queryresult.next();
      logger.debug("scenic name:"+scenic.getName());
      siteDao.updateSitePicture(scenic.getHashsiteid(), scenic.getThumbpic());
    }
    return result;
  }
}
