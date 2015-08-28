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

import com.mongodb.WriteResult;
import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.QueryConstant;
import com.newroad.tripmaster.dao.pojo.info.UserBehavior;

public class UserBehaviorDao extends BasicDAO<UserBehavior, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(UserBehaviorDao.class);

  protected UserBehaviorDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object saveUserBehavior(UserBehavior userBehavior) {
    long currentTime = System.currentTimeMillis();
    userBehavior.setCreateTime(currentTime);
    userBehavior.setUpdateTime(currentTime);
    try {
      Key<UserBehavior> key = getDs().save(userBehavior);
      return key.getId();
    } catch (Exception ex) {
      logger.error("Fail to save UserBehavior:", ex);
    }
    return null;
  }

  public Integer updateBehaviorActionType(String targetId, Integer queryType, Long userId, Integer updateType) {
    Query<UserBehavior> targetQuery = getDs().find(UserBehavior.class).field(DataConstant.ACTION_TYPE).equal(queryType);
    targetQuery.field(DataConstant.TARGET_ID).equal(targetId);
    targetQuery.field(DataConstant.USER_ID).equal(userId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<UserBehavior> updateOps = getDs().createUpdateOperations(UserBehavior.class);
      updateOps.set(DataConstant.ACTION_TYPE, updateType);
      long currentTime = System.currentTimeMillis();
      updateOps.set(DataConstant.UPDATE_TIME, currentTime);

      updateResult = getDs().update(targetQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update behavior action type:", ex);
    }
    return 0;
  }


  public Integer updateBehaviorStatus(String behaviorId, Integer type, String targetId, Integer status) {
    ObjectId objId = new ObjectId(behaviorId);
    Query<UserBehavior> targetQuery = getDs().find(UserBehavior.class).field(Mapper.ID_KEY).equal(objId);
    targetQuery.field(DataConstant.TARGET_ID).equal(targetId).field(DataConstant.ACTION_TYPE).equal(type);
    WriteResult updateResult = null;
    try {
      UpdateOperations<UserBehavior> updateOps = getDs().createUpdateOperations(UserBehavior.class);
      updateOps.set(DataConstant.STATUS, status);
      long currentTime = System.currentTimeMillis();
      updateOps.set(DataConstant.UPDATE_TIME, currentTime);

      updateResult = getDs().update(targetQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update behavior status:", ex);
    }
    return 0;
  }

  public List<UserBehavior> listUserBehaviors(Long userId, Integer actionType, String targetId, Integer status) {
    Query<UserBehavior> userBehaviorArray =
        getDs().find(UserBehavior.class).field(DataConstant.USER_ID).equal(userId).field(DataConstant.ACTION_TYPE).equal(actionType)
            .field(DataConstant.STATUS).equal(status);
    if (targetId != null) {
      userBehaviorArray.field(DataConstant.TARGET_ID).equal(targetId);
    }
    userBehaviorArray.limit(QueryConstant.QUERY_LIMIT).order("-" + DataConstant.CREATE_TIME);
    List<UserBehavior> behaviorList = userBehaviorArray.asList();
    return behaviorList;
  }

  public List<UserBehavior> listUserBehaviorTarget(Long userId, Integer actionType, Integer status) {
    Query<UserBehavior> userBehaviorArray =
        getDs().find(UserBehavior.class).field(DataConstant.STATUS).equal(status).field(DataConstant.USER_ID).equal(userId)
            .field(DataConstant.ACTION_TYPE).equal(actionType);
    userBehaviorArray.limit(QueryConstant.QUERY_LIMIT).order("-" + DataConstant.CREATE_TIME);
    userBehaviorArray.retrievedFields(true, DataConstant.TARGET_ID, DataConstant.ACTION_TYPE, DataConstant.USER_ID);
    List<UserBehavior> targetList = userBehaviorArray.asList();
    return targetList;
  }

}
