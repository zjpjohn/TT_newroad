package com.newroad.cos.pilot.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogGadget {
  public static String logFile = null;
  public static int debugLevel = 0;
  public static String logTag = "pilotSDK";
  public static final long logFileMaxSize = (1024 * 1024 * 4);

  private LogGadget() {

  }

  private static void log(String Log) {
    File f = new File(logFile);
    if (!f.exists()) {
      try {
        f.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
        logFile = null;
        debugLevel = 0;
        return;
      }
    }

    if (f.length() > logFileMaxSize) {
      f.delete();
      try {
        f.createNewFile();
      } catch (IOException e) {
        // should never happen
        e.printStackTrace();
        logFile = null;
        debugLevel = 0;
        return;
      }
    }

    try {
      FileWriter w = new FileWriter(logFile, true);
      String out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
      out += " " + logTag + ":" + Log + "\n";
      w.write(out);
      w.flush();
      w.close();
    } catch (IOException e) {
      e.printStackTrace();
      logFile = null;
      debugLevel = 0;
      return;
    }
  }

  public static void d(String Log) {
    if (debugLevel > 1) {
      log(Log);
    }
  }

  public static void e(String Log) {
    if (debugLevel > 0) {
      log(Log);
    }
  }
}
