package com.newroad.tripmaster.dao;

import java.util.Map;

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
import com.newroad.tripmaster.dao.pojo.trip.CustomizeRoute;

public class CustomizeRouteDao extends BasicDAO<CustomizeRoute, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(CustomizeRouteDao.class);

  protected CustomizeRouteDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object saveCustomizeRoute(CustomizeRoute customizeRoute) {
    long currentTime = System.currentTimeMillis();
    customizeRoute.setCreateTime(currentTime);
    customizeRoute.setUpdateTime(currentTime);
    try {
      Key<CustomizeRoute> key = getDs().save(customizeRoute);
      return key.getId();
    } catch (Exception ex) {
      logger.error("Fail to save customizeRoute:", ex);
    }
    return null;
  }

  public int updateCustomizeRoute(String tripRouteId, Map<String, Object> updateActionMap) {
    ObjectId objId = new ObjectId(tripRouteId);
    long currentTime = System.currentTimeMillis();
    updateActionMap.put(DataConstant.UPDATE_TIME, currentTime);
    
    Query<CustomizeRoute> routeQuery = getDs().find(CustomizeRoute.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<CustomizeRoute> updateOps = getDs().createUpdateOperations(CustomizeRoute.class);
      for (Map.Entry<String, Object> entry : updateActionMap.entrySet()) {
        if (!"_id".equals(entry.getKey()) && entry.getValue() != null) {
          updateOps.set(entry.getKey(), entry.getValue());
        }
      }
      updateResult = getDs().update(routeQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update customizeRoute:", ex);
    }
    return 0;
  }
  
  public int pushCustomizeRoute(String tripRouteId,String dayRouteMapKey, Map<String, Object> dayRouteMap) {
    ObjectId objId = new ObjectId(tripRouteId);
    Query<CustomizeRoute> routeQuery = getDs().find(CustomizeRoute.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<CustomizeRoute> updateOps = getDs().createUpdateOperations(CustomizeRoute.class);
      updateOps.set(dayRouteMapKey, dayRouteMap);
      updateOps.set(DataConstant.UPDATE_TIME,  System.currentTimeMillis());
      updateResult = getDs().update(routeQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to push customizeRoute:", ex);
    }
    return 0;
  }

  public int updateCustomizeRoute(Map<String, Object> queryParams, Map<String, Object> updateActionMap) {
    Query<CustomizeRoute> routeQuery = getDs().find(CustomizeRoute.class);
    WriteResult updateResult = null;
    try {
      for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
        if (entry.getValue() != null) {
          routeQuery.field(entry.getKey()).equal(entry.getValue());
        }
      }
      
      UpdateOperations<CustomizeRoute> updateOps = getDs().createUpdateOperations(CustomizeRoute.class);
      for (Map.Entry<String, Object> entry : updateActionMap.entrySet()) {
        if (!"_id".equals(entry.getKey()) && entry.getValue() != null) {
          updateOps.set(entry.getKey(), entry.getValue());
        }
      }
      updateResult = getDs().update(routeQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update customizeRoute:", ex);
    }
    return 0;
  }

  public int updateTripRouteStatus(String tripRouteId, Integer status) {
    ObjectId objId = new ObjectId(tripRouteId);
    Query<CustomizeRoute> productQuery = getDs().find(CustomizeRoute.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<CustomizeRoute> updateOps = getDs().createUpdateOperations(CustomizeRoute.class);
      updateOps.set(DataConstant.STATUS, status);
      long currentTime = System.currentTimeMillis();
      updateOps.set(DataConstant.UPDATE_TIME, currentTime);

      updateResult = getDs().update(productQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update trip route status:", ex);
    }
    return 0;
  }

  public CustomizeRoute getCustomizeRoute(String routeId) {
    ObjectId objId = new ObjectId(routeId);
    Query<CustomizeRoute> routeQuery = getDs().find(CustomizeRoute.class).field(Mapper.ID_KEY).equal(objId);
    CustomizeRoute tripRoute = routeQuery.get();
    return tripRoute;
  }
}
