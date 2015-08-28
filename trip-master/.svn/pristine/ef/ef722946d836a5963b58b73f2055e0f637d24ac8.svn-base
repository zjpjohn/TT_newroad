package com.newroad.tripmaster.dao.aliyun;

import com.aliyun.openservices.ots.OTSClient;

public class AliYunOTSManager {
  //Aliyun server connection endPoint
  private static final String internetEndPoint = "http://DevDisMongo.cn-hangzhou.ots-internal.aliyuncs.com";
  private static final String endPoint = "http://DevDisMongo.cn-hangzhou.ots.aliyuncs.com";
  private static final String accessId = "Cso8ssTWnX8LBsBL";
  private static final String accessKey = "PEJLBqmO5xBjMWvPVdXrfx09ZkSXpG";
  private static final String instanceName = "DevDisMongo";

  private static OTSClient otsInstance=null;
  private AliYunOTSManager() {

  }

  public static OTSClient getOTSInstance() {
    if(otsInstance==null){
      synchronized(AliYunOTSManager.class){
        otsInstance=new OTSClient(endPoint, accessId, accessKey, instanceName);
      }
    }
    return otsInstance;
  }
}
