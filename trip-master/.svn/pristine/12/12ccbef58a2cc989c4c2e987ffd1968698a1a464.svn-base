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
import com.newroad.tripmaster.dao.pojo.Scenic;
import com.newroad.tripmaster.service.ScenicServiceIf;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;
import com.newroad.util.apiresult.ServiceResult;


@Controller
@RequestMapping("/v{apiVersion}/scenic")
public class ScenicController {

  
  private static Logger logger = LoggerFactory.getLogger(ScenicController.class);

  @Autowired
  private ScenicServiceIf scenicService;
  
  /**
   * summary scenic
   */
  @RequestMapping(value = "/summary", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String summaryScenic(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    @SuppressWarnings("unchecked")
    Map<String,Object> map=JSONConvertor.getJSONInstance().readValue(reqParam, Map.class);
    String hashSiteId = (String) map.get(DataConstant.HASH_SITE_ID);
    logger.info("Summary Scenic info by hashsiteid:" + hashSiteId);
    ServiceResult<String> result=scenicService.detailScenic(hashSiteId);
    return result.toString();
  }
  
  /**
   * detail scenic
   */
  @RequestMapping(value = "/detail", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String detailScenic(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    @SuppressWarnings("unchecked")
    Map<String,Object> map=JSONConvertor.getJSONInstance().readValue(reqParam, Map.class);
    String hashSiteId = (String) map.get(DataConstant.HASH_SITE_ID);
    
    logger.info("Get Scenic Detail by hashsiteid:" + hashSiteId);
    ServiceResult<String> result=scenicService.detailScenic(hashSiteId);
    return result.toString();
  }

  @RequestMapping(value = "/save", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String saveScenic(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    Scenic scenic=JSONConvertor.getJSONInstance().fromJson(reqParam,Scenic.class);
    logger.info("Save Scenic Info:" + scenic);
    ServiceResult<String> result=scenicService.saveScenic(scenic);
    return result.toString();
  }
  

}
