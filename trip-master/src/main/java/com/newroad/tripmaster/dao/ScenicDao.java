package com.newroad.tripmaster.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.dao.pojo.Scenic;

public class ScenicDao extends BasicDAO<Scenic, ObjectId>{

  protected ScenicDao(Datastore ds) {
    super(ds);
  }
  
  public Scenic detailScenic(String hashsiteId) {
    Query<Scenic> scenicQuery=getDs().find(Scenic.class, DataConstant.HASH_SITE_ID, hashsiteId);
    Scenic scenic=scenicQuery.get();
    return scenic;
  }
  
  public List<Scenic> listChildScenics(String parentScenicId) {
    Query<Scenic> scenicQuery=getDs().find(Scenic.class, "parentid", parentScenicId);
    List<Scenic> sceneicList=scenicQuery.asList();
    return sceneicList;
  }
}
