package com.newroad.tripmaster.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.HttpConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.dao.pojo.info.Tips;
import com.newroad.tripmaster.filter.TokenAuthFilter;
import com.newroad.tripmaster.service.InfoServiceIf;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;


@Controller
@RequestMapping("/v{apiVersion}/info")
public class InfoController {

  private static Logger logger = LoggerFactory.getLogger(InfoController.class);

  @Autowired
  private InfoServiceIf infoService;

  /**
   * save tips
   */
  @RequestMapping(value = "/save", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String saveTips(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    Tips tips = JSONConvertor.getJSONInstance().fromJson(reqParam, Tips.class);
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    tips.setUserid(user.getUserId());
    logger.info("Save tips:" + tips);
    return infoService.submitTipContent(tips).toString();
  }

  /**
   * list tips
   */
  @RequestMapping(value = "/list", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listTips(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> map = JSONConvertor.getJSONInstance().readValue(reqParam, Map.class);
    String hashSiteId = (String) map.get(DataConstant.HASH_SITE_ID);
    if (hashSiteId == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for hashsiteid!").toString();
    }
    logger.info("list scenic tips for hashsiteid:" + hashSiteId);
    return infoService.listScenicTips(hashSiteId).toString();
  }

  /**
   * 
   */
  @RequestMapping(value = "/support", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String supportTip(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    Tips tips = JSONConvertor.getJSONInstance().readValue(reqParam, Tips.class);
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    Long userid = user.getUserId();
    tips.setUserid(userid);
    if (tips.getSupporttype() == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for support type!").toString();
    }
    logger.info("User tips info:" + userid);
    return infoService.submitTipOpinion(tips).toString();
  }

  /**
   * get user tips
   */
  @RequestMapping(value = "/user", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String getUserTips(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> map = JSONConvertor.getJSONInstance().readValue(reqParam, Map.class);
    String hashSiteId = (String) map.get(DataConstant.HASH_SITE_ID);
    //Integer userIdStr = (Integer) map.get(DataConstant.USERID);
    Long userId = 0l;
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    userId = user.getUserId();
    if (userId == 0) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    logger.info("User tips info:" + userId);
    return infoService.getUserTips(userId, hashSiteId).toString();
  }

}
