package com.newroad.tripmaster.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.dao.pojo.Coordinate;
import com.newroad.tripmaster.dao.pojo.Scenic;
import com.newroad.tripmaster.dao.pojo.Site;
import com.newroad.tripmaster.dao.pojo.info.Tips;
import com.newroad.tripmaster.dao.pojo.info.UserBehavior;
import com.newroad.tripmaster.dao.pojo.trip.CustomizeRoute;
import com.newroad.tripmaster.dao.pojo.trip.POIRoute;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;
import com.newroad.util.auth.MD5Encode;

public class JSONConvertor {

  private static Logger logger = LoggerFactory.getLogger(JSONConvertor.class);

  private static ObjectMapper objectMapper = null;

  public static ObjectMapper getJSONInstance() {
    if (objectMapper == null) {
      objectMapper = JsonFactory.create();
    }
    return objectMapper;
  }

  private JSONConvertor() {

  }

  public static Site transformJSON2Site(String json) {
    Site location = (Site) objectMapper.fromJson(json, Site.class);
    List<Coordinate> rangeList = location.getRange();
    // location.setRange(rangeList);
    String hash = MD5Encode.generateMD5Code(concatCoordinate(rangeList));
    location.setHashcode(hash);
    logger.info("Site location" + location);
    return location;
  }

  private static String concatCoordinate(List<Coordinate> rangeList) {
    StringBuilder sb = new StringBuilder();
    for (Coordinate coordinate : rangeList) {
      sb.append(coordinate.getLng());
      sb.append(coordinate.getLat());
    }
    return sb.toString();
  }

  public static Scenic transformJSON2Scenic(JSONObject json) {
    Scenic scenic = (Scenic) JSONObject.toBean(json, Scenic.class);
    return scenic;
  }

  public static Tips transformJSON2Tips(JSONObject json) {
    Tips tips = (Tips) JSONObject.toBean(json, Tips.class);
    return tips;
  }

  public static String transformSites2JSON(List<Site> siteList) {
    if (siteList == null) {
      return null;
    }
    List<Object> list = new ArrayList<Object>();
    for (Site site : siteList) {
      Map<String, Object> map = new TreeMap<String, Object>();
      map.put(DataConstant.HASH_SITE_ID, site.getHashcode());
      map.put(DataConstant.NAME, site.getName());
      map.put(DataConstant.DESCRIPTION, site.getDescription());
      map.put(DataConstant.SITE_TYPE, site.getSitetype());
      map.put(DataConstant.MARK_TYPE, site.getMarktype());
      map.put(DataConstant.PICTURE, site.getPicture());
      map.put(DataConstant.GRAVITY, site.getGravity());
      map.put(DataConstant.RANGE, site.getRange());
      map.put(DataConstant.RANGE_RADIUS, site.getRangeRadius());
      list.add(map);
    }
    String json = getJSONInstance().writeValueAsString(list);
    return json;
  }

  public static String transformSiteSummary2JSON(Site site) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (site != null) {
      map.put(DataConstant.HASH_SITE_ID, site.getHashcode());
      map.put(DataConstant.NAME, site.getName());
      map.put(DataConstant.PICTURE, site.getPicture());
      map.put(DataConstant.SITE_TYPE, site.getSitetype());
      map.put(DataConstant.MARK_TYPE, site.getMarktype());
      map.put(DataConstant.PARENT_SITE_ID, site.getParentsiteid());
      map.put(DataConstant.COUNTRY, site.getCountry());
      map.put(DataConstant.CITY, site.getCity());
      map.put(DataConstant.STREET, site.getStreet());
      map.put(DataConstant.GRAVITY, site.getGravity());
      map.put(DataConstant.RANGE, site.getRange());
      map.put(DataConstant.RANGE_RADIUS, site.getRangeRadius());
      if (site.getChildSiteList() != null) {
        map.put(DataConstant.CHILD_SITES, site.getChildSiteList());
      }
    }

