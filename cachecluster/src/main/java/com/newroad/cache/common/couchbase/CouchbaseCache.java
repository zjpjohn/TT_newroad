package com.newroad.cache.common.couchbase;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import net.spy.memcached.internal.OperationFuture;
import net.spy.memcached.ops.OperationStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.DesignDocument;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import com.couchbase.client.protocol.views.View;
import com.couchbase.client.protocol.views.ViewDesign;
import com.couchbase.client.protocol.views.ViewResponse;
import com.couchbase.client.protocol.views.ViewRow;
import com.newroad.cache.common.Cache;


/**
 * @info CouchbaseCache
 * @author tangzj1
 * @date Jan 14, 2014
 * @version 1.0
 */
public class CouchbaseCache implements Cache<String, Serializable> {

  private static Logger logger = LoggerFactory.getLogger(CouchbaseCache.class);

  protected CouchbaseClient cacheClient;

  protected int persisto;

  protected int replicateto;

  protected Integer querySkip = 0;

  protected Integer queryLimit = 10;

  public CouchbaseCache(String nodeipList, String bucketName, String password, long opTimeout, long opQueueMaxBlockTime, long reconnect,
      int persistNode, int replicateNode) {
    cacheClient = new CouchbaseManager(nodeipList, bucketName, password, opTimeout, opQueueMaxBlockTime, reconnect).getInstance();
    this.persisto = persistNode;
    this.replicateto = replicateNode;
  }

  public CouchbaseCache(CouchbaseClient client) {
    cacheClient = client;
  }

  public void shutDown() {
    if (cacheClient != null)
      cacheClient.shutdown();
  }

