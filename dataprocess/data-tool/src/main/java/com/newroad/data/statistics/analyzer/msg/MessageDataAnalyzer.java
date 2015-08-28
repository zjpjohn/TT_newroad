package com.newroad.data.statistics.analyzer.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cache.common.couchbase.CouchbaseCache;
import com.newroad.data.statistics.analyzer.DataAnalyzerIf;
import com.newroad.data.tool.db.couchbase.CouchbaseManager;
import com.newroad.data.tool.db.mysql.MySqlDao;

public class MessageDataAnalyzer implements DataAnalyzerIf {

  private static final Logger logger = LoggerFactory.getLogger(MessageDataAnalyzer.class);

  private CouchbaseCache cache;
  private MySqlDao mysqlDao;

  public MessageDataAnalyzer(MySqlDao mysqlDao) {
    super();
    this.cache = CouchbaseManager.getInstance();
    this.mysqlDao = mysqlDao;
  }

  private void messageGroupStatistics(MessageGroupQuery grourpQuery, String messageID) {
    grourpQuery.messageGroupStatistics(cache, mysqlDao, messageID);
  }

  public void viewMessageGroupQuery(String messageID) {
    logger.info("viewMessageGroupQuery included version/channel/deviceType group start... ");
    MessageGroupQuery[] algorithms = {new MessageVersionGroupQuery(), new MessageChannelGroupQuery(), new MessageDeviceTypeGroupQuery()};
    for (int i = 0; i < algorithms.length; i++) {
      messageGroupStatistics(algorithms[i], messageID);
    }
  }

}
