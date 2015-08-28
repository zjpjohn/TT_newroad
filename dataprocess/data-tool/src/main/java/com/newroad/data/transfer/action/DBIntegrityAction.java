package com.newroad.data.transfer.action;

import com.newroad.data.tool.db.mongo.MongoConnectionFactory;
import com.newroad.data.tool.utils.PropertiesUtils;
import com.newroad.data.transfer.upgrade.DataIntegrityChecker;

public class DBIntegrityAction {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    DataIntegrityChecker checker =
        new DataIntegrityChecker(MongoConnectionFactory.dbName1, MongoConnectionFactory.dbName2, PropertiesUtils.errorListFilePath,
            PropertiesUtils.missListFilePath,PropertiesUtils.invalidUserListFilePath);
    checker.checkDefaultDataIntegrity();
    checker.checkOplogIntegrity();
    //checker.syncOplogIntegrity();
  }

}
