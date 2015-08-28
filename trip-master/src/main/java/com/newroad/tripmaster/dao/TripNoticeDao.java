package com.newroad.tripmaster.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.dao.pojo.trip.TripNotice;

public class TripNoticeDao extends BasicDAO<TripNotice, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(TripNoticeDao.class);

  protected TripNoticeDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object saveTripNotice(TripNotice tripNotice) {
    try {
      Key<TripNotice> key = getDs().save(tripNotice);
      return key.getId();
    } catch (Exception ex) {
      logger.error("Fail to save trip notice:", ex);
    }
    return null;
  }

  public TripNotice getTripNoticeByProduct(String tripProductId) {
    Query<TripNotice> tripNoticeQuery = getDs().find(TripNotice.class).field(DataConstant.TRIP_PRODUCT_ID).equal(tripProductId);
    TripNotice tripNotice = tripNoticeQuery.get();
    return tripNotice;
  }
  
  public List<TripNotice> listTripNoticeByProduct(String tripProductId) {
    Query<TripNotice> tripNoticeQuery = getDs().find(TripNotice.class).field(DataConstant.TRIP_PRODUCT_ID).equal(tripProductId);
    List<TripNotice> noticeList = tripNoticeQuery.asList();
    return noticeList;
  }
}
