package com.newroad.tripmaster.service.scenic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.maria.MariaDao;
import com.newroad.tripmaster.dao.pojo.Lucker;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.dao.pojo.trip.TripCityDict;
import com.newroad.tripmaster.service.CommonServiceIf;
import com.newroad.util.apiresult.ServiceResult;
import com.newroad.util.http.HttpUtil;
import com.newroad.util.http.HttpUtil.Method;


public class CommonService implements CommonServiceIf {

  private static Logger logger = LoggerFactory.getLogger(CommonService.class);

  @Value("${USERSNS_HOST_URL}")
  private String accountHostURL;

  private static String USER_INFO_URL = "user/info";

  private static String USER_LIST_URL = "user/list";

  private MariaDao mariaDao;

  public String listSimpleUsers(Integer userRole, Integer start, Integer size) {
    String url = accountHostURL + USER_LIST_URL;
    StringBuilder urlsb = new StringBuilder();
    urlsb.append(url);
    urlsb.append("/" + userRole);
    urlsb.append("/" + start);
    urlsb.append("/" + size);

    String httpResult = null;
    try {
      StringBuffer result = HttpUtil.httpCall(Method.get, urlsb.toString(), null, null, null);
      httpResult = result.toString();
    } catch (Exception e) {
      logger.error("Fail to get lucker user info", e);
    }
    return httpResult;
  }

  public SimpleUser getUserInfo(Long userId) {
    SimpleUser userInfo = null;
    String url = accountHostURL + USER_INFO_URL + "/" + userId;
    // Map<String, Object> requestMap = new HashMap<String, Object>(1);
    // requestMap.put(DataConstant.USER_ID, userId);
    String httpResult = null;
    try {
      // StringBuffer sb = new StringBuffer();
      // String reqEntity = JSONConvertor.getJSONInstance().writeValueAsString(requestMap);
      // sb.append(reqEntity);
      StringBuffer result = HttpUtil.httpCall(Method.get, url, null, null, null);
      httpResult = result.toString();
    } catch (Exception e) {
      logger.error("Fail to get user info", e);
    }
    if (httpResult != null && !"".equals(httpResult)) {
      userInfo = JSONConvertor.getJSONInstance().fromJson(httpResult, SimpleUser.class);
    }
    return userInfo;
  }

  public Lucker getLuckerUserInfo(Long userId) {
    Lucker lucker = (Lucker) mariaDao.selectOne("lucker.getLuckerByUserID", userId);
    return lucker;
  }
  
  public Boolean checkLuckerExist(Long userId){
    Boolean result=true;
    Lucker lucker = getLuckerUserInfo(userId);
    if(lucker.getLuckerId()==null){
      lucker.setUserId(userId);
      int insertCount=mariaDao.insert("lucker.createLucker", lucker);
      if(insertCount==1){
        logger.info("Insert lucker into db!");
      }else{
        result=false;
      }
    }
    return result;
  }


  public Integer updateLuckerUser(Long luckerId, Map<String, Object> luckerMap, Map<String, Object> userMap) {
    luckerMap.put(DataConstant.USER_ID, luckerId);
    Integer updateCount = mariaDao.update("lucker.updateLucker", luckerMap);
    logger.info("Update lucker count:" + updateCount);
    if (userMap != null) {
      userMap.put(DataConstant.USER_ID, luckerId);
      Integer updateCount2 = mariaDao.update("lucker.updateUser", userMap);
      logger.info("Update user count:" + updateCount2);
    }
    return updateCount;
  }

  public Boolean sendSMS(String message) {
    return null;
  }

  public ServiceResult<String> listCities(String parentCode, Integer cityLevel) {
    ServiceResult<String> serviceResult = new ServiceResult<String>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put(DataConstant.PARENT_CODE, parentCode);
    queryMap.put(DataConstant.CITY_LEVEL, cityLevel);
    List<TripCityDict> resultList = null;
    switch (cityLevel) {
      case 1:
        resultList = mariaDao.selectList("city.findCityByCity2", queryMap);
        break;
      case 2:
        resultList = mariaDao.selectList("city.findCityByCountry", queryMap);
        break;
    }
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(resultList);
    serviceResult.setBusinessResult(jsonResult);
    return serviceResult;
  }

  public void setMariaDao(MariaDao mariaDao) {
    this.mariaDao = mariaDao;
  }


}
