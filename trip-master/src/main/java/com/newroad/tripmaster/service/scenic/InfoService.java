package com.newroad.tripmaster.service.scenic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.CommentDao;
import com.newroad.tripmaster.dao.SiteDao;
import com.newroad.tripmaster.dao.TipsDao;
import com.newroad.tripmaster.dao.UserBehaviorDao;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.dao.pojo.Site;
import com.newroad.tripmaster.dao.pojo.info.Comment;
import com.newroad.tripmaster.dao.pojo.info.Tips;
import com.newroad.tripmaster.dao.pojo.info.UserBehavior;
import com.newroad.tripmaster.service.InfoServiceIf;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.apiresult.ServiceResult;

public class InfoService implements InfoServiceIf {

  private static Logger logger = LoggerFactory.getLogger(InfoService.class);

  private SiteDao siteDao;

  private UserBehaviorDao userBehaviorDao;
  private TipsDao tipsDao;
  private CommentDao commentDao;
  private CommonService commonService;

  @Override
  public ServiceResult<String> submitCommonBehavior(UserBehavior userBehavior) {
    ServiceResult<String> result = new ServiceResult<String>();
    userBehavior.setStatus(1);
    long currentTime = System.currentTimeMillis();
    userBehavior.setCreateTime(currentTime);

    Object idObj = userBehaviorDao.saveUserBehavior(userBehavior);

    Map<String, Object> map = new HashMap<String, Object>(4);
    map.put(DataConstant.BEHAVIOR_ID, idObj.toString());
    map.put(DataConstant.ACTION_TYPE, userBehavior.getActionType());
    map.put(DataConstant.TARGET_ID, userBehavior.getTargetId());
    map.put(DataConstant.STATUS, 1);
    String json = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(json);
    return result;
  }

  @Override
  public ServiceResult<String> updateCommonBehaviorType(UserBehavior userBehavior, Integer updateType) {
    ServiceResult<String> result = new ServiceResult<String>();

    String targetId = userBehavior.getTargetId();
    int updateCount =
        userBehaviorDao.updateBehaviorActionType(targetId, userBehavior.getActionType(), userBehavior.getUserId(), updateType);
    logger.info("Update Common Behavior count:" + updateCount + " for targetId=" + targetId + " actionType=" + updateType);
    if (updateCount == 0) {
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to update Common Behavior because ObjectId is null!");
      return result;
    }
    Map<String, Object> map = new HashMap<String, Object>(3);
    map.put(DataConstant.ACTION_TYPE, updateType);
    map.put(DataConstant.TARGET_ID, targetId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public List<UserBehavior> listUserBehaviors(Long userId, Integer actionType, String targetId) {
    List<UserBehavior> userBehaviorList = userBehaviorDao.listUserBehaviors(userId, actionType, targetId, 1);
    // List<Comment> commentList = commentDao.listComments(null, userId, targetId);
    return userBehaviorList;
  }

  public  Map<String,UserBehavior> listUserBehaviorTarget(Long userId, Integer actionType) {
    List<UserBehavior> objList = userBehaviorDao.listUserBehaviorTarget(userId, actionType, 1);
    Map<String,UserBehavior> targetMap=new HashMap<String,UserBehavior>();
    for (UserBehavior userBehavior : objList) {
      targetMap.put(userBehavior.getTargetId(),userBehavior);
    }
    return targetMap;
  }

  public ServiceResult<String> submitTipContent(Tips tips) {
    ServiceResult<String> result = new ServiceResult<String>();
    long currentTime = System.currentTimeMillis();
    tips.setCreateTime(currentTime);
    tips.setUpdateTime(currentTime);

    Object idObj = tipsDao.saveTips(tips);
    Map<String, Object> map = new HashMap<String, Object>(1);
    map.put("tipsId", idObj);
    String json = JSONConvertor.getJSONInstance().writeValueAsString(map);
    logger.info("Tips object id:" + json);
    result.setBusinessResult(json);
    return result;
  }

  public ServiceResult<String> getUserTips(Long userId, String hashsiteid) {
    ServiceResult<String> result = new ServiceResult<String>();
    List<Tips> tipList = tipsDao.getUserTips(userId, hashsiteid);

    String json = JSONConvertor.getJSONInstance().writeValueAsString(tipList);
    logger.info("Get user tips:" + json);
    result.setBusinessResult(json);
    return result;
  }

  /**
   * support or reject tips with track user behavior
   */
  public ServiceResult<String> submitTipOpinion(Tips tips) {
    ServiceResult<String> result = new ServiceResult<String>();
    Object resultObj = tipsDao.supportTips(tips);
    logger.info("support tips:" + resultObj);
    Map<String, Object> map = new HashMap<String, Object>();
    if (resultObj != null) {
      map.put("status", 1);
    } else {
      map.put("status", 0);
    }
    String json = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(json);
    return result;
  }

  public ServiceResult<String> listScenicTips(String hashsiteid) {
    ServiceResult<String> result = new ServiceResult<String>();
    Site site = siteDao.getSiteByHash(hashsiteid);
    List<Tips> tipList = tipsDao.listScenicTips(hashsiteid);
    for (Tips tips : tipList) {
      Long userId = tips.getUserId();
      SimpleUser userInfo = commonService.getUserInfo(userId);
      tips.setUserInfo(userInfo);
    }
    String json = JSONConvertor.getJSONInstance().writeValueAsString(tipList);
    logger.info("Get tips list:" + json);
    result.setBusinessResult(json);
    return result;
  }

  /**
   * submit comment with user behavior
   */
  public ServiceResult<String> submitComment(Comment comment) {
    ServiceResult<String> result = new ServiceResult<String>();
    long currentTime = System.currentTimeMillis();
    comment.setCreateTime(currentTime);
    comment.setUpdateTime(currentTime);

    Object idObj = commentDao.submitComment(comment);
    Map<String, Object> map = new HashMap<String, Object>(2);
    map.put("commentId", idObj);
    map.put(DataConstant.ACTION_TYPE, comment.getActionType());
    map.put(DataConstant.TARGET_ID, comment.getTargetId());
    String json = JSONConvertor.getJSONInstance().writeValueAsString(map);
    logger.info("Comment object id:" + json);
    result.setBusinessResult(json);
    return result;
  }

  @Override
  public ServiceResult<String> listComments(Integer type, String targetId) {
    ServiceResult<String> result = new ServiceResult<String>();
    List<Comment> commentList = commentDao.listComments(type, null, targetId);
    for (Comment comment : commentList) {
      Long userId = comment.getUserId();
      SimpleUser userInfo = commonService.getUserInfo(userId);
      comment.setUserInfo(userInfo);
    }
    String json = JSONConvertor.getJSONInstance().writeValueAsString(commentList);
    logger.info("Get comment list:" + json);
    result.setBusinessResult(json);
    return result;
  }

  public void setTipsDao(TipsDao tipsDao) {
    this.tipsDao = tipsDao;
  }

  public void setSiteDao(SiteDao siteDao) {
    this.siteDao = siteDao;
  }

  public void setCommentDao(CommentDao commentDao) {
    this.commentDao = commentDao;
  }

  public void setCommonService(CommonService commonService) {
    this.commonService = commonService;
  }

  public void setUserBehaviorDao(UserBehaviorDao userBehaviorDao) {
    this.userBehaviorDao = userBehaviorDao;
  }



}
