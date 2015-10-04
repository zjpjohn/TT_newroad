package com.newroad.cache.common;


import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cache.common.couchbase.CouchbaseCache;
import com.newroad.cache.common.couchbase.CouchbaseViewService;




/**
 * Unit test for simple App.
 */
public class CacheQueryCommand {

  private static Logger logger = LoggerFactory.getLogger(CacheQueryCommand.class);

  private static String COUCHBASE_SERVER_NODELIST;
  private static String COUCHBASE_SERVER_BUCKETNAME;
  // private static String COUCHBASE_SERVER_BUCKETNAME2;
  private static String COUCHBASE_SERVER_PWD;
  private static Integer COUCHBASE_SERVER_RECONNECT;
  private static Integer COUCHBASE_SERVER_OP_TIMEOUT;
  private static Integer COUCHBASE_SERVER_OP_QUEUE_MAXBLOCKTIME;
  private static Integer COUCHBASE_SERVER_PERSISTNODE;
  private static Integer COUCHBASE_SERVER_REPLICATENODE;

  public static CouchbaseCache supernoteCache;

  public static CouchbaseCache supernoteCache2;

  static {
    System.setProperty("viewmode", "development"); // before the connection to Couchbase

    InputStream is = CacheQueryCommand.class.getResourceAsStream("/memcached.properties");
    Properties props = new Properties();
    try {
      props.load(is);
      COUCHBASE_SERVER_NODELIST = props.getProperty("COUCHBASE_SERVER_NODELIST");
      COUCHBASE_SERVER_BUCKETNAME = props.getProperty("COUCHBASE_SERVER_BUCKETNAME");
      // COUCHBASE_SERVER_BUCKETNAME2 = props.getProperty("COUCHBASE_SERVER_BUCKETNAME2");
      COUCHBASE_SERVER_PWD = props.getProperty("COUCHBASE_SERVER_PWD");
      COUCHBASE_SERVER_OP_TIMEOUT = Integer.valueOf(props.getProperty("COUCHBASE_SERVER_OP_TIMEOUT"));
      COUCHBASE_SERVER_OP_QUEUE_MAXBLOCKTIME = Integer.valueOf(props.getProperty("COUCHBASE_SERVER_OP_QUEUE_MAXBLOCKTIME"));
      COUCHBASE_SERVER_RECONNECT = Integer.valueOf(props.getProperty("COUCHBASE_SERVER_RECONNECT"));
      COUCHBASE_SERVER_PERSISTNODE = Integer.valueOf(props.getProperty("COUCHBASE_SERVER_PERSISTNODE"));
      COUCHBASE_SERVER_REPLICATENODE = Integer.valueOf(props.getProperty("COUCHBASE_SERVER_REPLICATENODE"));
      is.close();
    } catch (Exception e) {
      logger.error("load properties error", e);
    }
  }

  public static void init() {
    if (supernoteCache == null) {
      supernoteCache =
          new CouchbaseCache(COUCHBASE_SERVER_NODELIST, COUCHBASE_SERVER_BUCKETNAME, COUCHBASE_SERVER_PWD, COUCHBASE_SERVER_OP_TIMEOUT,
              COUCHBASE_SERVER_OP_QUEUE_MAXBLOCKTIME, COUCHBASE_SERVER_RECONNECT, COUCHBASE_SERVER_PERSISTNODE,
              COUCHBASE_SERVER_REPLICATENODE);
    }
  }

  public static void shutDown() {
    if (supernoteCache != null) {
      supernoteCache.shutDown();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    init();
    String keyID = null;
    if (supernoteCache != null) {
//      JSONArray array = new JSONArray();
//      array.add("supernote.msg.poll.151");
//      array.add(11);
//      array.add("01117");
//      keyID = array.toString();
//      JSONObject json = new JSONObject();
//      json.put("msgID", "151");
//      json.put("source", 12);
//      json.put("deviceID", "011116");
//      json.put("count", 4);
//      json.put("min", 4000);
//      json.put("sumsqr", 3003);
//      supernoteCache.set(keyID, json.toString(), true);
//      System.out.println(supernoteCache.get(keyID));
      Map<String, Object> queryMap = new HashMap<String, Object>();
//      if(args!=null){
//        int length=args.length;
//        for(int i=1;i<length;i++){
//          String[] param=args[i].split("=");
//          queryMap.put(param[0], param[1]);
//        }
//      }
      //queryMap.put("msgID", "151");
      
      String[] groupArray=new String[]{"deviceID"};
      String designDocName = "MessageDesignDoc";
      String viewName = "MessageCountView";
      
      CouchbaseViewService viewSerive = new CouchbaseViewService();
      viewSerive.setCouchbaseCache(supernoteCache);
      Map<String,Object> result= viewSerive.countGroupView(designDocName, viewName, queryMap, new String[]{"version"}, null, null);
      System.out.println("Count Group View Result:" + result);
      supernoteCache.shutDown();

    }
  }

}
