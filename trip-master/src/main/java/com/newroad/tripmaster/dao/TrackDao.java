package com.newroad.tripmaster.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.dao.pojo.info.UserBehavior;

public class TrackDao extends BasicDAO<TrackDao, ObjectId>{
  private static Logger logger = LoggerFactory.getLogger(TrackDao.class);
  
  protected TrackDao(Datastore dataStore) {
    super(dataStore);
  }

  public Object saveUserBehavior(UserBehavior userBehavior) {
    Key<UserBehavior> key = getDs().save(userBehavior);
    return key.getId();
  }

}
