package com.newroad.data.statistics.action;

import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.data.statistics.analyzer.UserDataAnalyzer;
import com.newroad.data.statistics.datamodel.OverAllStatistics;
import com.newroad.data.tool.db.mysql.MySqlDao;
import com.newroad.data.tool.utils.UserStatisticsConstants;

public class DataAnalyzerTimeTask extends TimerTask {

  private static final Logger logger = LoggerFactory.getLogger(DataAnalyzerTimeTask.class);

  private MySqlDao mysqlDao;

  public DataAnalyzerTimeTask() {
    this.mysqlDao = new MySqlDao();
  }

  @Override
  public void run() {
    statisticsTask();
  }

  /**
   * 执行任务
   */
  public synchronized void statisticsTask() {
    Long lastStatisticsTimeMs = getLastStatisticsTime();
    Long statisticsTime = System.currentTimeMillis();
    logger.info("                ");
    logger.info("Data statistics task covers from start time " + new Date(lastStatisticsTimeMs)
        + " to end time " + new Date(statisticsTime));

    UserDataAnalyzer userAnalyzer =
        new UserDataAnalyzer(lastStatisticsTimeMs, statisticsTime, mysqlDao);
//    DataExceptionHandler.initLogFile(DataExceptionHandler.regUserFilePath);
//    userAnalyzer.regUserSyncStatistics();
//    
//    userAnalyzer.userCommonStatistics(OperateType.install);
//    userAnalyzer.userCommonStatistics(OperateType.activate);

//    DataExceptionHandler.initLogFile(DataExceptionHandler.startFilePath);
//    userAnalyzer.userCommonStatistics(OperateType.start);
//    DataExceptionHandler.initLogFile(DataExceptionHandler.stopFilePath);
//    userAnalyzer.userCommonStatistics(OperateType.stop);
//    DataExceptionHandler.initLogFile(DataExceptionHandler.loginFilePath);
//    userAnalyzer.userCommonStatistics(OperateType.login);
//    DataExceptionHandler.initLogFile(DataExceptionHandler.logoutFilePath);
//    userAnalyzer.userCommonStatistics(OperateType.logout);
//    DataExceptionHandler.initLogFile(DataExceptionHandler.syncFilePath);
//    userAnalyzer.userCommonStatistics(OperateType.synchronize);
//    DataExceptionHandler.initLogFile(DataExceptionHandler.commitFilePath);
//    userAnalyzer.userCommonStatistics(OperateType.commit);

    userAnalyzer.updateDeviceStatistics();
    userAnalyzer.updateRegUserLoginStatistics();
    userAnalyzer.updateRegUserSyncStatistics();
    userAnalyzer.updateUserDayStatistics();
    userAnalyzer.overAllStatistics();

//    MessageDataAnalyzer messageAnalyzer=new MessageDataAnalyzer(mysqlDao);
//    messageAnalyzer.viewMessageGroupQuery("122");
  }

  private Long getLastStatisticsTime() {
    OverAllStatistics last =
        (OverAllStatistics)mysqlDao.selectOne(UserStatisticsConstants.GET_MAX_OVERALL_STATISTICS,
            null);
    return last == null ? 0L : last.getStatisticsTime().getTime();
  }

}
