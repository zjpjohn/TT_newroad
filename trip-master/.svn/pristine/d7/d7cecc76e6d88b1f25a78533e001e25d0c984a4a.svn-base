package com.newroad.tripmaster.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.ReadPreference;
import com.mongodb.WriteResult;
import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.QueryConstant;
import com.newroad.tripmaster.dao.pojo.trip.Count;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;

public class TripProductDao extends BasicDAO<TripProduct, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(TripProductDao.class);

  protected TripProductDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object saveTripProduct(TripProduct tripProduct) {
    long currentTime = System.currentTimeMillis();
    tripProduct.setCreateTime(currentTime);
    tripProduct.setUpdateTime(currentTime);
    try {
      Key<TripProduct> key = getDs().save(tripProduct);
      return key.getId();
    } catch (Exception ex) {
      logger.error("Fail to save trip product:", ex);
    }
    return null;
  }

  public int updateTripProduct(String tripProductId, Map<String, Object> updateActionMap) {
    ObjectId objId = new ObjectId(tripProductId);
    long currentTime = System.currentTimeMillis();
    updateActionMap.put(DataConstant.UPDATE_TIME, currentTime);

    Query<TripProduct> productQuery = getDs().find(TripProduct.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<TripProduct> updateOps = getDs().createUpdateOperations(TripProduct.class);
      for (Map.Entry<String, Object> entry : updateActionMap.entrySet()) {
        if (!"_id".equals(entry.getKey()) && entry.getValue() != null) {
          updateOps.set(entry.getKey(), entry.getValue());
        }
      }
      updateResult = getDs().update(productQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update trip product:", ex);
    }
    return 0;
  }

  public int updateTripProductStatus(String tripProductId, Integer status) {
    ObjectId objId = new ObjectId(tripProductId);
    Query<TripProduct> productQuery = getDs().find(TripProduct.class).field(Mapper.ID_KEY).equal(objId);
    WriteResult updateResult = null;
    try {
      UpdateOperations<TripProduct> updateOps = getDs().createUpdateOperations(TripProduct.class);
      updateOps.set(DataConstant.STATUS, status);
      long currentTime = System.currentTimeMillis();
      updateOps.set(DataConstant.UPDATE_TIME, currentTime);

      updateResult = getDs().update(productQuery, updateOps).getWriteResult();
      return updateResult.getN();
    } catch (Exception ex) {
      logger.error("Fail to update trip product status:", ex);
    }
    return 0;
  }

  // unused
  public Object deleteTripProduct(String tripProductId, Integer status) {
    ObjectId objId = new ObjectId(tripProductId);
    Query<TripProduct> productQuery = getDs().find(TripProduct.class).field(Mapper.ID_KEY).equal(objId);
    try {
      WriteResult writeResult = getDs().delete(productQuery);
      return writeResult.getUpsertedId();
    } catch (Exception ex) {
      logger.error("Fail to delete trip product:", ex);
    }
    return null;
  }

  public List<TripProduct> listTripProducts(String cityCode, Integer status) {
    Query<TripProduct> productQueryArray = getDs().find(TripProduct.class).field(DataConstant.STATUS).equal(status);
    if (cityCode != null && !"0".equals(cityCode) && !"".equals(cityCode)) {
      productQueryArray.field(DataConstant.USER_START_CITY).equal(cityCode);
    }
    productQueryArray.limit(QueryConstant.QUERY_LIMIT).order("-" + DataConstant.UPDATE_TIME);
    List<TripProduct> productList = productQueryArray.asList();
    return productList;
  }

  public List<TripProduct> listTripProductByUser(Long luckerId, Integer status, Integer start, Integer size) {
    Query<TripProduct> productQueryArray = null;
    List<TripProduct> productList = null;
    productQueryArray = getDs().find(TripProduct.class);
    if (status < 0) {
      productQueryArray.field(DataConstant.STATUS).greaterThan(0);
    } else {
      productQueryArray.field(DataConstant.STATUS).equal(status);
    }
    if (luckerId != null && luckerId > 0) {
      productQueryArray.field(DataConstant.LUCKER_ID).equal(luckerId);
    }
    productQueryArray.offset(start).limit(size);
    productList = productQueryArray.asList();
    return productList;
  }

  public List<TripProduct> listAllTripProducts(Integer start, Integer size) {
    Query<TripProduct> productQueryArray = null;
    List<TripProduct> productList = null;
    productQueryArray = getDs().find(TripProduct.class).field(DataConstant.STATUS).notEqual(0);
    productQueryArray.offset(start).limit(size);
    productList = productQueryArray.asList();
    return productList;
  }

  public Long countTripProduct(Long luckerId) {
    Long productCount = 0l;
    Query<TripProduct> productQueryArray = null;
    if (luckerId != null && luckerId > 0) {
      productQueryArray = getDs().find(TripProduct.class).field(DataConstant.LUCKER_ID).equal(luckerId);
      productCount = getDs().getCount(productQueryArray);
    } else {
      productCount = getDs().getCount(TripProduct.class);
    }
    return productCount;
  }

  public List<Count> aggregateCountTripProduct(Long luckerId) {
    DBCollection collection = getDs().getCollection(TripProduct.class);
    List<DBObject> dbObjectList = new ArrayList<DBObject>();
    if (luckerId != null) {
      DBObject fields = new BasicDBObject(DataConstant.LUCKER_ID, luckerId);
      DBObject match = new BasicDBObject("$match", fields);
      dbObjectList.add(match);
    }

    DBObject fields2 = new BasicDBObject("_id", "$status");
    fields2.put("count", new BasicDBObject("$sum", 1));
    DBObject project = new BasicDBObject("$group", fields2);
    dbObjectList.add(project);

    ReadPreference preference = ReadPreference.secondaryPreferred();
    collection.setReadPreference(preference);

    AggregationOutput aggregate = collection.aggregate(dbObjectList);

    List<Count> countList = new ArrayList<Count>();
    Iterable<DBObject> iter = aggregate.results();
    Iterator<DBObject> iterator = iter.iterator();
    while (iterator.hasNext()) {
      DBObject obj = iterator.next();
      Count count = new Count(obj.get("_id"), (Integer) obj.get("count"));
      countList.add(count);
    }
    return countList;
  }


  public TripProduct getTripProduct(String productId) {
    ObjectId objId = new ObjectId(productId);
    Query<TripProduct> productQuery = getDs().find(TripProduct.class).field(Mapper.ID_KEY).equal(objId);
    TripProduct tripProduct = productQuery.get();
    return tripProduct;
  }


}
