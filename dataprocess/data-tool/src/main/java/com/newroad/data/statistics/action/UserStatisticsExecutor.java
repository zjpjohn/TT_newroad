package com.newroad.data.statistics.action;

import java.util.Timer;
import java.util.TimerTask;

public class UserStatisticsExecutor {

  public static void main(String[] args) {
//    TimerTask statisticsTask = new DataAnalyzerTimeTask();
//    Timer timer = new Timer();
//    timer.scheduleAtFixedRate(statisticsTask, 0, 1000 * 60 * 60 * 24);
    DataAnalyzerTimeTask timeTask=new DataAnalyzerTimeTask();
    timeTask.statisticsTask();
  }

}
