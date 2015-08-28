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

import com.newroad.tripmaster.constant.HttpConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.pojo.Lucker;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.filter.TokenAuthFilter;
import com.newroad.tripmaster.service.SystemAdminServiceIf;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;

@Controller
@RequestMapping("/v{apiVersion}/admin")
public class SystemAdminController {

  private static Logger logger = LoggerFactory.getLogger(SystemAdminController.class);

  @Autowired
  private SystemAdminServiceIf adminService;

  /**
   * create trip lucker
   */
  @RequestMapping(value = "/lucker/create", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String createTripLucker(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when creating new lucker!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user.getUserRole() != 1) {
      return ApiReturnObjectUtil.getReturn401().toString();
    }
    Lucker lucker = JSONConvertor.getJSONInstance().fromJson(reqParam, Lucker.class);
    return adminService.createNewLucker(lucker).toString();
  }

}
