package com.newroad.cache.common.couchbase;



/**
 * @info
 * @author tangzj1
 * @date Jan 17, 2014
 * @version
 */
public class CouchbaseProperties {

  private static String[] nodeList;
  private static String bucketName;
  private static String password;
  private static long opTimeout;
  private static long opQueueMaxBlockTime;
  private static long reconnect = 1000;

  public CouchbaseProperties(String nodeList, String bucketName, String password, long opTimeout, long opQueueMaxBlockTime, long reconnect) {
    CouchbaseProperties.nodeList = nodeList.split(",");
    CouchbaseProperties.bucketName = bucketName;
    CouchbaseProperties.password = password;
    CouchbaseProperties.opTimeout = opTimeout;
    CouchbaseProperties.opQueueMaxBlockTime = opQueueMaxBlockTime;
    CouchbaseProperties.reconnect = reconnect;
  }

  public static String[] getNodeList() {
    return nodeList;
  }

  public static String getBucketName() {
    return bucketName;
  }

  public static String getPassword() {
    return password;
  }

  public static long getOpTimeout() {
    return opTimeout;
  }

  public static long getOpQueueMaxBlockTime() {
    return opQueueMaxBlockTime;
  }

  public static long getReconnect() {
    return reconnect;
  }

}
