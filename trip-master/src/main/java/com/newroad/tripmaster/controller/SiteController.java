package com.newroad.tripmaster.controller;

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
import com.newroad.tripmaster.dao.pojo.Coordinate;
import com.newroad.tripmaster.dao.pojo.Site;
import com.newroad.tripmaster.service.SiteServiceIf;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;
import com.newroad.util.apiresult.ServiceResult;

@Controller
@RequestMapping("/v{apiVersion}/site")
public class SiteController {

  private static Logger logger = LoggerFactory.getLogger(SiteController.class);

  @Autowired
  private SiteServiceIf locationService;

  /**
   * save location
   */
  @RequestMapping(value = "/save", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String saveLocation(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    Site site = JSONConvertor.transformJSON2Site(reqParam);
    logger.info("Site info save:" + site);
    return locationService.saveSite(site).toString();
  }

  /**
   * list sites
   */
  @RequestMapping(value = "/list", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String list(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    Coordinate point = JSONConvertor.getJSONInstance().fromJson(reqParam, Coordinate.class);
    logger.info("Site list Query point:" + point);
    ServiceResult<String> json = locationService.listSites(point, point.getSitetype());
    return json.toString();
  }
  
  /**
   * get site
   */
  @RequestMapping(value = "/get", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String position(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    @SuppressWarnings("unchecked")
    Map<String,Object> map=JSONConvertor.getJSONInstance().readValue(reqParam, Map.class);
    String hashSiteId = (String) map.get(DataConstant.HASH_SITE_ID);
    logger.info("Get Site by hashSiteId:" + hashSiteId);
    ServiceResult<String> json = locationService.getSite(hashSiteId);
    return json.toString();
  }

}
