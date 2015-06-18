package com.newroad.user.sns.service.user;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.user.sns.constant.SnsConstant;
import com.newroad.user.sns.dao.SnsDao;
import com.newroad.user.sns.filter.TokenAuthFilter;
import com.newroad.user.sns.model.Page;
import com.newroad.user.sns.model.login.LoginUser;
import com.newroad.user.sns.model.user.User;
import com.newroad.user.sns.service.application.ApplicationService;
import com.newroad.user.sns.util.ControllerUtils;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.apiresult.ServiceResult;

/**
 * 用户服务
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class UserService {

  private static Logger log = LoggerFactory.getLogger(UserService.class);

  private SnsDao snsDao;
  
  @SuppressWarnings("unused")
  private ApplicationService application;

  /**
   * 用户列表
   */
  public ServiceResult list(Map<String, Object> param) throws Exception {
    Page<List<User>> page = new Page<List<User>>();
    page.getPara().putAll(param);

    page.setPage(param.get(SnsConstant.PAGE_KEY));
    page.setSize(param.get(SnsConstant.PAGE_SIZE_KEY));

    page = snsDao.selectPage("user.findUserList", page);

    return new ServiceResult<Object>(ControllerUtils.fromPage(page));
  }

  /**
   * 用户信息
   */
  public ServiceResult getInfo(String apiVersion, Map<String, Object> param) throws Exception {

    LoginUser current = TokenAuthFilter.getCurrent();
    log.info("User info, user login id:" + current.getUserID());

    // Long userID = (Long) param.get(LoginUser.USERID);
    Long userID = current.getUserID();
    log.info("User info, user id:" + userID);

    User user = getUserByID(userID);

    // String app = (String)param.get("app");
    // Object info = application.getUserInfo(app, user, current, apiVersion);

    log.info("get user info for userID:" + userID);
    return new ServiceResult<Object>(user);
  }

  /**
   * 用户信息
   */
  public ServiceResult info(String apiVersion, Map<String, Object> param) throws Exception {
    Integer userID = (Integer) param.get(LoginUser.USERID);
    log.info("User info, user id:" + userID);
    User user = getUserByID(userID.longValue());

    // String app = (String)param.get("app");
    // Object info = application.getUserInfo(app, user, current, apiVersion);

    log.info("get user info for userID:" + userID);
    return new ServiceResult<Object>(user);
  }

  /**
   * 编辑用户信息
   */
  public ServiceResult update(Map<String, Object> param) throws Exception {
    Long userID = (Long) param.get(LoginUser.USERID);

    User user = getUserByID(userID);
    if (user == null)
      return new ServiceResult(ReturnCode.DATA_NOT_FOUND, ControllerUtils.bulid("user not found!"), null);

    if (SnsConstant.USER_STATUS_BLACK == user.getStatus())
      return new ServiceResult(ReturnCode.NO_PERMISSION, ControllerUtils.bulid("user status black!"), null);

    filterEditUserParameters(param);
    Integer updateCount = snsDao.update("user.editUser", param);
    if (updateCount == 1) {
      return new ServiceResult<String>(ReturnCode.OK, ControllerUtils.bulid("Update user profile successfully!"), userID.toString());
    }
    return new ServiceResult(ReturnCode.SERVER_ERROR, ControllerUtils.bulid("Fail to update user info!"), null);
  }

  private void filterEditUserParameters(Map<String, Object> paramMap) {
    Set<Entry<String, Object>> paramSet = paramMap.entrySet();
    Iterator<Entry<String, Object>> iter = paramSet.iterator();
    while (iter.hasNext()) {
      Entry<String, Object> entry = iter.next();
      Object value = entry.getValue();
      if ("".equals(value)) {
        iter.remove();
      } else if (value instanceof Integer) {
        Integer i = (Integer) value;
        if (i == 0) {
          iter.remove();
        }
      }
    }
  }

  /**
   * 绑定帐号
   */
  public ServiceResult bindAccount(Map<String, Object> param) throws Exception {
    return new ServiceResult<String>();
  }

  /**
   * 用户注册
   */
  public ServiceResult register(Map<String, Object> param) {
    return new ServiceResult<String>();
  }

  /**
   * 根据ID查找用户
   */
  public User getUserByID(Long userID) {
    return (User) snsDao.selectOne("user.getUserInfoByUserID", userID);
  }

  public void setApplication(ApplicationService application) {
    this.application = application;
  }

  public void setSnsDao(SnsDao snsDao) {
    this.snsDao = snsDao;
  }
}
