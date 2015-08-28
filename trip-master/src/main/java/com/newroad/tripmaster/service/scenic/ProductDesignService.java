package com.newroad.tripmaster.service.scenic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.CustomizeRouteDao;
import com.newroad.tripmaster.dao.TravelPOIDao;
import com.newroad.tripmaster.dao.TripNoticeDao;
import com.newroad.tripmaster.dao.TripPOIRouteDao;
import com.newroad.tripmaster.dao.TripProductDao;
import com.newroad.tripmaster.dao.pojo.trip.Count;
import com.newroad.tripmaster.dao.pojo.trip.CustomizeRoute;
import com.newroad.tripmaster.dao.pojo.trip.POIRoute;
import com.newroad.tripmaster.dao.pojo.trip.TravelPOI;
import com.newroad.tripmaster.dao.pojo.trip.TripNotice;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;
import com.newroad.tripmaster.service.ProductDesignServiceIf;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.apiresult.ServiceResult;

public class ProductDesignService implements ProductDesignServiceIf {

  private static Logger logger = LoggerFactory.getLogger(ProductDesignService.class);

  private TripProductDao tripProductDao;

  private TripPOIRouteDao tripPOIRouteDao;

  private CustomizeRouteDao customizeRouteDao;

  private TravelPOIDao travelPOIDao;

  private TripNoticeDao tripNoticeDao;

  private CommonService commonService;

