package com.newroad.tripmaster.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.ReadPreference;
import com.mongodb.WriteResult;
import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.dao.pojo.trip.POIRoute;
import com.newroad.tripmaster.dao.pojo.trip.TravelDayPOIComparator;
import com.newroad.tripmaster.dao.pojo.trip.TripDayRoute;
import com.newroad.tripmaster.util.BeanDBObjectUtils;

public class TripPOIRouteDao extends BasicDAO<POIRoute, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(TripPOIRouteDao.class);

  protected TripPOIRouteDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object saveTripPOIRoute(POIRoute poiRoute) {
    long currentTime = System.currentTimeMillis();
    poiRoute.setCreateTime(currentTime);
    poiRoute.setUpdateTime(currentTime);
    try {
      Key<POIRoute> key = getDs().save(poiRoute);
      return key.getId();
    } catch (Exception ex) {
      logger.error("Fail to save POI route:", ex);
    }
    return null;
  }

  public POIRoute getTripPOIRoute(String poiRouteId) {
    ObjectId objId = new ObjectId(poiRouteId);
    Query<POIRoute> poiRouteQuery = getDs().find(POIRoute.class).field(Mapper.ID_KEY).equal(objId);
    POIRoute poiRoute = poiRouteQuery.get();
    return poiRoute;
  }

  public int updatePOIRoute(String poiRouteId, Map<String, Object> updateActionMap) {
    ObjectId objId = new ObjectId(poiRouteId);
    long currentTime = System.currentTimeMillis();
    updateActionMap.put(DataConstant.UPDATE_TIME, currentTime);
    
    Query<POIRoute> poiRouteQuery = getDs().find(POIRoute.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<POIRoute> updateOps = getDs().createUpdateOperations(POIRoute.class);
      for (Map.Entry<String, Object> entry : updateActionMap.entrySet()) {
        if (!"_id".equals(entry.getKey()) && entry.getValue() != null) {
          updateOps.set(entry.getKey(), entry.getValue());
        }
      }
      updateResult = getDs().update(poiRouteQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update POI route:", ex);
    }
    return 0;
  }
  
  public int updatePOIRoute(Map<String,Object> queryParams, Map<String, Object> updateActionMap) {
    Query<POIRoute> poiRouteQuery = getDs().find(POIRoute.class);
    WriteResult updateResult = null;
    try {
      for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
        if (!"_id".equals(entry.getKey()) && entry.getValue() != null) {
          poiRouteQuery.field(entry.getKey()).equal(entry.getValue());
        }
      }
      
      UpdateOperations<POIRoute> updateOps = getDs().createUpdateOperations(POIRoute.class);
      for (Map.Entry<String, Object> entry : updateActionMap.entrySet()) {
        if (entry.getValue() != null) {
          updateOps.set(entry.getKey(), entry.getValue());
        }
      }
      updateResult = getDs().update(poiRouteQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update POI route:", ex);
    }
    return 0;
  }

  public int updatePOIRouteStatus(String poiRouteId, Integer status) {
    ObjectId objId = new ObjectId(poiRouteId);
    Query<POIRoute> productQuery = getDs().find(POIRoute.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<POIRoute> updateOps = getDs().createUpdateOperations(POIRoute.class);
      updateOps.set(DataConstant.STATUS, status);
      long currentTime = System.currentTimeMillis();
      updateOps.set(DataConstant.UPDATE_TIME, currentTime);

      updateResult = getDs().update(productQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update poi route status:", ex);
    }
    return 0;
  }

  public List<POIRoute> listTripPOIRoutes(String userRouteId, Integer status) {
    Query<POIRoute> poiRouteQuery =
        getDs().find(POIRoute.class).field(DataConstant.TRIP_ROUTE_ID).equal(userRouteId).field(DataConstant.STATUS).equal(status);
    List<POIRoute> poiRouteList = poiRouteQuery.asList();
    Collections.sort(poiRouteList);
    return poiRouteList;
  }

  public TripDayRoute getTripDayRoute(String userRouteId, String roundDay) {
    TripDayRoute dayRoute = null;
    Query<POIRoute> poiRouteQuery =
        getDs().find(POIRoute.class).field(DataConstant.TRIP_ROUTE_ID).equal(userRouteId).field(DataConstant.ROUTE_DAY).equal(roundDay);
    List<POIRoute> poiRouteList = poiRouteQuery.asList();
    if (poiRouteList != null && poiRouteList.size() > 0) {
      dayRoute = new TripDayRoute(userRouteId, poiRouteList);
    }
    return dayRoute;
  }

  public SortedSet<POIRoute> aggregateTripDayPOI(String tripRouteId) {
    DBCollection collection = getDs().getCollection(POIRoute.class);
    List<DBObject> dbObjectList = new ArrayList<DBObject>();
    DBObject fields = new BasicDBObject(DataConstant.TRIP_ROUTE_ID, tripRouteId);
    fields.put(DataConstant.TRAVELPOI_TRAVELPOID, new BasicDBObject("$exists", 1));
    DBObject match = new BasicDBObject("$match", fields);
    dbObjectList.add(match);

    DBObject fields2 = new BasicDBObject(DataConstant.TRAVELPOI_TRAVELPOID, 1);
    fields2.put(DataConstant.TRAVELPOI_POINAME, 1);
    fields2.put("routeDay", 1);
    DBObject project = new BasicDBObject("$project", fields2);
    dbObjectList.add(project);

    ReadPreference preference = ReadPreference.secondaryPreferred();
    collection.setReadPreference(preference);

    AggregationOutput aggregate = collection.aggregate(dbObjectList);

    SortedSet<POIRoute> routeSet = new TreeSet<POIRoute>(new TravelDayPOIComparator());
    Iterable<DBObject> iter = aggregate.results();
    Iterator<DBObject> iterator = iter.iterator();
    while (iterator.hasNext()) {
      DBObject obj = iterator.next();
      POIRoute poiRoute = new POIRoute();
      BeanDBObjectUtils.dbObject2Bean(obj, poiRoute);
      routeSet.add(poiRoute);
    }
    return routeSet;
  }

}
