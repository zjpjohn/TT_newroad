package com.newroad.tripmaster.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newroad.tripmaster.constant.HttpConstant;
import com.newroad.tripmaster.filter.TokenAuthFilter;
import com.newroad.tripmaster.service.RecommendServiceIf;

@Controller
@RequestMapping("/v{apiVersion}/recommend")
public class RecommendController {

  private static Logger logger = LoggerFactory.getLogger(RecommendController.class);
  
  @Autowired
  private RecommendServiceIf recommendService;
  
  private String userId;
  
  /**
   * system recommend
   */
  @RequestMapping(value = "/select", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String select(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    recommendService.select();
    return apiVersion;
  }

  
  /**
   * user Recommend
   */
  @RequestMapping(value = "/userselect", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String userSelect(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    recommendService.userSelect(getCurrentUserId());
    return apiVersion;
  }
  
  /**
   * list the selected scenic info
   */
  @RequestMapping(value = "/listscenic", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listScenic(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    recommendService.listScenic(getCurrentUserId());
    return apiVersion;
  }
  
  /**
   * 
   */
  @RequestMapping(value = "/userfootmark", produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listUserScenic(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    recommendService.listUserScenic(getCurrentUserId());
    return apiVersion;
  }
  
  private String getCurrentUserId(){
    if(userId==null){
      JSONObject session=TokenAuthFilter.getCurrent();
      userId=session.getString("userId");
    }
    return userId;
  }
}