  public CouchbaseClient getCouchbaseClient() {
    return cacheClient;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#get(java.lang.Object, java.util.concurrent.Callable)
   */
  public Serializable get(String key) {
    return (Serializable) cacheClient.get(key);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#getAllPresent(java.lang.Iterable)
   */
  public Map<String, Serializable> getAllPresent(Iterable<String> keys) {
    Map<String, Serializable> cacheMap = new ConcurrentHashMap<String, Serializable>();
    for (Iterator<String> itemIt = keys.iterator(); itemIt.hasNext();) {
      String itemKey = itemIt.next();
      cacheMap.put(itemKey, get(itemKey));
    }
    return cacheMap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#put(java.lang.Object, java.lang.Object)
   */
  public boolean set(String key, Serializable value) throws CacheException {
    OperationFuture<Boolean> result = cacheClient.set(key, value);
    try {
      return result.get();
    } catch (InterruptedException e) {
      logger.error("Cache set InterruptedException:", e);
      throw new CacheException("Cache (key=" + key + ") set InterruptedException:", e);
    } catch (ExecutionException e) {
      logger.error("Cache set ExecutionException:", e);
      throw new CacheException("Cache (key=" + key + ") set ExecutionException:", e);
    } catch (Exception e) {
      logger.error("Cache set Exception:", e);
      throw new CacheException("Cache (key=" + key + ") set Exception:", e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#put(java.lang.Object, java.lang.Object)
   */
  public boolean set(String key, Serializable value, int persisto, int replicateto) throws CacheException {
    OperationFuture<Boolean> result =
        cacheClient.set(key, value, NodeSetting.getPersistToSetting(persisto), NodeSetting.getReplicateToSetting(replicateto));
    try {
      return result.get();
    } catch (InterruptedException e) {
      logger.error("Cache set InterruptedException:", e);
      throw new CacheException("Cache (key=" + key + ") set InterruptedException:", e);
    } catch (ExecutionException e) {
      logger.error("Cache set ExecutionException:", e);
      throw new CacheException("Cache (key=" + key + ") set ExecutionException:", e);
    } catch (Exception e) {
      logger.error("Cache set Exception:", e);
      throw new CacheException("Cache (key=" + key + ") set Exception:", e);
    }
  }

  public boolean set(String key, Serializable value, long exp) throws CacheException {
    OperationFuture<Boolean> result = cacheClient.set(key, (int) exp, value);
    try {
      return result.get();
    } catch (InterruptedException e) {
      logger.error("Cache set InterruptedException:", e);
      throw new CacheException("Cache (key=" + key + ") set InterruptedException:", e);
    } catch (ExecutionException e) {
      logger.error("Cache set ExecutionException:", e);
      throw new CacheException("Cache (key=" + key + ") set ExecutionException:", e);
    } catch (Exception e) {
      logger.error("Cache set Exception:", e);
      throw new CacheException("Cache (key=" + key + ") set Exception:", e);
    }
  }

  /*
   * expire time unit is second.
   */
  public boolean set(String key, Serializable value, long exp, int persisto, int replicateto) throws CacheException {
    OperationFuture<Boolean> result =
        cacheClient.set(key, (int) exp, value, NodeSetting.getPersistToSetting(persisto), NodeSetting.getReplicateToSetting(replicateto));
    try {
      return result.get();
    } catch (InterruptedException e) {
      logger.error("Cache set InterruptedException:", e);
      throw new CacheException("Cache (key=" + key + ") set InterruptedException:", e);
    } catch (ExecutionException e) {
      logger.error("Cache set ExecutionException:", e);
      throw new CacheException("Cache (key=" + key + ") set ExecutionException:", e);
    } catch (Exception e) {
      logger.error("Cache set Exception:", e);
      throw new CacheException("Cache (key=" + key + ") set Exception:", e);
    }
  }

  public boolean set(String k, Serializable v, boolean isPresist) {
    if (isPresist) {
      return set(k, v, this.persisto, this.replicateto);
    } else {
      return set(k, v);
    }
  }

  public boolean set(String k, Serializable v, long time, boolean isPresist) {
    if (isPresist) {
      return set(k, v, (int) time, this.persisto, this.replicateto);
    } else {
      return set(k, v, (int) time);
    }
  }

  public boolean touch(String key, long exp) throws CacheException {
    OperationFuture<Boolean> result = cacheClient.touch(key, (int) exp);
    try {
      return result.get();
    } catch (InterruptedException e) {
      logger.error("Cache touch InterruptedException:", e);
      throw new CacheException("Cache (key=" + key + ") touch InterruptedException:", e);
    } catch (ExecutionException e) {
      logger.error("Cache touch ExecutionException:", e);
      throw new CacheException("Cache (key=" + key + ") touch ExecutionException:", e);
    } catch (Exception e) {
      logger.error("Cache touch Exception:", e);
      throw new CacheException("Cache (key=" + key + ") touch Exception:", e);
    }
  }

  /**
   * Continuously try a set with exponential backoff until number of tries or successful. The
   * exponential backoff will wait a maximum of 1 second, or whatever
   * 
   * @param key
   * @param exp
   * @param value
   * @param tries number of tries before giving up
   * @return the OperationFuture<Boolean> that wraps this set operation
   */
  public OperationFuture<Boolean> contSet(String key, int exp, Object value, int tries) {
    OperationFuture<Boolean> result = null;
    OperationStatus status;
    int backoffexp = 0;

    try {
      do {
        if (backoffexp > tries) {
          throw new RuntimeException("Could not perform a set after " + tries + " tries.");
        }
        result = cacheClient.set(key, exp, value);
        status = result.getStatus(); // blocking call, improve if needed
        if (status.isSuccess()) {
          break;
        }
        if (backoffexp > 0) {
          double backoffMillis = Math.pow(2, backoffexp);
          backoffMillis = Math.min(1000, backoffMillis); // 1 sec max
          Thread.sleep((int) backoffMillis);
          logger.error("Backing off, tries so far: " + backoffexp);
        }
        backoffexp++;

        if (!status.isSuccess()) {
          logger.error("Failed with status: " + status.getMessage());
        }

      } while (status.getMessage().equals("Temporary failure"));
    } catch (InterruptedException ex) {
      logger.error("Interrupted while trying to contSet Exception:" + ex.getMessage());
    }

    if (result == null) {
      throw new RuntimeException("Could not carry out operation."); // rare
    }

    // note that other failure cases fall through. status.isSuccess() can be
    // checked for success or failure or the message can be retrieved.
    return result;
  }

  @SuppressWarnings("unchecked")
  public int setAll(Map<? extends String, ? extends Serializable> map) {
    Set<?> entrySet = map.entrySet();
    Iterator<?> iter = entrySet.iterator();
    int count = 0;
    while (iter.hasNext()) {
      Entry<String, Serializable> entry = (Entry<String, Serializable>) iter.next();
      if (set(entry.getKey(), entry.getValue())) {
        count++;
      }
    }
    return count;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#invalidate(java.lang.Object)
   */
  public boolean delete(String key) {
    boolean isSucc = false;
    OperationFuture<Boolean> result =
        cacheClient.delete(key, NodeSetting.getPersistToSetting(persisto), NodeSetting.getReplicateToSetting(replicateto));
    try {
      isSucc = result.get();
    } catch (InterruptedException e) {
      logger.error("Cache (key=" + key + ",persisto=" + persisto + ",replicateto=" + replicateto + ") delete InterruptedException:", e);
      // throw new CacheException("Cache (key=" + key + ") delete InterruptedException:", e);
    } catch (ExecutionException e) {
      logger.error("Cache (key=" + key + ",persisto=" + persisto + ",replicateto=" + replicateto + ") delete ExecutionException:", e);
      // throw new CacheException("Cache (key=" + key + ") delete ExecutionException:", e);
    } catch (Exception e) {
      logger.error("Cache (key=" + key + ",persisto=" + persisto + ",replicateto=" + replicateto + ") delete Exception:", e);
      // throw new CacheException("Cache (key=" + key + ") delete Exception:", e);
    }
    return isSucc;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#invalidateAll(java.lang.Iterable)
   */
  public int deleteAll(Iterable<String> keys) {
    Iterator<?> iter = keys.iterator();
    int count = 0;
    while (iter.hasNext()) {
      Object key = iter.next();
      if (delete((String) key)) {
        count++;
      }
    }
    return count;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#invalidateAll()
   */
  public boolean deleteAll() {
    boolean isSucc = false;
    OperationFuture<Boolean> result = cacheClient.flush();
    try {
      isSucc = result.get();
    } catch (InterruptedException e) {
      logger.error("Cache deleteAll InterruptedException:", e);
      // throw new CacheException("Cache deleteAll InterruptedException:", e);
    } catch (ExecutionException e) {
      logger.error("Cache deleteAll ExecutionException:", e);
      // throw new CacheException("Cache deleteAll ExecutionException:", e);
    } catch (Exception e) {
      logger.error("Cache deleteAll Exception:", e);
      // throw new CacheException("Cache deleteAll Exception:", e);
    }
    return isSucc;
  }

  /**
   * 缂撳瓨鏁版嵁杩囨湡鏃堕棿
   * 
   * @return
   */
  public Date getExpiry(int seconds) {
    Calendar expiry = Calendar.getInstance();
    expiry.add(Calendar.SECOND, seconds);
    return expiry.getTime();
  }

  public long getExpiryLongFromNow(int seconds) {
    Calendar expiry = Calendar.getInstance();
    expiry.add(Calendar.SECOND, seconds);
    return expiry.getTimeInMillis();
  }

  /**
   * 鑾峰緱褰撳墠鏃堕棿鐨刲ong鍊�
   */
  public long getNowLong() {
    return System.currentTimeMillis();
  }

  public ViewDesign createViewDesign(String viewName, String mapFunc, String reduceFunc) {
    return new ViewDesign(viewName, mapFunc, reduceFunc);
  }


  public DesignDocument createDevDesignDoc(String docName, ViewDesign viewDesign) {
    String DEV_PREFIX = "dev_";
    DesignDocument ddoc = new DesignDocument(DEV_PREFIX + docName);
    if (viewDesign != null) {
      ddoc.getViews().add(viewDesign);
    }
    cacheClient.createDesignDoc(ddoc);
    return ddoc;
  }

  public DesignDocument createDesignDoc(String docName, ViewDesign viewDesign) {
    DesignDocument ddoc = new DesignDocument(docName);
    if (viewDesign != null) {
      ddoc.getViews().add(viewDesign);
    }
    cacheClient.createDesignDoc(ddoc);
    return ddoc;
  }

  public View getView(String designDoc, String viewName) {
    return cacheClient.getView(designDoc, viewName);
  }

  public ViewResponse basicQuery(String designDoc, String viewName, Integer skip, Integer limit) {
    View view = cacheClient.getView(designDoc, viewName);
    Query query = new Query();
    query.setIncludeDocs(true);
    query.setStale(Stale.FALSE);
    // Need to call the source code of Couchbase client SDK
    // query.setFullSet(FullSet.TRUE);
    if (skip != null) {
      this.querySkip = skip;
    }
    query.setSkip(querySkip);
    if (limit != null) {
      this.queryLimit = limit;
    }
    query.setLimit(queryLimit);
    ViewResponse response = cacheClient.query(view, query);
    return response;
  }

  public Map<String, Object> queryViewDocument(String designDoc, String viewName, Integer skip, Integer limit) {
    ViewResponse response = basicQuery(designDoc, viewName, skip, limit);
    // 4: Iterate over the Data and print out the full document
    Iterator<ViewRow> iter = response.iterator();
    Map<String, Object> queryResult = new HashMap<String, Object>();
    while (iter.hasNext()) {
      ViewRow row = iter.next();
      queryResult.put(row.getKey(), row.getDocument());
    }
    return queryResult;
  }

  public Map<String, Object> queryViewGroupCount(String designDocName, String viewName, Integer skip, Integer limit) {
    View view = cacheClient.getView(designDocName, viewName);
    Query query = new Query();
    query.setStale(Stale.FALSE);
    // Need to call the source code of Couchbase client SDK
    // query.setFullSet(FullSet.TRUE);
    query.setGroup(true);
    if (skip != null) {
      this.querySkip = skip;
    }
    query.setSkip(querySkip);
    if (limit != null) {
      this.queryLimit = limit;
    }

    ViewResponse response = cacheClient.query(view, query);
    Iterator<ViewRow> iter = response.iterator();
    Map<String, Object> queryResult = new HashMap<String, Object>();
    while (iter.hasNext()) {
      ViewRow row = iter.next();
      queryResult.put(row.getKey(), row.getValue());
    }
    return queryResult;
  }

  public int queryViewCount(String designDocName, String viewName) {
    View view = cacheClient.getView(designDocName, viewName);
    Query query = new Query();
    query.setStale(Stale.FALSE);
    // Need to call the source code of Couchbase client SDK
    // query.setFullSet(FullSet.TRUE);

    ViewResponse response = cacheClient.query(view, query);
    Iterator<ViewRow> iter = response.iterator();
    int count = 0;
    while (iter.hasNext()) {
      ViewRow row = iter.next();
      count = Integer.valueOf(row.getValue());
    }
    return count;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#size()
   */
  public long size() {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#asMap()
   */
  public ConcurrentMap<String, Serializable> asMap() {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.lenovo.common.cache.Cache#keys()
   */
  @Override
  public List<String> keys() {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection<SocketAddress> getAvailableServers() {
    return cacheClient.getAvailableServers();
  }

  public Collection<SocketAddress> getUnavailableServers() {
    return cacheClient.getUnavailableServers();
  }

  public Map<SocketAddress, Map<String, String>> getStats() {
    return cacheClient.getStats();
  }

  public boolean isAvailable() {
    Collection<SocketAddress> unavailibleAddressColl = cacheClient.getUnavailableServers();
    int failAddressNum = unavailibleAddressColl.size();
    if (failAddressNum > 0) {
      return false;
    }
    return true;
  }


}
