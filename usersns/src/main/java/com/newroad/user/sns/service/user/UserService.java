package com.newroad.user.sns.service.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.user.sns.constant.SnsConstant;
import com.newroad.user.sns.dao.SnsDao;
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
  public ServiceResult list(Integer role, Integer pageStart, Integer size) throws Exception {
    Page<List<User>> page = new Page<List<User>>();
    page.setPage(pageStart);
    page.setSize(size);

    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put("userRole", role);
    page = snsDao.selectPage("user.findUserList", queryMap, page);
    return new ServiceResult<Object>(ControllerUtils.fromPage(page));
  }

  /**
   * 用户信息
   */
  public ServiceResult info(String apiVersion, Long userId) throws Exception {
    User user = getUserByID(userId);
    // String app = (String)param.get("app");
    // Object info = application.getUserInfo(app, user, current, apiVersion);
    log.debug("get user info for userID:" + userId);
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

  public Boolean updateRole(Long userID, Integer userRole) throws Exception {
    Boolean result = false;
    User user = getUserByID(userID);
    if (user == null)
      return null;

    if (SnsConstant.USER_STATUS_BLACK == user.getStatus())
      return null;
    Map<String, Object> updateMap = new HashMap<String, Object>();
    updateMap.put("userId", userID);
    updateMap.put("userRole", userRole);
    Integer updateCount = snsDao.update("user.updateUserRole", updateMap);
    if (updateCount == 1) {
      result = true;
    }
    return result;
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
