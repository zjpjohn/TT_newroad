package com.newroad.tripmaster.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newroad.tripmaster.constant.HttpConstant;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.filter.TokenAuthFilter;
import com.newroad.tripmaster.service.CommonServiceIf;
import com.newroad.tripmaster.service.VoyagerServiceIf;
import com.newroad.util.apiresult.ApiReturnObjectUtil;

@Controller
@RequestMapping("/v{apiVersion}/voyager")
public class VoyagerController {

  private static Logger logger = LoggerFactory.getLogger(VoyagerController.class);

  @Autowired
  private VoyagerServiceIf voyagerService;

  @Autowired
  private CommonServiceIf commonService;

  /**
   * list trip product by cityCode
   */
  @RequestMapping(value = "/products/{cityCode}/{recommendType}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listTripProduct(HttpServletRequest request, @PathVariable("cityCode") String cityCode,
      @PathVariable("recommendType") Integer recommendType, @PathVariable String apiVersion) throws Exception {
    logger.debug("listTripProduct cityCode:" + cityCode);
    String result = null;
    switch (recommendType) {
      case 1:
        result = voyagerService.listTripProduct(cityCode).toString();
        break;
      case 2:
        result = voyagerService.listTripProduct(cityCode).toString();
        break;
    }
    return result;
  }
  
  /**
   * detail trip product
   */
  @RequestMapping(value = "/product/{tripProductId}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String detailTripProduct(HttpServletRequest request, @PathVariable("tripProductId") String tripProductId, @PathVariable String apiVersion)
      throws Exception {
    if (tripProductId == null || "".equals(tripProductId)) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    return voyagerService.detailTripProduct(tripProductId).toString();
  }

  /**
   * customize route detail
   */
  @RequestMapping(value = "/route/{tripRouteId}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String detailTripRoute(HttpServletRequest request, @PathVariable("tripRouteId") String tripRouteId, @PathVariable String apiVersion)
      throws Exception {
    if (tripRouteId == null || "".equals(tripRouteId)) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    return voyagerService.detailCustomizeRoute(tripRouteId).toString();
  }

  /**
   * travel poi detail
   */
  @RequestMapping(value = "/travelpoi/{travelPOId}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String detailTravelPOI(HttpServletRequest request, @PathVariable("travelPOId") String travelPOId, @PathVariable String apiVersion)
      throws Exception {
    if (travelPOId == null || "".equals(travelPOId)) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    return voyagerService.detailTravelPOI(travelPOId).toString();
  }

  /**
   * list trip product by cityCode
   */
  @RequestMapping(value = "/luckers/{start}/{size}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listProductLucker(HttpServletRequest request, @PathVariable("start") Integer start, @PathVariable("size") Integer size,
      @PathVariable String apiVersion) throws Exception {
    logger.debug("listProductLucker userRole=2");
    return commonService.listSimpleUsers(2, start, size).toString();
  }

  /**
   * get lucker detail
   */
  @RequestMapping(value = "/lucker/{luckerId}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String detailTravelLucker(HttpServletRequest request, @PathVariable("luckerId") Long luckerId, @PathVariable String apiVersion)
      throws Exception {
    if (luckerId == null || "".equals(luckerId)) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    return voyagerService.getLuckerUserInfo(luckerId).toString();
  }
  
  @RequestMapping(value = "/mine", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String detailMyInfo(HttpServletRequest request, @PathVariable String apiVersion)
      throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    Long userId = user.getUserId();
    return voyagerService.getMyUserInfo(userId).toString();
  }
  
  /**
   * get lucker detail
   */
  @RequestMapping(value = "/mine/order", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listMyProductOrder(HttpServletRequest request, @PathVariable String apiVersion)
      throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    Long userId = user.getUserId();
    return voyagerService.listMyProductOrder(userId).toString();
  }
  
  /**
   * get lucker detail
   */
  @RequestMapping(value = "/mine/favorite", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listMyFavorit(HttpServletRequest request, @PathVariable String apiVersion)
      throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    Long userId = user.getUserId();
    return voyagerService.listMyFavorite(userId).toString();
  }
  
  /**
   * list trip cities
   */
  @RequestMapping(value = "/city/{parentCode}/{cityLevel}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listTripCity(HttpServletRequest request, @PathVariable("parentCode") String parentCode,
      @PathVariable("cityLevel") Integer cityLevel, @PathVariable String apiVersion) throws Exception {
    return commonService.listCities(parentCode, cityLevel).toString();
  }

  /**
   * recommend
   */
  @RequestMapping(value = "/product/recommend", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String recommendTripProduct(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null) {
      return null;
    }
    return voyagerService.recommendTripProduct(user.getUserId()).toString();
  }

}
