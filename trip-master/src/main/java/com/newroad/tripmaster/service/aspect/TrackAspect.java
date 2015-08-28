package com.newroad.tripmaster.service.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.dao.TrackDao;
import com.newroad.tripmaster.dao.pojo.info.UserBehavior;

@Aspect
public class TrackAspect {
  
  private static Logger logger = LoggerFactory.getLogger(TrackAspect.class);
  
  private TrackDao trackDao;
  
  @Pointcut("execution(* com.newroad.tripmaster.service.scenic.InfoService.submit*(..))")  
  public void trackUserAction(){
    logger.info("Track User Action");
  }  
  
  
  public void saveUserBehavior(UserBehavior userBehavior) {
    long currentTime = System.currentTimeMillis();
    userBehavior.setCreateTime(currentTime);
    userBehavior.setUpdateTime(currentTime);

    Object idObj = trackDao.saveUserBehavior(userBehavior);
    logger.info("save user behavior id:" + idObj);
  }
}
