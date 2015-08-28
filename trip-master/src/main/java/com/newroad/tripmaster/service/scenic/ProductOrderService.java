package com.newroad.tripmaster.service.scenic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.TripProductDao;
import com.newroad.tripmaster.dao.maria.MariaDao;
import com.newroad.tripmaster.dao.pojo.Count;
import com.newroad.tripmaster.dao.pojo.order.ProductOrder;
import com.newroad.tripmaster.dao.pojo.order.TravelUser;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;
import com.newroad.tripmaster.service.ProductOrderServiceIf;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ServiceResult;

public class ProductOrderService implements ProductOrderServiceIf {

  private static Logger logger = LoggerFactory.getLogger(ProductDesignService.class);

  private TripProductDao tripProductDao;

  private MariaDao mariaDao;

  /**
   * Create product order service
   */
  @Override
  public ServiceResult<String> createProductOrder(ProductOrder productOrder) {
    ServiceResult<String> result = new ServiceResult<String>();
    String payKey = StringHelper.generateKeyId();
    productOrder.setPayKey(payKey);
    Integer insertCount = mariaDao.insert("order.createOrder", productOrder);
    logger.info("CreateProductOrder count:" + insertCount);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrder.getOrderId());
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> updatetOrderPayStatus(Long productOrderId, Long userId, Integer status, String payStatus) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    queryMap.put(DataConstant.USER_ID, userId);
    queryMap.put(DataConstant.STATUS, status);
    queryMap.put(DataConstant.PAY_STATUS, payStatus);
    Integer updateCount = mariaDao.update("order.systemUpdateOrder", queryMap);
    logger.info("UpdatetOrderPayStatus count:" + updateCount);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    map.put(DataConstant.STATUS, 2);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> viewProductOrder(Long luckerId, Integer userRole, Long productOrderId) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    if (userRole == 2) {
      queryMap.put(DataConstant.LUCKER_ID, luckerId);
    }
    ProductOrder productOrder = mariaDao.selectOne("order.selectProductOrderById", queryMap);
    logger.info("viewProductOrder info:" + productOrder);

    List<TravelUser> orderUserList = mariaDao.selectList("travelUser.selectOrderUsersByOrderId", productOrderId);
    logger.info("ProductOrderUserList:" + orderUserList);
    productOrder.setOrderUserList(orderUserList);

    String tripProductId = productOrder.getTripProductId();
    if (tripProductId != null) {
      TripProduct tripProduct = tripProductDao.getTripProduct(tripProductId);
      productOrder.setTripProduct(tripProduct);
    }

    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(productOrder);
    result.setBusinessResult(jsonResult);
    return result;
  }
  
  @Override
  public ServiceResult<String> viewUserProductOrder(Long userId, Long productOrderId) {
    ServiceResult<String> result = new ServiceResult<String>();
    ProductOrder productOrder=viewUserProductOrders(userId,productOrderId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(productOrder);
    result.setBusinessResult(jsonResult);
    return result;
  }

  
  public ProductOrder viewUserProductOrders(Long userId, Long orderId) {
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put(DataConstant.USER_ID, userId);
    queryMap.put(DataConstant.PRODUCT_ORDER_ID, orderId);
    ProductOrder productOrder = mariaDao.selectOne("order.selectProductOrderById", queryMap);
    logger.info("selectProductOrderById:" + productOrder);
    return productOrder;
  }

  @Override
  public ServiceResult<String> listProductOrders(Long luckerId, Integer userRole, Integer start, Integer size) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    if (userRole == 2) {
      queryMap.put(DataConstant.LUCKER_ID, luckerId);
    }
    List<ProductOrder> productOrderList = mariaDao.selectList("order.selectProductOrderList", queryMap);
    List<Count> orderCountList = mariaDao.selectList("order.countProductOrders", queryMap);
    logger.info("selectProductOrderList:" + productOrderList + ",orderCountList:" + orderCountList);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_LIST, productOrderList);
    map.put(DataConstant.PRODUCT_ORDER_COUNT, orderCountList);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public List<ProductOrder> listUserProductOrders(Long userId) {
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put(DataConstant.USER_ID, userId);
    List<ProductOrder> productOrderList = mariaDao.selectList("order.selectProductOrderByUser", queryMap);
    logger.info("selectProductOrderByUser list:" + productOrderList);
    return productOrderList;
  }

  @Override
  public ServiceResult<String> systemUpdateProductOrder(Long productOrderId, Map<String, Object> updateActionMap) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> updateMap = updateActionMap;
    updateMap.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    Integer updateCount = mariaDao.update("order.systemUpdateOrder", updateMap);
    logger.info("SystemUpdateOrder count:" + updateCount);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> updateProductOrderByLucker(Long productOrderId, Long luckerId, Map<String, Object> updateActionMap) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> updateMap = updateActionMap;
    updateMap.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    updateMap.put(DataConstant.LUCKER_ID, luckerId);
    Integer updateCount = mariaDao.update("order.updateOrderByLucker", updateMap);
    logger.info("UpdateOrderByLucker count:" + updateCount);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> updateProductOrderByUser(Long productOrderId, Long userId, Map<String, Object> updateActionMap) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> updateMap = updateActionMap;
    updateMap.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    updateMap.put(DataConstant.USER_ID, userId);
    Integer updateCount = mariaDao.update("order.updateOrderByUser", updateMap);
    logger.info("UpdatetOrderByUser count:" + updateCount);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }


  @Override
  public ServiceResult<String> updateProductOrderStatus(Long productOrderId, Integer status) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    queryMap.put(DataConstant.STATUS, status);
    Integer updateCount = mariaDao.update("order.updateOrderStatus", queryMap);
    logger.info("UpdatetOrderStatus count:" + updateCount);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    map.put(DataConstant.STATUS, status);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public void setTripProductDao(TripProductDao tripProductDao) {
    this.tripProductDao = tripProductDao;
  }

  public void setMariaDao(MariaDao mariaDao) {
    this.mariaDao = mariaDao;
  }


}
