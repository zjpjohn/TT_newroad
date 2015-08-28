package com.newroad.data.statistics.analyzer;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.data.statistics.datamodel.DeviceRecord;
import com.newroad.data.statistics.datamodel.OperateType;
import com.newroad.data.statistics.datamodel.OverAllStatistics;
import com.newroad.data.statistics.datamodel.RegisterUserRecord;
import com.newroad.data.statistics.datamodel.UserDayStatistics;
import com.newroad.data.statistics.datamodel.UserLoginStatistics;
import com.newroad.data.tool.db.mongo.MongoConnectionFactory;
import com.newroad.data.tool.db.mysql.MySqlDao;
import com.newroad.data.tool.utils.UserStatisticsConstants;
import com.mongodb.DB;

public class UserDataAnalyzer implements DataAnalyzerIf {

  private static final Logger logger = LoggerFactory.getLogger(UserDataAnalyzer.class);

  private Long startStatisticsTime = 0L;
  private Long endStatisticsTime = 0L;

  public final static Integer QUERY_LIMIT = 10000;
  private DB db;
  private MySqlDao mysqlDao;

  public static int THREAD_NUM = 150;
  public static AtomicInteger executeThreadCount = new AtomicInteger(0);

  private static ExecutorService threadPool = new ThreadPoolExecutor(THREAD_NUM, 1000, 600, TimeUnit.SECONDS,
      new LinkedBlockingQueue<Runnable>(150), new ThreadPoolExecutor.DiscardOldestPolicy());

  public UserDataAnalyzer(Long startStatisticsTime, Long endStatisticsTime, MySqlDao mysqlDao) {
    logger.info("The statistics start time:" + startStatisticsTime + ",end time:" + endStatisticsTime);
    this.startStatisticsTime = startStatisticsTime;
    this.endStatisticsTime = endStatisticsTime;
    this.db = MongoConnectionFactory.getDefaultDB();
    this.mysqlDao = mysqlDao;

    executeThreadCount.set(0);
  }

  /**
   * get the latest register user info from ln_user and ln_user_statistics collections
   */
  public void regUserSyncStatistics() {
    Set<Future<Integer>> futureSet = new HashSet<Future<Integer>>();
    Long operateCount = 0L;
    try {
      operateCount = UserStatisticsToolkit.countRegUserStatistics(startStatisticsTime, endStatisticsTime);
      Long round = operateCount / QUERY_LIMIT + 1;
      int roundInt = round.intValue();

      CountDownLatch threadSignal = new CountDownLatch(roundInt);
      int count = 0;
      while (count * QUERY_LIMIT < operateCount) {
        int startIndex = count * QUERY_LIMIT;
        Future<Integer> future =
            threadPool.submit(new RegUserSyncTask(threadSignal, count, db, startStatisticsTime, endStatisticsTime, startIndex, QUERY_LIMIT,
                mysqlDao));
        futureSet.add(future);
        count++;
      }
      threadSignal.await();
    } catch (Exception e) {
      logger.error("regUserSyncStatistics Exception:" + e);
    } finally {
      logger.info("regUserSyncStatistics completed...");
    }
  }
  
  
  public void userCommonStatistics(OperateType operateType) {
//    Set<Future<Integer>> futureSet = new HashSet<Future<Integer>>();

    Long operateCount = null;
    try {
      operateCount = UserStatisticsToolkit.countOperateStatistics(operateType, startStatisticsTime, endStatisticsTime);
      Long round = operateCount / QUERY_LIMIT + 1;
      int roundInt = round.intValue();
      CountDownLatch threadSignal = new CountDownLatch(roundInt);
      int count = 0;
      while (count * QUERY_LIMIT < operateCount) {
        int startIndex = count * QUERY_LIMIT;
//        Future<Integer> future =
//            threadPool.submit(new UserStatisticsLoadTask(threadSignal, count, db, operateType, startStatisticsTime, endStatisticsTime,
//                startIndex, QUERY_LIMIT, mysqlDao));
//        futureSet.add(future);
        UserStatisticsLoadTask loadTask= new UserStatisticsLoadTask(threadSignal, count, db, operateType, startStatisticsTime, endStatisticsTime,
          startIndex, QUERY_LIMIT, mysqlDao);
        loadTask.call();
        count++;
      }
      //threadSignal.await();
    } catch (Exception e) {
      logger.error("userCommonStatistics Exception:" + e);
    } finally {
      logger.info("userCommonStatistics OperateType=" + operateType + " completed...");
    }
  }



