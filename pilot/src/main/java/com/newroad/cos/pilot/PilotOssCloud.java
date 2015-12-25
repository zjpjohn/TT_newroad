package com.newroad.cos.pilot;

import com.newroad.cos.pilot.util.LogGadget;

public final class PilotOssCloud {
  private PilotOssCloud() {

  }

  public static void setBaseUrl(String baseUrl, String connectorUrl) {
    OssManager.setBaseUrl(baseUrl, connectorUrl);
  }

  public static void setOssCloudLog(String logFile, int debugLevel) {
    LogGadget.logFile = logFile;
    LogGadget.debugLevel = debugLevel;
  }

  public static PilotOssEx CreateOssCloudEx() {
    return new PilotOssForLeNote();
  }

}
