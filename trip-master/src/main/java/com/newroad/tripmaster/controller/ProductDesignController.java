package com.newroad.tripmaster.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.HttpConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.pojo.Lucker;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.dao.pojo.trip.CustomizeRoute;
import com.newroad.tripmaster.dao.pojo.trip.POIRoute;
import com.newroad.tripmaster.dao.pojo.trip.TravelDateUnit;
import com.newroad.tripmaster.dao.pojo.trip.TravelPOI;
import com.newroad.tripmaster.dao.pojo.trip.TripNotice;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;
import com.newroad.tripmaster.filter.TokenAuthFilter;
import com.newroad.tripmaster.service.ProductDesignServiceIf;
import com.newroad.tripmaster.util.BeanDBObjectUtils;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;
import com.newroad.util.apiresult.ReturnCode;

@Controller
@RequestMapping("/v{apiVersion}/design")
public class ProductDesignController {

  private static Logger logger = LoggerFactory.getLogger(ProductDesignController.class);

  @Autowired
  private ProductDesignServiceIf productDesignService;

  /**
   * save designed trip product
   */
  @RequestMapping(value = "/product/create", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String createTripProduct(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when creating new trip product!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    TripProduct tripProduct = JSONConvertor.getJSONInstance().fromJson(reqParam, TripProduct.class);
    if (tripProduct.getLuckerId() == null) {
      tripProduct.setLuckerId(user.getUserId());
    }
    return productDesignService.createTripProduct(tripProduct).toString();
  }

  /**
   * update designed trip product
   */
  @RequestMapping(value = "/product/update/{tripProductId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String updateTripProduct(HttpServletRequest request, @PathVariable String tripProductId, @PathVariable String apiVersion)
      throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when updating trip product!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    if (tripProductId == null || "".equals(tripProductId)) {
      logger.error("Fail to get tripProductId when updating trip product!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }

    TripProduct product = JSONConvertor.getJSONInstance().fromJson(reqParam, TripProduct.class);
    List<TravelDateUnit> travelDateList = product.getTravelDateList();
    product.setTravelDateList(null);
    Map<String, Object> productMap = BeanDBObjectUtils.bean2Map(product);
    BeanDBObjectUtils.filterBeanMap(productMap);
    return productDesignService.updateTripProduct(tripProductId, productMap, travelDateList).toString();
  }

  /**
   * update designed trip product status
   */
  @RequestMapping(value = "/product/status/{tripProductId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String updateTripProductStatus(HttpServletRequest request, @PathVariable String tripProductId, @PathVariable String apiVersion)
      throws Exception {
    if (tripProductId == null || "".equals(tripProductId)) {
      logger.error("Fail to get tripProductId when update trip product status!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when updating trip product status!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> map = JSONConvertor.getJSONInstance().readValue(reqParam, Map.class);
    Integer status = (Integer) map.get(DataConstant.STATUS);
    return productDesignService.updateTripProductStatus(tripProductId, status).toString();
  }

  /**
   * trip product list by user
   */
  @RequestMapping(value = "/product/list/{start}/{size}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listUserTripProduct(HttpServletRequest request, @PathVariable Integer start, @PathVariable Integer size,
      @PathVariable String apiVersion) throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null && start == null || size == null) {
      logger.error("Fail to get request parameters when listing trip product!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    Long lucker = user.getUserId();
    return productDesignService.listUserTripProduct(lucker, user.getUserRole(), start, size).toString();
  }

  /**
   * trip product list by user
   */
  @RequestMapping(value = "/route/list", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listUserTripRoute(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    Long userId = user.getUserId();
    return productDesignService.listUserTripRoute(userId).toString();
  }

  // /**
  // * create designed trip route
  // */
  // @RequestMapping(value = "/route/create", produces = HttpConstant.CONTENT_TYPE_JSON)
  // public @ResponseBody
  // String createTripRoute(HttpServletRequest request, @PathVariable String apiVersion) throws
  // Exception {
  // String reqParam = StringHelper.getRequestEntityString(request);
  // if (reqParam.indexOf("error") >= 0) {
  // return ApiReturnObjectUtil.getReturn401().toString();
  // }
  // CustomizeRoute tripRoute = JSONConvertor.getJSONInstance().fromJson(reqParam,
  // CustomizeRoute.class);
  // return productDesignService.createCustomizeRoute(tripRoute).toString();
  // }

  /**
   * update designed trip route
   */
  @RequestMapping(value = "/route/update/{tripRouteId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String updateTripRoute(HttpServletRequest request, @PathVariable String tripRouteId, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when updating trip route!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    if (tripRouteId == null || "".equals(tripRouteId)) {
      logger.error("Fail to get request parameters when updating trip route!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }

    CustomizeRoute route = JSONConvertor.getJSONInstance().fromJson(reqParam, CustomizeRoute.class);
    Map<String, Object> routeMap = BeanDBObjectUtils.bean2Map(route);
    BeanDBObjectUtils.filterBeanMap(routeMap);
    return productDesignService.updateCustomizeRoute(tripRouteId, routeMap).toString();
  }

  /**
   * update designed day trip route
   */
  @RequestMapping(value = "/dayroute/update/{tripRouteId}/{routeDay}", method = RequestMethod.POST,
      produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String updateDayTripRoute(HttpServletRequest request, @PathVariable String tripRouteId, @PathVariable String routeDay,
      @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when updating day trip route!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> map = JSONConvertor.getJSONInstance().fromJson(reqParam, Map.class);
    String dayRouteTitle = (String) map.get(DataConstant.DAY_ROUTE_TITLE);
    if (tripRouteId == null || "".equals(tripRouteId) || dayRouteTitle == null || "".equals(dayRouteTitle)) {
      logger.error("Fail to get request parameters when updating day trip route!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    return productDesignService.updateDayRouteInfo(tripRouteId, routeDay, dayRouteTitle).toString();
  }

  /**
   * delete designed day trip route
   */
  @RequestMapping(value = "/dayroute/delete/{tripRouteId}/{routeDay}", method = RequestMethod.POST,
      produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String deleteDayTripRoute(HttpServletRequest request, @PathVariable String tripRouteId, @PathVariable String routeDay,
      @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when deleting day trip route!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    if (tripRouteId == null || "".equals(tripRouteId) || routeDay == null || "".equals(routeDay)) {
      logger.error("Fail to get request parameters when deleting day trip route!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    return productDesignService.deleteDayRoute(tripRouteId, routeDay).toString();
  }

  /**
   * delete designed trip route
   */
  @RequestMapping(value = "/route/delete/{tripRouteId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String deleteTripRoute(HttpServletRequest request, @PathVariable String tripRouteId, @PathVariable String apiVersion) throws Exception {
    if (tripRouteId == null || "".equals(tripRouteId)) {
      logger.error("Fail to get request parameters when deleting trip route!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    Integer status = 0;
    return productDesignService.updateCustomizeRouteStatus(tripRouteId, status).toString();
  }

  /**
   * create designed trip poi route
   */
  @RequestMapping(value = "/poiroute/create", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String createTripPOIRoute(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when creating poiroute!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    POIRoute poiRoute = JSONConvertor.getJSONInstance().fromJson(reqParam, POIRoute.class);
    return productDesignService.createTripPOIRoute(poiRoute).toString();
  }

  /**
   * update designed trip poi route
   */
  @RequestMapping(value = "/poiroute/update/{poiRouteId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String updateTripPOIRoute(HttpServletRequest request, @PathVariable String poiRouteId, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when updating poiroute!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    if (poiRouteId == null || "".equals(poiRouteId)) {
      logger.error("Fail to get request parameters when updating poiroute!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    POIRoute poiRoute = JSONConvertor.getJSONInstance().fromJson(reqParam, POIRoute.class);
    Map<String, Object> poiRouteMap = BeanDBObjectUtils.bean2Map(poiRoute);
    BeanDBObjectUtils.filterBeanMap(poiRouteMap);
    poiRouteMap.remove(DataConstant.POI_ROUTE_ID);
    return productDesignService.updateTripPOIRoute(poiRouteId, poiRouteMap).toString();
  }

  /**
   * delete designed trip poi route
   */
  @RequestMapping(value = "/poiroute/delete/{poiRouteId}", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String deleteTripPOIRoute(HttpServletRequest request, @PathVariable String poiRouteId, @PathVariable String apiVersion) throws Exception {
    if (poiRouteId == null || "".equals(poiRouteId)) {
      logger.error("Fail to get request parameters when deleting poiroute!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    Integer status = 0;
    return productDesignService.updateTripPOIRouteStatus(poiRouteId, status).toString();
  }


  /**
   * add travel poi
   */
  @RequestMapping(value = "/travelpoi/add", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String addTravelPOI(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when adding travel poi!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    TravelPOI travelPOI = JSONConvertor.getJSONInstance().fromJson(reqParam, TravelPOI.class);
    travelPOI.setLuckerId(user.getUserId());
    return productDesignService.createTravelUserPOI(travelPOI).toString();
  }

  /**
   * add travel poi
   */
  @RequestMapping(value = "/notice/create", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String createTripNotice(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when creating trip notice!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    TripNotice tripNotice = JSONConvertor.getJSONInstance().fromJson(reqParam, TripNotice.class);
    tripNotice.setLuckerId(user.getUserId());
    return productDesignService.createTripNotice(tripNotice).toString();
  }

  /**
   * update trip lucker
   */
  @RequestMapping(value = "/lucker/update/{userId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String updateTripLucker(HttpServletRequest request, @PathVariable Long userId, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when updating new lucker!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user.getUserRole() == 2 && userId != user.getUserId()) {
      return ApiReturnObjectUtil.getReturn401().toString();
    }
    Lucker lucker = JSONConvertor.getJSONInstance().fromJson(reqParam, Lucker.class);
    Map<String, Object> luckerMap = BeanDBObjectUtils.bean2Map(lucker);
    Map<String, Object> userMap = null;
    if (luckerMap.get("simpleUser") != null) {
      Object obj = luckerMap.get("simpleUser");
      userMap = BeanDBObjectUtils.bean2Map(obj);
    }
    return productDesignService.updateLuckerUser(userId, luckerMap, userMap).toString();
  }

  /**
   * save new callback resource info
   */
  @RequestMapping(value = "/resource/callback", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String callbackResource(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> requestMap = JSONConvertor.getJSONInstance().fromJson(reqParam, Map.class);
    String resourceKey = (String) requestMap.get("resourceKey");
    String resourceURL = (String) requestMap.get("resourceURL");
    return productDesignService.callbackResource(resourceKey, resourceURL).toString();
  }

  public ReturnCode checkUserAuthitory(SimpleUser user) {
    if (user == null || user.getUserId() == 0) {
      logger.error("Fail to get user token because toke is fail or expired!");
      return ReturnCode.NO_TOKEN;
    }
    if (user.getUserRole() != 1 && user.getUserRole() != 2) {
      logger.error("Fail to access because of unauthorization!");
      return ReturnCode.UNAUTHORIZED;
    }
    return ReturnCode.OK;
  }
}