  public void updateDeviceStatistics() {
    DeviceRecord deviceRecord = new DeviceRecord();
    deviceRecord.setCreateTime(startStatisticsTime);
    deviceRecord.setIsUsable(1);
    int updateRecord = mysqlDao.update(UserStatisticsConstants.UPDATE_ACTIVE_DEVICE_USABLE, deviceRecord);
    logger.info("update device usable status in oms_device_statistics data count " + updateRecord);
  }


  @SuppressWarnings("unchecked")
  public void updateRegUserLoginStatistics() {
    Map<String, Date> map = new HashMap<String, Date>();
    map.put("startStatisticsTime", new Date(startStatisticsTime));
    map.put("endStatisticsTime", new Date(endStatisticsTime));
    List<String> userList = (List<String>) mysqlDao.selectList(UserStatisticsConstants.GETT_LOGIN_USER_LIST, map, 0, Integer.MAX_VALUE);
    if (userList != null && userList.size() > 0) {
      for (String userID : userList) {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("userId", userID);
        userMap.put("endStatisticsTime", new Date(endStatisticsTime));
        Long loginCount = (Long) mysqlDao.selectOne(UserStatisticsConstants.COUNT_USER_LOGIN_RECORD, userMap);
        UserLoginStatistics lastLoginData =
            (UserLoginStatistics) mysqlDao.selectOne(UserStatisticsConstants.GET_LAST_USER_LOGIN_TIME, userMap);

        RegisterUserRecord registUser = new RegisterUserRecord();
        registUser.setUserId(userID);
        registUser.setUserLoginNum(loginCount.intValue());
        registUser.setLastLoginTime(lastLoginData.getLoginTime());
        registUser.setSystemTime(new Date());
        int updateRecord = mysqlDao.update(UserStatisticsConstants.UPDATE_REGUSER_LOGIN_STATISTICS, registUser);
        if (updateRecord == 0) {
          logger.error("Couldn't update reguser login data userID:" + userID);
        }
      }
    }
    logger.info("update oms_reguser_statistics data for login data.");

  }

  @SuppressWarnings("unchecked")
  public void updateRegUserSyncStatistics() {
    Map<String, Date> map = new HashMap<String, Date>();
    map.put("startStatisticsTime", new Date(startStatisticsTime));
    map.put("endStatisticsTime", new Date(endStatisticsTime));
    List<String> userList2 = (List<String>) mysqlDao.selectList(UserStatisticsConstants.GET_SYNC_USER_LIST, map, 0, Integer.MAX_VALUE);
    if (userList2 != null && userList2.size() > 0) {
      for (String userID : userList2) {

        RegisterUserRecord registUser = new RegisterUserRecord();
        registUser.setUserId(userID);
        Long userNoteCount =
            UserStatisticsToolkit.countNoteStatisticsByUser(UserStatisticsConstants.COLLECTION_NOTE, userID, endStatisticsTime);
        Long userCategoryCount =
            UserStatisticsToolkit.countNoteStatisticsByUser(UserStatisticsConstants.COLLECTION_CATE, userID, endStatisticsTime);
        Long userTagCount =
            UserStatisticsToolkit.countNoteStatisticsByUser(UserStatisticsConstants.COLLECTION_TAG, userID, endStatisticsTime);
        Long userResourceCount =
            UserStatisticsToolkit.countNoteStatisticsByUser(UserStatisticsConstants.COLLECTION_RESOURCE, userID, endStatisticsTime);
        Integer userSyncCount = UserStatisticsToolkit.countSyncStatisticsByUser(userID, endStatisticsTime);
        Long maxUserSyncTime = UserStatisticsToolkit.maxSyncTimeByUser(userID, endStatisticsTime);

        registUser.setUserNoteCount(userNoteCount);
        registUser.setUserCategoryCount(userCategoryCount);
        registUser.setUserTagCount(userTagCount);
        registUser.setUserResourceCount(userResourceCount);
        registUser.setUserSyncNum(userSyncCount);
        registUser.setLastSyncTime(maxUserSyncTime);
        registUser.setSystemTime(new Date());
        int updateRecord = mysqlDao.update(UserStatisticsConstants.UPDATE_REGUSER_SYNC_STATISTICS, registUser);
        if (updateRecord == 0) {
          logger.error("Couldn't update reguser sync data userID:" + userID);
        }
      }
    }
    logger.info("update oms_reguser_statistics data for sync data.");
  }

