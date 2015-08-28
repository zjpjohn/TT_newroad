package com.newroad.tripmaster.service;

import com.newroad.util.apiresult.ServiceResult;

public interface VoyagerServiceIf {

  public ServiceResult<String> listTripProduct(String cityCode);

  public ServiceResult<String> detailTripProduct(String tripProductId);

  public ServiceResult<String> listProductTravelDates(String tripProductId, String yearMonth);

  public ServiceResult<String> detailCustomizeRoute(String tripRouteId);

  public ServiceResult<String> detailCustomizeDayRoute(String tripRouteId, String routeDay);

  public ServiceResult<String> detailTravelPOI(String travelPOIId);

  public ServiceResult<String> listLuckerUsers(Integer start, Integer size);

  public ServiceResult<String> getLuckerUserInfo(Long userId);

  public ServiceResult<String> getMyUserInfo(Long userId);

  public ServiceResult<String> listMyProductOrder(Long userId);

  public ServiceResult<String> listMyFavorite(Long userId);

  public ServiceResult<String> recommendTripProduct(Long userId);
}
