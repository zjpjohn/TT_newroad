package com.newroad.tripmaster.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.WriteResult;
import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.QueryConstant;
import com.newroad.tripmaster.dao.pojo.trip.DateInfo;
import com.newroad.tripmaster.dao.pojo.trip.TravelDateUnit;

public class TravelDateUnitDao extends BasicDAO<TravelDateUnit, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(TravelDateUnitDao.class);

  protected TravelDateUnitDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object saveTravelDateUnit(TravelDateUnit travelDateUnit) {
    long currentTime = System.currentTimeMillis();
    travelDateUnit.setUpdateTime(currentTime);
    try {
      Key<TravelDateUnit> key = getDs().save(travelDateUnit);
      return key.getId();
    } catch (Exception ex) {
      logger.error("Fail to save travel date unit:", ex);
    }
    return null;
  }

  public List<TravelDateUnit> listTravelDateUnit(String tripProductId, String yearMonth, Integer status) {
    Query<TravelDateUnit> travelDateQueryArray =
        getDs().find(TravelDateUnit.class).field(DataConstant.TRIP_PRODUCT_ID).equal(tripProductId);
    if (yearMonth != null) {
      travelDateQueryArray.field(DataConstant.YEAR_MONTH).equal(yearMonth);
    }
    travelDateQueryArray.field(DataConstant.STATUS).equal(status);
    travelDateQueryArray.limit(QueryConstant.QUERY_LIMIT).order("-" + DataConstant.UPDATE_TIME);
    List<TravelDateUnit> travelDateList = travelDateQueryArray.asList();
    return travelDateList;
  }

  public TravelDateUnit getTravelDateUnit(String tripProductId, String yearMonth, Integer status) {
    Query<TravelDateUnit> travelDateQueryArray =
        getDs().find(TravelDateUnit.class).field(DataConstant.TRIP_PRODUCT_ID).equal(tripProductId);
    travelDateQueryArray.field(DataConstant.YEAR_MONTH).equal(yearMonth);
    travelDateQueryArray.field(DataConstant.STATUS).equal(status);
    TravelDateUnit travelDateUnit = travelDateQueryArray.get();
    return travelDateUnit;
  }

  public int updateTravelDateUnit(String tripProductId, String yearMonth, List<DateInfo> dateList) {
    long currentTime = System.currentTimeMillis();

    Query<TravelDateUnit> travelDateQuery =
        getDs().find(TravelDateUnit.class).field(DataConstant.TRIP_PRODUCT_ID).equal(tripProductId).field(DataConstant.YEAR_MONTH)
            .equal(yearMonth);
    WriteResult updateResult = null;
    try {
      UpdateOperations<TravelDateUnit> updateOps = getDs().createUpdateOperations(TravelDateUnit.class);
      updateOps.set(DataConstant.DATE_LIST, dateList);
      updateOps.set(DataConstant.UPDATE_TIME, currentTime);
      updateResult = getDs().update(travelDateQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update travel date info:", ex);
    }
    return 0;
  }

  public int updateTravelDateUnitStatus(String tripProductId, String yearMonth, Integer status) {
    long currentTime = System.currentTimeMillis();

    Query<TravelDateUnit> travelDateQuery = getDs().find(TravelDateUnit.class).field(DataConstant.TRIP_PRODUCT_ID).equal(tripProductId);
    if (yearMonth != null) {
      travelDateQuery.field(DataConstant.YEAR_MONTH).equal(yearMonth);
    }
    WriteResult updateResult = null;
    try {
      UpdateOperations<TravelDateUnit> updateOps = getDs().createUpdateOperations(TravelDateUnit.class);
      updateOps.set(DataConstant.STATUS, status);
      updateOps.set(DataConstant.UPDATE_TIME, currentTime);
      updateResult = getDs().update(travelDateQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update travel date status:", ex);
    }
    return 0;
  }

  public int updateTravelDateUnit(String tripProductId, String yearMonth, List<DateInfo> dateList, Integer status, Boolean upsert) {
    long currentTime = System.currentTimeMillis();

    Query<TravelDateUnit> travelDateQuery =
        getDs().find(TravelDateUnit.class).field(DataConstant.TRIP_PRODUCT_ID).equal(tripProductId).field(DataConstant.YEAR_MONTH)
            .equal(yearMonth);
    WriteResult updateResult = null;
    try {
      UpdateOperations<TravelDateUnit> updateOps = getDs().createUpdateOperations(TravelDateUnit.class);
      updateOps.set(DataConstant.DATE_LIST, dateList);
      updateOps.set(DataConstant.STATUS, status);
      updateOps.set(DataConstant.UPDATE_TIME, currentTime);
      updateResult = getDs().update(travelDateQuery, updateOps, upsert).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update travel date info:", ex);
    }
    return 0;
  }

}
