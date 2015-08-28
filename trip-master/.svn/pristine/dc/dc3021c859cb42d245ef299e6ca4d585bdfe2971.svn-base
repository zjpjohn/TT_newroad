package com.newroad.tripmaster.service;

import java.util.Map;

import com.newroad.tripmaster.dao.pojo.trip.CustomizeRoute;
import com.newroad.tripmaster.dao.pojo.trip.POIRoute;
import com.newroad.tripmaster.dao.pojo.trip.TravelPOI;
import com.newroad.tripmaster.dao.pojo.trip.TripNotice;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;
import com.newroad.util.apiresult.ServiceResult;

public interface ProductDesignServiceIf {

  public ServiceResult<String> createTripProduct(TripProduct tripProduct);

  public ServiceResult<String> updateTripProduct(String tripProductId, Map<String, Object> updateActionMap);

  public ServiceResult<String> updateTripProductStatus(String tripProductId, Integer status);

  public ServiceResult<String> listUserTripProduct(Long luckerId, Integer userRole, Integer start, Integer size);

  public ServiceResult<String> listUserTripRoute(Long luckerId);

  // public ServiceResult<String> detailCustomizeRoute(String tripRouteId);

  public Map<String, Object> createCustomizeRoute(CustomizeRoute tripRoute);

  public ServiceResult<String> updateCustomizeRoute(String tripRouteId, Map<String, Object> updateActionMap);

  public ServiceResult<String> updateCustomizeRouteStatus(String tripRouteId, Integer status);

  public ServiceResult<String> updateDayRouteInfo(String tripRouteId, String routeDay, String title);

  public ServiceResult<String> deleteDayRoute(String tripRouteId, String routeDay);

  // public ServiceResult<String> detailCustomizeDayRoute(String tripRouteId,String routeDay);

  public ServiceResult<String> createTripPOIRoute(POIRoute poiRoute);

  public ServiceResult<String> updateTripPOIRoute(String poiRouteId, Map<String, Object> updateActionMap);

  public ServiceResult<String> updateTripPOIRouteStatus(String poiRouteId, Integer status);

  public ServiceResult<String> createTravelUserPOI(TravelPOI travelUserPOI);

  public ServiceResult<String> updateLuckerUser(Long luckerId, Map<String, Object> luckerMap, Map<String, Object> userMap);

  public ServiceResult<String> createTripNotice(TripNotice tripNotice);

  public ServiceResult<String> callbackResource(String resourceKey, String resourceURL);
}
