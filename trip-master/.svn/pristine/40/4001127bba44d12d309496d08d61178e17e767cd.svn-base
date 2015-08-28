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
import com.newroad.tripmaster.dao.pojo.info.Comment;
import com.newroad.tripmaster.filter.TokenAuthFilter;
import com.newroad.tripmaster.service.InfoServiceIf;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;

@Controller
@RequestMapping("/v{apiVersion}/comment")
public class CommentController {
  private static Logger logger = LoggerFactory.getLogger(CommentController.class);

  @Autowired
  private InfoServiceIf commentService;

  /**
   * submit comment
   */
  @RequestMapping(value = "/submit", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String submitComment(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    Comment comment = JSONConvertor.getJSONInstance().fromJson(reqParam, Comment.class);
    SimpleUser user=TokenAuthFilter.getCurrentUser();
    if (user == null) {
      return ApiReturnObjectUtil.getReturn400("Bad request params for userId!").toString();
    }
    comment.setUserid(user.getUserId());
    logger.info("Save comment:" + comment);
    return commentService.submitComment(comment).toString();
  }

  /**
   * list comment
   */
  @RequestMapping(value = "/list", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listComment(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
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
    return commentService.listScenicComments(hashSiteId).toString();
  }

}
