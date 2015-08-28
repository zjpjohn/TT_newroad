package com.newroad.tripmaster.service.scenic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.SiteDao;
import com.newroad.tripmaster.dao.pojo.Coordinate;
import com.newroad.tripmaster.dao.pojo.Site;
import com.newroad.tripmaster.service.SiteServiceIf;
import com.newroad.util.apiresult.ServiceResult;

public class SiteService implements SiteServiceIf {

  private static Logger logger = LoggerFactory.getLogger(SiteService.class);

  private SiteDao siteDao;

  //private ScenicDao scenicDao;

  public ServiceResult<String> saveSite(Site site) {
    ServiceResult<String> result = new ServiceResult<String>();
    int hashcode = site.hashCode();
    site.setHashcode(Integer.valueOf(hashcode).toString());
    long currentTime = System.currentTimeMillis();
    site.setCreatetime(currentTime);
    site.setLastupdatedtime(currentTime);

    Object idObj = siteDao.saveSite(site);
    logger.info("Site object id:" + idObj);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.SITE_ID, idObj);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> listSites(Coordinate point, Integer sitetype) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    if (point.getRadius() >= 0.1) {
      List<Site> siteList = null;
      if (sitetype != null) {
        siteList = siteDao.listSites(point, sitetype);
      } else {
        siteList = siteDao.listSites(point);
      }
      jsonResult = JSONConvertor.transformSites2JSON(siteList);
    } else {
      Site site = siteDao.getSiteByPoint(point);
      jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(site);
    }
    logger.info("Site list:" + jsonResult);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> getSite(String hashSiteId) {
    ServiceResult<String> result = new ServiceResult<String>();
    Site site = siteDao.getSiteByHash(hashSiteId);
    site.setSiteId(null);
    if (site.getSitetype() == 1) {
      List<Site> childSiteList=listSites(site.getHashcode());
      site.setChildSiteList(childSiteList);
    }
    //Scenic scenic = scenicDao.detailScenic(hashSiteId);
    String jsonResult = JSONConvertor.transformSiteSummary2JSON(site);
    result.setBusinessResult(jsonResult);
    return result;
  }
  
  public List<Site> listSites(String parentSiteId) {
    return siteDao.listSites(parentSiteId);
  }

  public void setSiteDao(SiteDao siteDao) {
    this.siteDao = siteDao;
  }

//  public void setScenicDao(ScenicDao scenicDao) {
//    this.scenicDao = scenicDao;
//  }


}
