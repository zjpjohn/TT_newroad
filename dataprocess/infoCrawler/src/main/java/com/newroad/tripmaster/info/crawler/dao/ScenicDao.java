package com.newroad.tripmaster.info.crawler.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.newroad.tripmaster.info.crawler.domain.Scenic;


public class ScenicDao extends BasicDAO<Scenic, ObjectId>{

  protected ScenicDao(Datastore ds) {
    super(ds);
  }
   
  public Object saveScenic(Scenic scenic) {
    Key<Scenic> key = getDs().save(scenic);
    return key.getId();
  }
  
  public Scenic detailScenic(String siteId) {
    Query<Scenic> scenicQuery=getDs().find(Scenic.class, "hashsiteid", siteId);
    Scenic scenic=scenicQuery.get();
    return scenic;
  }
  
  public List<Scenic> listChildScenics(String parentScenicId) {
    Query<Scenic> scenicQuery=getDs().find(Scenic.class, "parentid", parentScenicId);
    List<Scenic> sceneicList=scenicQuery.asList();
    return sceneicList;
  }
}
