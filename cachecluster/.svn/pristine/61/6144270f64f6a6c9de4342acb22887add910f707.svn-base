package com.newroad.cache.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

  private static Logger logger = LoggerFactory.getLogger(RedisManager.class);

  // Redis服务器IP
  // private static String address = "139.196.27.176";
  // // Redis的端口号
  // private static int port = 6379;
  // // 访问密码
  // private static String password = "xingzhe";
  // // 可用连接实例的最大数目，默认值为8；
  // // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
  // private static int max_active = 1024;
  // // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
  // private static int max_idle = 200;
  // // 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
  // private static int max_wait_time = 10000;
  // private static int timeout = 10000;
  // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
  private static boolean test_on_borrow = true;

  private static JedisPool jedisPool = null;

  // /**
  // * 初始化Redis连接池
  // */
  // static {
  // try {
  // JedisPoolConfig config = new JedisPoolConfig();
  // config.setMaxTotal(max_active);
  // config.setMaxIdle(max_idle);
  // config.setMaxWaitMillis(max_wait_time);
  // config.setTestOnBorrow(test_on_borrow);
  // jedisPool = new JedisPool(config, address, port, timeout, password);
  // } catch (Exception e) {
  // logger.error("Fail to initilize jedis instance!");
  // }
  // }

  static void init(String address, int port, String password, int max_active, int max_idle, int max_wait_time, int timeout) {
    try {
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxTotal(max_active);
      config.setMaxIdle(max_idle);
      config.setMaxWaitMillis(max_wait_time);
      config.setTestOnBorrow(test_on_borrow);
      jedisPool = new JedisPool(config, address, port, timeout, password);
    } catch (Exception e) {
      logger.error("Fail to initilize jedis instance!");
    }
  }

  /**
   * 获取Jedis实例
   * 
   * @return
   */
  public synchronized static Jedis getJedis() {
    try {
      if (jedisPool != null) {
        Jedis resource = jedisPool.getResource();
        return resource;
      } else {
        return null;
      }
    } catch (Exception e) {
      logger.error("Fail to get Jedis resource exception:" + e);
      return null;
    }
  }

  /**
   * 释放jedis资源
   * 
   * @param jedis
   */
  public static void releaseResource(final Jedis jedis) {
    if (jedis != null) {
      jedis.close();
    }
  }

}
