package com.newroad.tripmaster.service.scenic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.CommentDao;
import com.newroad.tripmaster.dao.SiteDao;
import com.newroad.tripmaster.dao.TipsDao;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.dao.pojo.Site;
import com.newroad.tripmaster.dao.pojo.info.Comment;
import com.newroad.tripmaster.dao.pojo.info.Tips;
import com.newroad.tripmaster.service.InfoServiceIf;
import com.newroad.util.apiresult.ServiceResult;

public class InfoService implements InfoServiceIf {

  private static Logger logger = LoggerFactory.getLogger(InfoService.class);

  private TipsDao tipsDao;
  private SiteDao siteDao;
  private CommentDao commentDao;
  private CommonService  commonService;

  public ServiceResult<String> submitTipContent(Tips tips) {
    ServiceResult<String> result = new ServiceResult<String>();
    long currentTime = System.currentTimeMillis();
    tips.setCreatetime(currentTime);
    tips.setLastupdatedtime(currentTime);

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
      Long userId = tips.getUserid();
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
    comment.setCreatetime(currentTime);
    comment.setLastupdatedtime(currentTime);

    Object idObj = commentDao.submitComment(comment);
    Map<String, Object> map = new HashMap<String, Object>(1);
    map.put("commentId", idObj);
    String json = JSONConvertor.getJSONInstance().writeValueAsString(map);
    logger.info("Comment object id:" + json);
    result.setBusinessResult(json);
    return result;
  }

  public ServiceResult<String> listScenicComments(String hashsiteid) {
    ServiceResult<String> result = new ServiceResult<String>();
    List<Comment> commentList = commentDao.listComments(hashsiteid);
    for (Comment comment : commentList) {
      Long userId = comment.getUserid();
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

  public CommonService getCommonService() {
    return commonService;
  }

  public void setCommonService(CommonService commonService) {
    this.commonService = commonService;
  }

}