package com.newroad.tripmaster.info.crawler.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.info.crawler.domain.Site;

public class SiteDao extends BasicDAO<Site, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(SiteDao.class);

  protected SiteDao(Datastore dataStore) {
    super(dataStore);
  }

  public Object updateSitePicture(String hashcode,String picture) {
    Query<Site> siteQuery = getDs().find(Site.class).field("hashcode").equal(hashcode);
    UpdateOperations<Site> siteUpdate = getDs().createUpdateOperations(Site.class).set("picture", picture);
    Site site = getDs().findAndModify(siteQuery, siteUpdate);
    logger.debug("site update hashcode="+hashcode+",picture="+picture);
    return site;
  }


}
