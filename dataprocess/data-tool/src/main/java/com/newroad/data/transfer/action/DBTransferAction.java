package com.newroad.data.transfer.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.data.tool.db.mongo.MongoConnectionFactory;
import com.newroad.data.tool.utils.PropertiesUtils;
import com.newroad.data.transfer.upgrade.DBUpgradeExecutor;

/**
 * @author tangzj1
 * @version 2.0
 * @since May 9, 2014
 */
public class DBTransferAction {

  private static Logger logger = LoggerFactory.getLogger(DBTransferAction.class);
  
  /**
   * @Title: main
   * @Description: TODO
   * @param @param args
   * @return void
   * @throws
   */
  public static void main(String[] args) {
//     DBDictionaryUpgrade dbDict = new DBDictionaryUpgrade();
//     dbDict.upgradeDictionaryV2();
    long start=System.currentTimeMillis();
    logger.info("Start data transfer V2 task at time:"+start);
    DBUpgradeExecutor dbNote = new DBUpgradeExecutor(MongoConnectionFactory.dbName1, MongoConnectionFactory.dbName2,PropertiesUtils.userListFolderPath);
    dbNote.importUserV2();
    dbNote.executeFileDBUpgrade();
    //dbNote.checkData();
  }

}
