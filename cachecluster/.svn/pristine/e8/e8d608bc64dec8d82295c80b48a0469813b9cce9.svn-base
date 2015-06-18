package com.newroad.cache.common.couchbase;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.protocol.views.ViewDesign;

public class CouchbaseViewService {

  private static Logger logger = LoggerFactory.getLogger(CouchbaseViewService.class);

  private CouchbaseCache couchbaseCache;

  public void setCouchbaseCache(CouchbaseCache couchbaseCache) {
    this.couchbaseCache = couchbaseCache;
  }

  public Map<String, Object> queryJSONDoc(String designDocName, String viewName, Map<String, Object> queryConditionMap, Integer skip,
      Integer limit) {
    String mapFunction =
        "function (doc, meta) {\n" + "  if (meta.type == \"json\" && meta.id!=null){\n  " + "    emit(meta.id, doc);\n" + "}}";
    if (queryConditionMap != null && queryConditionMap.size() > 0) {
      String queryCond = "";
      for (Map.Entry<String, Object> entry : queryConditionMap.entrySet()) {
        queryCond += " && doc." + entry.getKey() + "=='" + entry.getValue() + "'";
      }
      int index = mapFunction.indexOf("meta.id!=null");
      String prefix = mapFunction.substring(0, index + 13);
      String end = mapFunction.substring(index + 13);
      mapFunction = prefix + queryCond + end;
    }
    logger.debug("Couchbase query view map function:" + mapFunction);
    couchbaseCache.createDesignDoc(designDocName, new ViewDesign(viewName, mapFunction));
    return couchbaseCache.queryViewDocument(designDocName, viewName, skip, limit);
  }

  public int countView(String designDocName, String viewName, Map<String, Object> queryConditionMap) {
    String mapFunction = "function (doc, meta) {\n" + "  if (meta.id!=null){\n  " + " emit(meta.id, null);\n" + "}}";

    if (queryConditionMap != null && queryConditionMap.size() > 0) {
      String queryCond = "";
      for (Map.Entry<String, Object> entry : queryConditionMap.entrySet()) {
        queryCond += " && doc." + entry.getKey() + "=='" + entry.getValue() + "'";
      }
      int index = mapFunction.indexOf("meta.id!=null");
      String prefix = mapFunction.substring(0, index + 13);
      String end = mapFunction.substring(index + 13);
      mapFunction = prefix + queryCond + end;
    }
    String reduce = "_count";
    logger.debug("Couchbase count view map function:" + mapFunction + ",reduce function:" + reduce);
    couchbaseCache.createDesignDoc(designDocName, new ViewDesign(viewName, mapFunction, reduce));
    return couchbaseCache.queryViewCount(designDocName, viewName);
  }

  public Map<String, Object> countGroupView(String designDocName, String viewName, Map<String, Object> queryConditionMap, String[] groupArray,
      Integer skip, Integer limit) {
    String mapFunction = null;
    if (groupArray != null && groupArray.length > 0) {
      String groupCheckStr = "";
      String groupEmitStr = "";
      for (String groupkey : groupArray) {
        groupCheckStr += "doc." + groupkey + "&&";
        groupEmitStr += "doc." + groupkey + ",";
      }
      groupCheckStr = groupCheckStr.substring(0, groupCheckStr.length() - 2);
      groupEmitStr = groupEmitStr.substring(0, groupEmitStr.length() - 1);
      mapFunction =
          "function (doc, meta) {\n" + "  if (meta.id!=null){\n  " + "if(" + groupCheckStr + "){\n " + " emit(" + groupEmitStr + ");\n"              + "}}}";
    } else {
      mapFunction = "function (doc, meta) {\n" + "  if (meta.id!=null){\n  " + " emit(meta.id, null);\n" + "}}";
    }

    if (queryConditionMap != null && queryConditionMap.size() > 0) {
      String queryCond = "";
      for (Map.Entry<String, Object> entry : queryConditionMap.entrySet()) {
        queryCond += " && doc." + entry.getKey() + "=='" + entry.getValue() + "'";
      }
      int index = mapFunction.indexOf("meta.id!=null");
      String prefix = mapFunction.substring(0, index + 13);
      String end = mapFunction.substring(index + 13);
      mapFunction = prefix + queryCond + end;
    }
    String reduce = "_count";
    logger.debug("Couchbase group view map function:" + mapFunction + ",reduce function:" + reduce);
    couchbaseCache.createDesignDoc(designDocName, new ViewDesign(viewName, mapFunction, reduce));
    return couchbaseCache.queryViewGroupCount(designDocName, viewName, skip, limit);
  }


}
