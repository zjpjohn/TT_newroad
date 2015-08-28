package com.newroad.tripmaster.service.scenic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.CustomizeRouteDao;
import com.newroad.tripmaster.dao.TravelPOIDao;
import com.newroad.tripmaster.dao.TripNoticeDao;
import com.newroad.tripmaster.dao.TripPOIRouteDao;
import com.newroad.tripmaster.dao.TripProductDao;
import com.newroad.tripmaster.dao.pojo.Lucker;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.dao.pojo.order.ProductOrder;
import com.newroad.tripmaster.dao.pojo.trip.CustomizeRoute;
import com.newroad.tripmaster.dao.pojo.trip.POIRoute;
import com.newroad.tripmaster.dao.pojo.trip.TravelPOI;
import com.newroad.tripmaster.dao.pojo.trip.TripDayRoute;
import com.newroad.tripmaster.dao.pojo.trip.TripNotice;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;
import com.newroad.tripmaster.service.VoyagerServiceIf;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.apiresult.ServiceResult;

public class VoyagerService implements VoyagerServiceIf {

  private static Logger logger = LoggerFactory.getLogger(VoyagerService.class);

  private TripNoticeDao tripNoticeDao;

  private TripProductDao tripProductDao;

  private TripPOIRouteDao tripPOIRouteDao;

  private TravelPOIDao travelPOIDao;

  private CustomizeRouteDao customizeRouteDao;

  private ProductOrderService productOrderService;

  private CommonService commonService;

  /**
   * list trip products
   */
  public ServiceResult<String> listTripProduct(String cityCode) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    List<TripProduct> tripProductList = tripProductDao.listTripProducts(cityCode, 1);
    logger.debug("Trip Product list:" + tripProductList);
    if (tripProductList == null || tripProductList.size() == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to list trip product because ObjectId is null!");
      return result;
    }

    for (TripProduct tripProduct : tripProductList) {
      getProductLuckerInfo(tripProduct, tripProduct.getLuckerId());
    }
    Integer productCount = tripProductList.size();

    Map<String, Object> map = new HashMap<String, Object>(2);
    map.put("productList", JSONConvertor.filterTripProducts(tripProductList));
    map.put("productCount", productCount);
    jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  private void getProductLuckerInfo(TripProduct tripProduct, Long luckerId) {
    SimpleUser userInfo = commonService.getUserInfo(luckerId);
    tripProduct.setUserInfo(userInfo);
  }

