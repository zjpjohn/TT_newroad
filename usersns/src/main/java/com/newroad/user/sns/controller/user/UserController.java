package com.newroad.user.sns.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newroad.user.sns.constant.SnsConstant;
import com.newroad.user.sns.service.user.UserService;
import com.newroad.user.sns.util.ControllerUtils;
import com.newroad.util.apiresult.ApiReturnObjectUtil;

@Controller
@RequestMapping("/v{apiVersion}/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * 用户列表
	 */
	@RequestMapping(value = "/list", produces = SnsConstant.CONTENT_TYPE_JSON)
	public @ResponseBody String list(HttpServletRequest request) throws Exception {
		try {
			return userService.list(ControllerUtils.getParam(request)).toString();
		} catch (Exception e) {
			log.error("get user info error :", e);
			return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
		}
	}
	
	/**
	 * 检索用户信息
	 */
	@RequestMapping(value = "/getInfo", produces = SnsConstant.CONTENT_TYPE_JSON)
	public @ResponseBody String getInfo(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
		try {
			return userService.getInfo(apiVersion, ControllerUtils.getParam(request)).toString();
		} catch (Exception e) {
			log.error("get user info error :", e);
			return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
		}
	}
	
	   /**
     * 检索用户信息
     */
    @RequestMapping(value = "/info", produces = SnsConstant.CONTENT_TYPE_JSON)
    public @ResponseBody String info(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
        try {
            return userService.info(apiVersion, ControllerUtils.getParam(request)).toString();
        } catch (Exception e) {
            log.error("get user info error :", e);
            return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
        }
    }

	/**
	 * 编辑用户信息
	 */
	@RequestMapping(value = "/edit", produces = SnsConstant.CONTENT_TYPE_JSON)
	public @ResponseBody String edit(HttpServletRequest request) throws Exception {
		try {
			return userService.update(ControllerUtils.getParam(request)).toString();
		} catch (Exception e) {
			log.error("bind account error :", e);
			return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
		}
	}
	
}
