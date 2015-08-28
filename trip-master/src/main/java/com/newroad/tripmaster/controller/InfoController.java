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
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.dao.pojo.info.Comment;
import com.newroad.tripmaster.dao.pojo.info.Tips;
import com.newroad.tripmaster.dao.pojo.info.UserBehavior;
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
   * like product
   */
  @RequestMapping(value = "/product/like/{tripProductId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String likeProduct(HttpServletRequest request, @PathVariable("tripProductId") String tripProductId, @PathVariable String apiVersion)
      throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null || tripProductId == null) {
      logger.error("Fail to get request parameters when liking product!");
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    UserBehavior userBehavior = new UserBehavior(1, "", tripProductId, user.getUserId());
    logger.info("Save user behavior product like action:" + userBehavior);
    return infoService.submitCommonBehavior(userBehavior).toString();
  }

  /**
   * ignore product
   */
  @RequestMapping(value = "/product/ignore/{tripProductId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String unlikeProduct(HttpServletRequest request, @PathVariable("tripProductId") String tripProductId, @PathVariable String apiVersion)
      throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null || tripProductId == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId or tripProductId!").toString();
    }
    UserBehavior userBehavior = new UserBehavior(1, "", tripProductId, user.getUserId());
    logger.info("Ignore product like action for tripProductId:" + tripProductId);
    return infoService.updateCommonBehaviorType(userBehavior, 2).toString();
  }

  /**
   * submit comment
   */
  @RequestMapping(value = "/comment/submit/{type}/{targetId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String submitComment(HttpServletRequest request, @PathVariable("type") Integer type, @PathVariable("targetId") String targetId,
      @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    if (targetId == null || type == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for comment targetId or type!").toString();
    }
    Comment comment = JSONConvertor.getJSONInstance().fromJson(reqParam, Comment.class);
    comment.setUserId(user.getUserId());
    comment.setActionType(type);
    comment.setTargetId(targetId);
    logger.info("Save comment:" + comment);
    return infoService.submitComment(comment).toString();
  }

  /**
   * list comment
   */
  @RequestMapping(value = "/comment/list/{type}/{targetId}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listComment(HttpServletRequest request, @PathVariable("type") Integer type, @PathVariable("targetId") String targetId,
      @PathVariable String apiVersion) throws Exception {
    if (targetId == null || type == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for comment targetId or type!").toString();
    }
    logger.info("list comments for targetId:" + targetId + " type:" + type);
    return infoService.listComments(type, targetId).toString();
  }


  /**
   * save tips
   */
  @RequestMapping(value = "/tips/save", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String saveTips(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    Tips tips = JSONConvertor.getJSONInstance().fromJson(reqParam, Tips.class);
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    tips.setUserId(user.getUserId());
    logger.info("Save tips:" + tips);
    return infoService.submitTipContent(tips).toString();
  }

  /**
   * list tips
   */
  @RequestMapping(value = "/tips/list/{hashSiteId}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listTips(HttpServletRequest request, @PathVariable("hashSiteId") String hashSiteId, @PathVariable String apiVersion)
      throws Exception {
    if (hashSiteId == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for hashsiteid!").toString();
    }
    logger.info("list scenic tips for hashsiteid:" + hashSiteId);
    return infoService.listScenicTips(hashSiteId).toString();
  }

  /**
   * 
   */
  @RequestMapping(value = "/tips/support", produces = HttpConstant.CONTENT_TYPE_JSON)
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
    tips.setUserId(userid);
    if (tips.getSupporttype() == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for support type!").toString();
    }
    logger.info("User tips info:" + userid);
    return infoService.submitTipOpinion(tips).toString();
  }

  /**
   * get user tips
   */
  @RequestMapping(value = "/tips/user/{hashSiteId}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String getUserTips(HttpServletRequest request, @PathVariable("hashSiteId") String hashSiteId, @PathVariable String apiVersion)
      throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    // Integer userIdStr = (Integer) map.get(DataConstant.USERID);
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    Long userId = user.getUserId();
    logger.info("User tips info:" + userId);
    return infoService.getUserTips(userId, hashSiteId).toString();
  }

}
