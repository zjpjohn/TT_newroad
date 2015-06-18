package com.newroad.cache.common.couchbase;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.ConnectionObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactory;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
import com.newroad.cache.common.CacheManager;

/**
 * @info CouchbaseManager
 * @author tangzj1
 * @date Jan 13, 2014
 * @version
 */
public class CouchbaseManager implements CacheManager {

  private static Logger logger = LoggerFactory.getLogger(CouchbaseManager.class);

  static {
    System.setProperty("net.spy.log.LoggerImpl", "net.spy.memcached.compat.log.SunLogger");
  }

  private ThreadLocal<CouchbaseClient> local = new ThreadLocal<CouchbaseClient>();
  
  protected CouchbaseClient cacheClient;
  
  private String[] nodeList;
  private String bucketName;
  private String password;
  private long opTimeout;
  private long opQueueMaxBlockTime;
  private long reconnect = 1000;

  protected CouchbaseConnectionFactory connectionFactory;

  CouchbaseManager(String nodeList, String bucketName, String password, long opTimeout, long opQueueMaxBlockTime, long reconnect){
    this.nodeList=nodeList.split(",");
    this.bucketName = bucketName;
    this.password = password;
    this.opTimeout = opTimeout;
    this.opQueueMaxBlockTime = opQueueMaxBlockTime;
    this.reconnect = reconnect;
  }
  
  public void buildCouchbaseConnection() {
    List<URI> baseURIs = new ArrayList<URI>();
    try {
      for (String serverAddress : nodeList) {
        URI base = new URI(String.format("http://%s/pools", serverAddress));
        baseURIs.add(base);
      }
      CouchbaseConnectionFactoryBuilder cfb = new CouchbaseConnectionFactoryBuilder();
      cfb.setOpTimeout(opTimeout);
      // wait up to 10 seconds for an operation to succeed
      cfb.setOpQueueMaxBlockTime(opQueueMaxBlockTime);
      // wait up to 5 seconds when trying to enqueue an operation
      cfb.setReconnectThresholdTime(reconnect, TimeUnit.MILLISECONDS);
      connectionFactory = cfb.buildCouchbaseConnection(baseURIs, bucketName, password);
      logger.info("buildCouchbaseConnection in Couchbase Cluster is starting...");
    } catch (URISyntaxException e) {
      logger.error("buildCouchbaseConnection server address URISyntaxException:", e);
    } catch (IOException e) {
      logger.error("buildCouchbaseConnection IOException:", e);
    }
  }

  @Override
  public void init() throws Exception {
    buildCouchbaseConnection();
    if (connectionFactory == null) {
      logger.error("CouchbaseClient connectionFactory is null!");
      return;
    }
    cacheClient = new CouchbaseClient(connectionFactory);
    cacheClient.addObserver(new ConnectionObserver() {
      public void connectionLost(SocketAddress sa) {
        logger.info("Connection lost to " + sa.toString());
      }

      public void connectionEstablished(SocketAddress sa, int reconnectCount) {
        logger.info("Connection established with " + sa.toString() + ",Reconnected count: " + reconnectCount);
      }
    });
  }

  public void destroy() {
    cacheClient.shutdown(60, TimeUnit.SECONDS);
    logger.info("Disconnecting from Couchbase Cluster...");
  }

  public CouchbaseClient getInstance() {
    cacheClient = local.get();
    if (cacheClient == null) {
      try {
        init();
        local.set(cacheClient);
      } catch (Exception e) {
        logger.error("CouchbaseClient getInstance Exception:", e);
      }
    }
    return cacheClient;
  }

}