    // if (scenic != null) {
    // map.put(DataConstant.SUMMARY, scenic.getSummary());
    // map.put(DataConstant.DESCRIPTION, scenic.getDescription());
    // map.put(DataConstant.SCENIC_TYPE, scenic.getType());
    // map.put(DataConstant.GRADE, scenic.getGrade());
    // map.put(DataConstant.PICTURE, scenic.getPicture());
    // }
    String json = getJSONInstance().writeValueAsString(map);
    return json;
  }

  public static String transformScenicDetail2JSON(Scenic scenic) {
    Map<String, Object> map = new TreeMap<String, Object>();
    map.put(DataConstant.ID, scenic.getScenicId().toString());
    map.put(DataConstant.NAME, scenic.getName());
    map.put(DataConstant.SUMMARY, scenic.getSummary());
    map.put(DataConstant.DESCRIPTION, scenic.getDescription());
    map.put(DataConstant.TYPE, scenic.getType());
    map.put(DataConstant.GRADE, scenic.getGrade());
    map.put(DataConstant.PICTURE, scenic.getPicture());
    String json = getJSONInstance().writeValueAsString(map);
    return json;
  }

  public static String transformRoute2JSON(CustomizeRoute customizeRoute) {
    Map<String, Object> map = new TreeMap<String, Object>();
    map.put(DataConstant.TRIP_ROUTE_ID, customizeRoute.getTripRouteId());
    map.put(DataConstant.ROUTE_NAME, customizeRoute.getRouteName());
    map.put(DataConstant.DESCRIPTION, customizeRoute.getDescription());
    map.put(DataConstant.DURATION, customizeRoute.getDuration());
    map.put(DataConstant.ROUTE_MAP, customizeRoute.getRouteMap());
    map.put(DataConstant.CAROUSEL_PICS, customizeRoute.getCarouselPics());
    map.put(DataConstant.ROUTE_TAGS, customizeRoute.getRouteTags());
    map.put(DataConstant.STATUS, customizeRoute.getStatus());
    map.put(DataConstant.LUCKER_ID, customizeRoute.getLuckerId());
    String json = getJSONInstance().writeValueAsString(map);
    return json;
  }

  public static Map<String, Object> filterTripProduct(TripProduct product) {
    return null;
  }

  public static Map<String, Object> filterTripRoute(CustomizeRoute route) {
    return null;
  }

  public static Map<String, Object> filterPOIRoute(POIRoute poiRoute) {
    return null;
  }

  public static String resourceURLGenerate(String pictureKey) {
    return SystemConstant.QINIU_DOMAIN + pictureKey;
  }

  public static List<Object> filterTripProducts(List<TripProduct> tripProductList, Map<String, UserBehavior> behaviorTargetMap) {
    boolean isUserBehaviorExist = false;
    if (behaviorTargetMap != null && behaviorTargetMap.size() > 0) {
      isUserBehaviorExist = true;
    }

    List<Object> list = new ArrayList<Object>();
    for (TripProduct tripProduct : tripProductList) {
      Map<String, Object> map = new TreeMap<String, Object>();
      map.put(DataConstant.TRIP_PRODUCT_ID, tripProduct.getTripProductId());
      map.put(DataConstant.PRODUCT_NAME, tripProduct.getProductName());
      map.put(DataConstant.TITLE, tripProduct.getTitle());
      map.put(DataConstant.SUMMARY, tripProduct.getSummary());
      map.put(DataConstant.PICTURE, tripProduct.getPicture());
      map.put(DataConstant.USER_START_CITY, tripProduct.getUserStartCity());
      map.put(DataConstant.TRAVEL_CITIES, tripProduct.getTravelCities());
      map.put(DataConstant.TRIP_ROUTE_ID, tripProduct.getTripRouteId());
      map.put(DataConstant.PRICE, tripProduct.getPrice());
      map.put(DataConstant.PRICE_UNIT, tripProduct.getPriceUnit());
      map.put(DataConstant.STATUS, tripProduct.getStatus());
      map.put(DataConstant.LUCKER_ID, tripProduct.getLuckerId());
      if (tripProduct.getLucker() != null) {
        map.put(DataConstant.LUCKER_NAME, tripProduct.getLucker().getLuckerName());
        map.put(DataConstant.LUCKER_PORTRAIT, tripProduct.getLucker().getLuckerPortrait());
        map.put(DataConstant.LUCKER_LEVEL, tripProduct.getLucker().getLuckerLevel());
      }
      if (isUserBehaviorExist && behaviorTargetMap.get(tripProduct.getTripProductId()) != null) {
        UserBehavior userBehavior = behaviorTargetMap.get(tripProduct.getTripProductId());
        map.put(DataConstant.USER_FAVORITE, userBehavior.getActionType());
        map.put(DataConstant.USER_ID, userBehavior.getUserId());
      }
      map.put(DataConstant.CREATE_TIME, tripProduct.getCreateTime());
      map.put(DataConstant.UPDATE_TIME, tripProduct.getUpdateTime());
      list.add(map);
    }
    return list;
  }

  public static String concatStringArray(String[] stringArray) {
    StringBuilder sb = new StringBuilder();
    int length = stringArray.length;
    for (int i = 0; i < length; i++) {
      if (i < length - 1) {
        sb.append(stringArray[i] + ".");
      } else {
        sb.append(stringArray[i]);
      }
    }
    return sb.toString();
  }

}
