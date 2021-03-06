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
import com.newroad.tripmaster.dao.pojo.trip.TravelPOI;

public class TravelPOIDao extends BasicDAO<TravelPOI, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(TravelPOIDao.class);

  protected TravelPOIDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object saveTravelPOI(TravelPOI travelPOI) {
    try {
      Key<TravelPOI> key = getDs().save(travelPOI);
      return key.getId();
    } catch (Exception ex) {
      logger.error("Fail to save travel POI:", ex);
    }
    return null;
  }

  public TravelPOI getTravelPOI(String travelPOId) {
    ObjectId objId = new ObjectId(travelPOId);
    Query<TravelPOI> poiQuery = getDs().find(TravelPOI.class).field(Mapper.ID_KEY).equal(objId);
    TravelPOI poi = poiQuery.get();
    return poi;
  }

  public int updateTravelPOI(String travelPOIId, Map<String, Object> updateActionMap) {
    ObjectId objId = new ObjectId(travelPOIId);
    Query<TravelPOI> poiQuery = getDs().find(TravelPOI.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<TravelPOI> updateOps = getDs().createUpdateOperations(TravelPOI.class);
      for (Map.Entry<String, Object> entry : updateActionMap.entrySet()) {
        if (!"_id".equals(entry.getKey()) && entry.getValue() != null) {
          updateOps.set(entry.getKey(), entry.getValue());
        }
      }
      updateResult = getDs().update(poiQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update travel POI:", ex);
    }
    return 0;
  }
  
  public Object updateTripRouteStatus(String tripRouteId, Integer status) {
    ObjectId objId = new ObjectId(tripRouteId);
    Query<TravelPOI> productQuery = getDs().find(TravelPOI.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<TravelPOI> updateOps = getDs().createUpdateOperations(TravelPOI.class);
      updateOps.set(DataConstant.STATUS, status);

      updateResult = getDs().update(productQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update travel POI status:", ex);
    }
    return 0;
  }
}
