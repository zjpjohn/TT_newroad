package com.newroad.tripmaster.dao;

import com.aliyun.openservices.ots.OTSClient;
import com.newroad.tripmaster.dao.aliyun.AliYunOTSManager;
import com.newroad.tripmaster.dao.aliyun.OTSCommonDao;

public class TestOTSOperator {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    String tableName="tripMasterTest";
    OTSClient client=AliYunOTSManager.getOTSInstance();
    OTSCommonDao commonDao=new OTSCommonDao(client);
    //commonDao.createTable(tableName);
    commonDao.listTable();
    commonDao.putRow(tableName, null);
    commonDao.getRow(tableName);
  }

}