  public ServiceResult<String> detailTripProduct(String productId) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    TripProduct tripProduct = tripProductDao.getTripProduct(productId);
    if (tripProduct == null) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to detail trip product because tripProduct is null!");
      return result;
    }

    String tripRouteId = tripProduct.getTripRouteId();
    if (tripRouteId != null) {
      CustomizeRoute customizeRoute = customizeRouteDao.getCustomizeRoute(tripRouteId);
      if (customizeRoute != null) {
        // Map<String, TripDayRoute> dayRouteMap = groupDistinctTripDayRoute(tripRouteId);
        // customizeRoute.setDayRouteMap(dayRouteMap);
        tripProduct.setTripRoute(customizeRoute);
      }
    } else {
      logger.error("TripRouteId is null!");
    }

    getProductLuckerInfo(tripProduct, tripProduct.getLuckerId());

    List<TripNotice> tripNotices = tripNoticeDao.listTripNoticeByProduct(productId);
    tripProduct.setTripNotices(tripNotices);

    jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(tripProduct);
    logger.debug("Trip Product information:" + jsonResult);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public Map<String, TripDayRoute> groupDistinctTripDayRoute(String tripRouteId) {
    Map<String, TripDayRoute> dayRouteMap = new TreeMap<String, TripDayRoute>();
    SortedSet<POIRoute> routeSet = tripPOIRouteDao.aggregateTripDayPOI(tripRouteId);
    for (POIRoute route : routeSet) {
      String routeDay = route.getRouteDay();
      TripDayRoute currentDayRoute = dayRouteMap.get(routeDay);
      List<POIRoute> currentList = null;
      if (currentDayRoute == null) {
        currentList = new ArrayList<POIRoute>();
        currentList.add(route);
        dayRouteMap.put(routeDay, new TripDayRoute(null, currentList));
      } else {
        currentList = currentDayRoute.getPoiRouteList();
        currentList.add(route);
        dayRouteMap.put(routeDay, currentDayRoute);
      }
    }
    logger.info("groupDistinctTripDayRoute:" + dayRouteMap);
    return dayRouteMap;
  }

  private Map<String, TripDayRoute> groupTripDayRoute(String tripRouteId, Map<String, String> dayRouteInfo) {
    List<POIRoute> poiRouteList = tripPOIRouteDao.listTripPOIRoutes(tripRouteId, 1);
    Map<String, TripDayRoute> dayRouteMap = new TreeMap<String, TripDayRoute>();
    for (POIRoute route : poiRouteList) {
      String routeDay = route.getRouteDay();
      TripDayRoute currentDayRoute = dayRouteMap.get(routeDay);
      List<POIRoute> currentList = null;
      if (currentDayRoute == null) {
        currentList = new ArrayList<POIRoute>();
        currentList.add(route);
        TripDayRoute newDayRoute = new TripDayRoute(null, currentList);
        if (dayRouteInfo != null) {
          newDayRoute.setDayRouteTitle(dayRouteInfo.get(routeDay));
        }
        dayRouteMap.put(routeDay, newDayRoute);
      } else {
        currentList = currentDayRoute.getPoiRouteList();
        currentList.add(route);
        dayRouteMap.put(routeDay, currentDayRoute);
      }
    }
    logger.info("groupTripDayRoute:" + dayRouteMap);
    return dayRouteMap;
  }

  /**
   * trip route detail
   */
  public ServiceResult<String> detailCustomizeRoute(String tripRouteId) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    CustomizeRoute customizeRoute = customizeRouteDao.getCustomizeRoute(tripRouteId);
    if (customizeRoute == null) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to detail customize route because customizeRoute is null!");
      return result;
    }

    Map<String, TripDayRoute> dayRouteMap = groupTripDayRoute(tripRouteId, customizeRoute.getDayRouteInfo());
    customizeRoute.setDayRouteMap(dayRouteMap);
    jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(customizeRoute);
    logger.debug("Customize Trip Product information:" + jsonResult);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> detailTravelPOI(String travelPOIId) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    TravelPOI travelPOI = travelPOIDao.getTravelPOI(travelPOIId);
    if (travelPOI == null) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to detail travel poi because travelPOI is null!");
      return result;
    }

    jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(travelPOI);
    logger.info("Travel POI information:" + jsonResult);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> detailCustomizeDayRoute(String tripRouteId, String routeDay) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public ServiceResult<String> getLuckerUserInfo(Long userId) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    Lucker lucker = commonService.getLuckerUserInfo(userId);
    List<TripProduct> tripProductList = tripProductDao.listTripProductByUser(userId, -1, 0, 10);
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("lucker", lucker);
    map.put("luckerProductList", tripProductList);
    jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> getMyUserInfo(Long userId) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    SimpleUser simpleUser=commonService.getUserInfo(userId);
    jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(simpleUser);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> listMyProductOrder(Long userId) {
    ServiceResult<String> result = new ServiceResult<String>();
    String jsonResult = null;
    List<ProductOrder> productOrders = productOrderService.listUserProductOrders(userId);
    for(ProductOrder order:productOrders){
      Long luckerId=order.getLuckerId();
      Lucker lucker = commonService.getLuckerUserInfo(luckerId);
      order.setLucker(lucker);
      TripProduct tripProduct = tripProductDao.getTripProduct(order.getTripProductId());
      order.setTravelCities(tripProduct.getTravelCities());
    }
    jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(productOrders);
    result.setBusinessResult(jsonResult);
    return result;
  }
  
  
  @Override
  public ServiceResult<String> listMyFavorite(Long userId) {
    // TODO Auto-generated method stub
    return null;
  }

  
  public ServiceResult<String> recommendTripProduct(Long userId) {
    // TODO Auto-generated method stub
    return null;
  }

  public void setTripNoticeDao(TripNoticeDao tripNoticeDao) {
    this.tripNoticeDao = tripNoticeDao;
  }

  public void setTripProductDao(TripProductDao tripProductDao) {
    this.tripProductDao = tripProductDao;
  }

  public void setTripPOIRouteDao(TripPOIRouteDao tripPOIRouteDao) {
    this.tripPOIRouteDao = tripPOIRouteDao;
  }

  public void setTravelPOIDao(TravelPOIDao travelPOIDao) {
    this.travelPOIDao = travelPOIDao;
  }

  public void setCustomizeRouteDao(CustomizeRouteDao customizeRouteDao) {
    this.customizeRouteDao = customizeRouteDao;
  }

  public void setCommonService(CommonService commonService) {
    this.commonService = commonService;
  }

  public void setProductOrderService(ProductOrderService productOrderService) {
    this.productOrderService = productOrderService;
  }

  
}
