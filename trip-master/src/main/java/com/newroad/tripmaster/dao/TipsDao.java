package com.newroad.tripmaster.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.dao.pojo.info.Tips;


public class TipsDao extends BasicDAO<Tips, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(TipsDao.class);

  public static Integer QUERY_LIMIT = 100;

  protected TipsDao(Datastore dataStore) {
    super(dataStore);
  }

  public Object saveTips(Tips tips) {
    Key<Tips> key = getDs().save(tips);
    return key.getId();
  }

  /**
   * @info support or reject tip
   * @param tips
   * @return
   */
  public Object supportTips(Tips tips) {
    ObjectId objId = new ObjectId(tips.getTipsId());
    Query<Tips> tipsQuery = getDs().find(Tips.class).field(Mapper.ID_KEY).equal(objId);
    UpdateOperations<Tips> tipsUpdate = null;
    int type = tips.getSupporttype();
    switch (type) {
      case 1:
        tipsUpdate = getDs().createUpdateOperations(Tips.class).inc("useful", 1);
        break;
      case 2:
        tipsUpdate = getDs().createUpdateOperations(Tips.class).inc("useless", 1);
        break;
    }
    Object obj = getDs().findAndModify(tipsQuery, tipsUpdate);
    return obj;
  }

  public List<Tips> getUserTips(Long userid, String hashsiteid) {
    Query<Tips> tipsQueryArray =
        getDs().find(Tips.class).field(DataConstant.USERID).equal(userid).order("-lastupdatedtime").limit(QUERY_LIMIT);
    if (hashsiteid != null) {
      tipsQueryArray.field(DataConstant.HASH_SITE_ID).equal(hashsiteid);
    }
    List<Tips> tipsList = tipsQueryArray.asList();
    logger.info("User Tips:" + tipsList);
    return tipsList;
  }

  public List<Tips> listScenicTips(String hashsiteid) {
    Query<Tips> tipsQueryArray =
        getDs().find(Tips.class).field(DataConstant.HASH_SITE_ID).equal(hashsiteid).order("-lastupdatedtime").limit(QUERY_LIMIT);
    List<Tips> tipsList = tipsQueryArray.asList();
    logger.info("Scenic Tips:" + tipsList);
    return tipsList;
  }
}