  public ServiceResult<String> createTripProduct(TripProduct tripProduct) {
    ServiceResult<String> result = new ServiceResult<String>();
    tripProduct.setStatus(2);

    Object idObj = tripProductDao.saveTripProduct(tripProduct);
    logger.info("TripProduct create objectId:" + idObj + "in pre-submit status!");
    if (idObj == null) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to create trip product because ObjectId is null!");
      return result;
    }
    String tripProductId = idObj.toString();
    Map<String, Object> tripRouteMap = createCustomizeRoute(new CustomizeRoute(tripProduct.getProductName(), tripProduct.getLuckerId()));
    if (tripRouteMap == null) {
      logger.error("Fail to create trip route because tripRouteId is null!");
    } else {
      updateTripProduct(tripProductId, tripRouteMap);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRIP_PRODUCT_ID, tripProductId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> updateTripProduct(String tripProductId, Map<String, Object> updateActionMap) {
    ServiceResult<String> result = new ServiceResult<String>();
    int updateCount = tripProductDao.updateTripProduct(tripProductId, updateActionMap);
    logger.info("Update TripProduct objectId count:" + updateCount);
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to update trip product because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRIP_PRODUCT_ID, tripProductId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> updateTripProductStatus(String tripProductId, Integer status) {
    ServiceResult<String> result = new ServiceResult<String>();

    int updateCount = tripProductDao.updateTripProductStatus(tripProductId, status);
    logger.info("Update TripProduct objectId count:" + updateCount + " status=" + status);
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to update trip product status because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRIP_PRODUCT_ID, tripProductId);
    map.put(DataConstant.STATUS, status);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> listUserTripProduct(Long luckerId, Integer userRole, Integer start, Integer size) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    List<TripProduct> tripProductList = null;
    List<Count> countList = null;
    if (userRole == 1) {
      tripProductList = tripProductDao.listAllTripProducts(start, size);
      countList = tripProductDao.aggregateCountTripProduct(null);
    } else {
      tripProductList = tripProductDao.listTripProductByUser(luckerId, -1, start, size);
      countList = tripProductDao.aggregateCountTripProduct(luckerId);
    }

    Map<String, Object> map = new HashMap<String, Object>(2);
    map.put("productList", JSONConvertor.filterTripProducts(tripProductList));
    map.put("productCount", countList);
    jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> listUserTripRoute(Long luckerId) {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<String, Object> createCustomizeRoute(CustomizeRoute tripRoute) {
    tripRoute.setStatus(1);
    Object idObj = customizeRouteDao.saveCustomizeRoute(tripRoute);
    logger.info("CustomizeRoute object id:" + idObj);
    if (idObj == null) {
      return null;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRIP_ROUTE_ID, idObj.toString());
    return map;
  }

  public ServiceResult<String> updateCustomizeRoute(String tripRouteId, Map<String, Object> updateActionMap) {
    ServiceResult<String> result = new ServiceResult<String>();

    int updateCount = customizeRouteDao.updateCustomizeRoute(tripRouteId, updateActionMap);
    logger.info("Update CustomizeRoute objectId count:" + updateCount);
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to update customize route because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRIP_ROUTE_ID, tripRouteId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> updateCustomizeRouteStatus(String tripRouteId, Integer status) {
    ServiceResult<String> result = new ServiceResult<String>();

    int updateCount = customizeRouteDao.updateTripRouteStatus(tripRouteId, status);
    logger.info("Update CustomizeRoute objectId count:" + updateCount + " status=0(unusable)");
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to update customize route status because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRIP_ROUTE_ID, tripRouteId);
    map.put(DataConstant.STATUS, status);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> createTripPOIRoute(POIRoute poiRoute) {
    ServiceResult<String> result = new ServiceResult<String>();
    poiRoute.setStatus(1);

    Object idObj = tripPOIRouteDao.saveTripPOIRoute(poiRoute);
    logger.info("TripPOIRoute object id:" + idObj);
    if (idObj == null) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to create trip POI route because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.POI_ROUTE_ID, idObj.toString());
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> updateTripPOIRoute(String poiRouteId, Map<String, Object> updateActionMap) {
    ServiceResult<String> result = new ServiceResult<String>();
    int updateCount = tripPOIRouteDao.updatePOIRoute(poiRouteId, updateActionMap);
    logger.info("Update TripPOIRoute objectId count:" + updateCount);
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to update trip POI route because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.POI_ROUTE_ID, poiRouteId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ServiceResult<String> updateTripPOIRouteStatus(String poiRouteId, Integer status) {
    ServiceResult<String> result = new ServiceResult<String>();

    int updateCount = tripPOIRouteDao.updatePOIRouteStatus(poiRouteId, status);
    logger.info("Update TripPOIRoute objectId count:" + updateCount + " status=0(unusable)");
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to delete trip POI route because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.POI_ROUTE_ID, poiRouteId);
    map.put(DataConstant.STATUS, status);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> updateDayRouteInfo(String tripRouteId, String routeDay, String dayRouteTitle) {
    ServiceResult<String> result = new ServiceResult<String>();

    // CustomizeRoute customizeRoute=customizeRouteDao.getCustomizeRoute(tripRouteId);
    // Map<String, String> dayRouteInfo = customizeRoute.getDayRouteInfo();
    // if(dayRouteInfo==null){
    // dayRouteInfo=new HashMap<String,String>();
    // }
    // dayRouteInfo.put(routeDay, dayRouteTitle);

    Map<String, Object> updateActionMap = new HashMap<String, Object>();
    updateActionMap.put(DataConstant.DAY_ROUTE_INFO + "." + routeDay, dayRouteTitle);
    int updateCount = customizeRouteDao.updateCustomizeRoute(tripRouteId, updateActionMap);
    logger.info("Update CustomizeRoute dayRouteMap objectId count:" + updateCount);
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to update customize route dayRouteMap because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRIP_ROUTE_ID, tripRouteId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> deleteDayRoute(String tripRouteId, String routeDay) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put(DataConstant.TRIP_ROUTE_ID, tripRouteId);
    queryMap.put(DataConstant.ROUTE_DAY, routeDay);

    Map<String, Object> updateMap = new HashMap<String, Object>();
    updateMap.put(DataConstant.STATUS, 0);

    int updateCount = tripPOIRouteDao.updatePOIRoute(queryMap, updateMap);
    logger.info("Delete TripPOIRoute objectId count:" + updateCount + " status=0(unusable)");
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to delete trip POI route because ObjectId is null!");
      return result;
    }

    queryMap.put(DataConstant.STATUS, 0);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(queryMap);
    result.setBusinessResult(jsonResult);
    return result;
  }


  public ServiceResult<String> createTravelUserPOI(TravelPOI travelUserPOI) {
    ServiceResult<String> result = new ServiceResult<String>();
    long currentTime = System.currentTimeMillis();
    travelUserPOI.setCreateTime(currentTime);
    travelUserPOI.setUpdateTime(currentTime);

    Object idObj = travelPOIDao.saveTravelPOI(travelUserPOI);
    logger.info("TravelPOI objectId:" + idObj);
    if (idObj == null) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to create travel POI because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRAVELPOID, idObj.toString());
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> createTripNotice(TripNotice tripNotice) {
    ServiceResult<String> result = new ServiceResult<String>();
    long currentTime = System.currentTimeMillis();
    tripNotice.setCreateTime(currentTime);
    tripNotice.setUpdateTime(currentTime);

    Object idObj = tripNoticeDao.saveTripNotice(tripNotice);
    logger.info("TripNotice object id:" + idObj);
    if (idObj == null) {
      return null;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.TRIP_NOTICE_ID, idObj.toString());
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> updateLuckerUser(Long luckerId, Map<String, Object> luckerMap, Map<String, Object> userMap) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> map = new HashMap<String, Object>();
    if (commonService.checkLuckerExist(luckerId)) {
      Integer updateCount = commonService.updateLuckerUser(luckerId, luckerMap, userMap);
      if (updateCount == 0) {
        result.setReturnCode(ReturnCode.SERVER_ERROR);
        result.setReturnMessage("Fail to update lucker " + luckerId + " user info !");
        return result;
      }
      map.put(DataConstant.LUCKER_ID, luckerId);
    }
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> callbackResource(String resourceKey, String resourceURL) {
    // TODO Auto-generated method stub
    return null;
  }

  public void setTripProductDao(TripProductDao tripProductDao) {
    this.tripProductDao = tripProductDao;
  }

  public void setTripPOIRouteDao(TripPOIRouteDao tripPOIRouteDao) {
    this.tripPOIRouteDao = tripPOIRouteDao;
  }

  public void setCustomizeRouteDao(CustomizeRouteDao customizeRouteDao) {
    this.customizeRouteDao = customizeRouteDao;
  }

  public void setTravelPOIDao(TravelPOIDao travelPOIDao) {
    this.travelPOIDao = travelPOIDao;
  }

  public void setTripNoticeDao(TripNoticeDao tripNoticeDao) {
    this.tripNoticeDao = tripNoticeDao;
  }

  public void setCommonService(CommonService commonService) {
    this.commonService = commonService;
  }


}
