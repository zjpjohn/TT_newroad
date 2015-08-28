package com.newroad.tripmaster.info.crawler.dbmanager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.newroad.tripmaster.info.crawler.exception.MongoDaoException;


/**
 * @info : Mongo DB Manager
 * @author: tangzj1
 * @data : 2013-10-10
 * @since : 2.0
 */
public class MongoManager extends AbstractFactoryBean<Mongo>{

  private static Logger log = LoggerFactory.getLogger(MongoManager.class);

  private String[] nodeiplist = null;
  private String[] nodeportlist = null;
  private String dbName = null;
  private String userName = null;
  private String passWord = null;
  private Integer connectionsPerHost;
  private Integer threadsAllowedToBlock;
  private Integer connectionTimeOut = 15 * 1000;
  private Integer maxRetryTime = 15 * 1000;
  private Integer socketTimeOut = 60 * 1000;
  private List<ServerAddress> serverAddressList;

  private static Mongo mongoClient;

  private MongoManager() {
    super();
    new MongoManager(nodeiplist, nodeportlist, dbName, userName, passWord, connectionsPerHost, threadsAllowedToBlock, connectionTimeOut,
        maxRetryTime, socketTimeOut);
  }

  private MongoManager(String[] nodeiplist, String[] nodeportlist, String dbName, String userName, String passWord,
      Integer connectionsPerHost, Integer threadsAllowedToBlock, Integer connectionTimeOut, Integer maxRetryTime, Integer socketTimeOut) {
    if (mongoClient == null) {
      this.nodeiplist = nodeiplist;
      this.nodeportlist = nodeportlist;
      this.dbName = dbName;
      this.userName = userName;
      this.passWord = passWord;
      this.connectionsPerHost = connectionsPerHost;
      this.threadsAllowedToBlock = threadsAllowedToBlock;
      this.connectionTimeOut = connectionTimeOut;
      this.maxRetryTime = maxRetryTime;
      this.socketTimeOut = socketTimeOut;
      serverAddressList = new ArrayList<ServerAddress>();
      try {
        for (int i = 0; i < nodeiplist.length; i++) {
          serverAddressList.add(new ServerAddress(nodeiplist[i], Integer.parseInt(nodeportlist[i])));
        }
      } catch (NumberFormatException e) {
        // TODO Auto-generated catch block
        log.error("MongoManager NumberFormatException:" + e);
      } catch (UnknownHostException e) {
        // TODO Auto-generated catch block
        log.error("MongoManager UnknownHostException:" + e);
      }
      mongoInit();
    }
  }

  @Override
  protected Mongo createInstance() throws Exception {
    if (mongoClient == null) {
      synchronized (MongoManager.class) {
        new MongoManager();
      }
    }
    return mongoClient;
  }

  private void mongoInit() {
    MongoClientOptions.Builder builder =
        MongoClientOptions.builder().connectionsPerHost(connectionsPerHost)
            .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlock).connectTimeout(connectionTimeOut)
            .socketTimeout(socketTimeOut);
    if (maxRetryTime > 0) {
      builder.connectTimeout(maxRetryTime);
    }

    int nodes = serverAddressList.size();
    MongoCredential credential = MongoCredential.createMongoCRCredential(userName, dbName, passWord.toCharArray());
    if (nodes <= 0) {
      mongoClient = null;
      throw new MongoDaoException("Couldn't get available mongo server address!");
    } else if (nodes == 1) {
      mongoClient = new MongoClient(serverAddressList.get(0), Arrays.asList(credential), builder.build());
    } else {
      mongoClient = new MongoClient(serverAddressList, Arrays.asList(credential), builder.build());
    }
//    can't authenticate twice on the same database
//    DB dbpool = mongoClient.getDB(dbName);
//    if (!dbpool.authenticate(userName, passWord.toCharArray())) {
//      log.error("Authentication failure!userName:" + userName + ",dbName:" + dbName + ",passWord:" + passWord);
//      throw new MongoDaoException("DB Connection failed because of authentication failure!");
//    }
  }

  @Override
  public Class<?> getObjectType() {
    return MongoManager.class;
  }
  
  private ThreadLocal<DB> local = new ThreadLocal<DB>();

  public DB getDB() {
    DB db = local.get();
    if (db != null) {
      return db;
    }
    return mongoClient.getDB(dbName);
  }

  public void closeDB() {
    if (mongoClient != null) {
      mongoClient.close();
    }
  }
}
