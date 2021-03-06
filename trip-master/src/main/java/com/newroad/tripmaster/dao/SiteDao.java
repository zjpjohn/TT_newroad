package com.newroad.tripmaster.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.constant.QueryConstant;
import com.newroad.tripmaster.dao.pojo.Coordinate;
import com.newroad.tripmaster.dao.pojo.Site;

public class SiteDao extends BasicDAO<Site, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(SiteDao.class);
  
  public static String BLANK="";

  protected SiteDao(Datastore dataStore) {
    super(dataStore);
  }

  public Object saveSite(Site site) {
    Key<Site> key = getDs().save(site);
    return key.getId();
  }

  public Site getSiteByPoint(Coordinate point) {
    Double longitude = point.getLng();
    Double latitude = point.getLat();
    Map<String,Double> map=new HashMap<String,Double>();
    map.put("lng", longitude);
    map.put("lat", latitude);
    String json = JSONConvertor.getJSONInstance().writeValueAsString(map);
    Query<Site> siteQuery = getDs().find(Site.class).field(DataConstant.GRAVITY).equal(json);
    Site site = siteQuery.get();
    return site;
  }

  public Site getSiteByHash(String hashSiteId) {
    Query<Site> siteQuery = getDs().find(Site.class).field(DataConstant.HASH_CODE).equal(hashSiteId);
    Site site = siteQuery.get();
    return site;
  }

  public List<Site> listSites(Coordinate point) {
    Double longitude = point.getLng();
    Double latitude = point.getLat();
    logger.info("Query location coordinate:lng=" + longitude + ",lat=" + latitude);
    // Exception:Legacy point is out of bounds for spherical query
    // You should either change your coordinates to be in range or set spherical parameter to false
    // to avoid getting this error.
    Query<Site> siteQueryArray =
        getDs().find(Site.class).field(DataConstant.PARENT_SITE_ID).equal(BLANK).field(DataConstant.GRAVITY)
            .near(longitude, latitude, point.getDistance(), true).limit(QueryConstant.QUERY_LIMIT);
    List<Site> siteList = siteQueryArray.asList();
    return siteList;
  }
  
  public List<Site> listSites(String parentSiteId) {
    Query<Site> siteQueryArray =
        getDs().find(Site.class).field(DataConstant.PARENT_SITE_ID).equal(parentSiteId).limit(QueryConstant.QUERY_LIMIT);
    List<Site> siteList = siteQueryArray.asList();
    return siteList;
  }

  public List<Site> listSites(Coordinate point, Integer sitetype) {
    Double longitude = point.getLng();
    Double latitude = point.getLat();
    logger.info("Query location coordinate:lng=" + longitude + ",lat=" + latitude + ",sitetyp="
        + sitetype);
    Query<Site> siteQueryArray =
        getDs().find(Site.class).field(DataConstant.SITE_TYPE).equal(sitetype).field(DataConstant.PARENT_SITE_ID).equal(BLANK).
             field(DataConstant.GRAVITY)
            .near(longitude, latitude, point.getDistance(), true).limit(QueryConstant.QUERY_LIMIT);
    List<Site> siteList = siteQueryArray.asList();
    return siteList;
  }
}
