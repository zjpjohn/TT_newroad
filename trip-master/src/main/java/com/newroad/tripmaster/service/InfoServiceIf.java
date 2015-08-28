package com.newroad.tripmaster.service;

import com.newroad.tripmaster.dao.pojo.info.Comment;
import com.newroad.tripmaster.dao.pojo.info.Tips;
import com.newroad.tripmaster.dao.pojo.info.UserBehavior;
import com.newroad.util.apiresult.ServiceResult;


public interface InfoServiceIf {

  public ServiceResult<String> submitCommonBehavior(UserBehavior userBehavior);

  public ServiceResult<String> updateCommonBehaviorType(UserBehavior userBehavior, Integer updateType);

  public ServiceResult<String> submitTipContent(Tips tips);

  public ServiceResult<String> submitTipOpinion(Tips tips);

  public ServiceResult<String> getUserTips(Long userId, String hashsiteid);

  public ServiceResult<String> listScenicTips(String hashsiteid);

  public ServiceResult<String> submitComment(Comment comment);

  public ServiceResult<String> listComments(Integer commentType, String targetId);
}
