package com.newroad.tripmaster.service.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.dao.UserBehaviorDao;
import com.newroad.tripmaster.dao.pojo.info.UserBehavior;

@Aspect
public class TrackAspectService {

  private static Logger logger = LoggerFactory.getLogger(TrackAspectService.class);

  private UserBehaviorDao userBehaviorDao;

  @Pointcut("execution(* com.newroad.tripmaster.service.scenic.InfoService.submit*(..))")
  public void trackUserAction() {
    logger.info("Track User Action");
  }

  public String saveUserBehavior(UserBehavior userBehavior) {
    long currentTime = System.currentTimeMillis();
    userBehavior.setCreateTime(currentTime);

    Object idObj = userBehaviorDao.saveUserBehavior(userBehavior);
    logger.info("save user behavior id:" + idObj);
    return idObj.toString();
  }

  public void setUserBehaviorDao(UserBehaviorDao userBehaviorDao) {
    this.userBehaviorDao = userBehaviorDao;
  }
 
  
}