  @SuppressWarnings("unchecked")
  public void updateUserDayStatistics() {
    Map<String, Date> map = new HashMap<String, Date>();
    map.put("startStatisticsTime", new Date(startStatisticsTime));
    map.put("endStatisticsTime", new Date(endStatisticsTime));
    List<UserDayStatistics> userDayStatisticsList =
        (List<UserDayStatistics>) mysqlDao.selectList(UserStatisticsConstants.SELECT_USER_DAY_STATISTICS, map, 0, Integer.MAX_VALUE);
    for (UserDayStatistics userDayStatistics : userDayStatisticsList) {
      Map<String, Object> userMap = new HashMap<String, Object>();
      String userId = userDayStatistics.getUserId();
      Date clientDataTime = userDayStatistics.getClientDataTime();
      Long oneDay = 1000 * 60 * 60 * 24L;
      Date startClientDataTime = new Date(clientDataTime.getTime() - oneDay);
      userMap.put("userId", userDayStatistics.getUserId());
      userMap.put("startStatisticsTime", startClientDataTime);
      userMap.put("endStatisticsTime", clientDataTime);
      Integer loginDayCount = (Integer) mysqlDao.selectOne(UserStatisticsConstants.COUNT_USER_LOGIN_RECORD, userMap);
      Integer syncDayCount = (Integer) mysqlDao.selectOne(UserStatisticsConstants.COUNT_USER_SYNC_RECORD, userMap);

      UserDayStatistics dayStatistics = new UserDayStatistics();
      dayStatistics.setUserId(userId);
      dayStatistics.setDayLoginNum(loginDayCount);
      dayStatistics.setDaySyncNum(syncDayCount);
      dayStatistics.setClientDataTime(clientDataTime);
      dayStatistics.setSystemTime(new Date());
      int updateRecord = mysqlDao.update(UserStatisticsConstants.UPDATE_DAY_USER_NUM, dayStatistics);
      if (updateRecord == 0) {
        logger.error("Couldn't update user day statistics userID:" + userId);
      }
    }
  }

  public void overAllStatistics() {
    // statistics mongoDB oplog record.
    Map<String, Date> map = new HashMap<String, Date>();
    map.put("endStatisticsTime", new Date(endStatisticsTime));
    Long activateUserNum = (Long) mysqlDao.selectOne(UserStatisticsConstants.COUNT_USER_ACTIVE_RECORD, map);
    Long registerUserNum = (Long) mysqlDao.selectOne(UserStatisticsConstants.COUNT_REGISTER_USER_RECORD, map);
    Long offlineUserNum = (Long) mysqlDao.selectOne(UserStatisticsConstants.COUNT_USER_DAY_STATISTICS, map);
    Long loginUserNum = (Long) mysqlDao.selectOne(UserStatisticsConstants.COUNT_LOGIN_USER_NUM, map);
    Long syncUserNum = UserStatisticsToolkit.countUserSyncStatistics(endStatisticsTime).longValue();

    Long allNoteCount = UserStatisticsToolkit.countNoteStatisticsByUser(UserStatisticsConstants.COLLECTION_NOTE, null, endStatisticsTime);
    Long allResourceCount =
        UserStatisticsToolkit.countNoteStatisticsByUser(UserStatisticsConstants.COLLECTION_RESOURCE, null, endStatisticsTime);

    Long executeTime = System.currentTimeMillis() - endStatisticsTime;
    OverAllStatistics overAllStatis =
        new OverAllStatistics(activateUserNum, registerUserNum, offlineUserNum, loginUserNum, syncUserNum, allNoteCount, allResourceCount,
            executeTime, endStatisticsTime);
    mysqlDao.insert(UserStatisticsConstants.INSERT_OVERALL_STATISTICS, overAllStatis);
    logger.info("New oms_overall_statistics record from startTime:" + startStatisticsTime + " to endTime:" + endStatisticsTime
        + " and executeTime is " + executeTime);
  }



}
