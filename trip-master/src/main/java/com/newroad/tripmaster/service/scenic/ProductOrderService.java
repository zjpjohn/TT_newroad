package com.newroad.tripmaster.service.scenic;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.constant.SystemConstant;
import com.newroad.tripmaster.dao.TripProductDao;
import com.newroad.tripmaster.dao.maria.MariaDao;
import com.newroad.tripmaster.dao.pojo.Count;
import com.newroad.tripmaster.dao.pojo.order.ProductOrder;
import com.newroad.tripmaster.dao.pojo.order.TravelUser;
import com.newroad.tripmaster.dao.pojo.trip.TripProduct;
import com.newroad.tripmaster.service.ProductOrderServiceIf;
import com.newroad.tripmaster.service.client.MessageSenderTask;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.apiresult.ServiceResult;

public class ProductOrderService implements ProductOrderServiceIf {

  private static Logger logger = LoggerFactory.getLogger(ProductOrderService.class);

  private TripProductDao tripProductDao;

  private MariaDao mariaDao;

  private ProductDateInventoryService productInventoryService;

  // private CommonService commonService;

  private ThreadPoolTaskExecutor taskExecutor;

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
    if (productOrder.getOrderId() == null) {
      logger.error("Fail to create new product order for tripProductId=" + productOrder.getTripProductId() + " userId="
          + productOrder.getUserId());
      result.setReturnCode(ReturnCode.SERVER_ERROR);
      result.setReturnMessage("Fail to create new product order for tripProductName=" + productOrder.getProductName());
      return result;
    }
    updateProductInventory2TravelDate(productOrder.getTripProductId(), productOrder.getTripStartDate(), 1);

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrder.getOrderId());
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  @Override
  public ServiceResult<String> updatetOrderPayStatus(Long productOrderId, Integer status, String payStatus) {
    ServiceResult<String> result = new ServiceResult<String>();
    Map<String, Object> queryMap = new HashMap<String, Object>();
    queryMap.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    queryMap.put(DataConstant.STATUS, status);
    queryMap.put(DataConstant.PAY_STATUS, payStatus);
    Integer updateCount = mariaDao.update("order.updateOrderPayStatus", queryMap);
    logger.info("UpdatetOrderPayStatus count:" + updateCount + " where status=" + status);
    if (status == 2) {
      ProductOrder productOrder = selectProductOrder(null, null, productOrderId);
      // String luckerName = null;
      // String luckerMobile = null;
      // if (productOrder.getLucker() != null) {
      // luckerName = productOrder.getLucker().getLuckerName();
      // luckerMobile = productOrder.getLucker().getLuckerMobile();
      // }
      sendOrderInfoMessage(productOrder);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    map.put(DataConstant.STATUS, status);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  private void sendOrderInfoMessage(ProductOrder productOrder) {
    String mobile = "18121025090";
    // String message =
    // "[yoyo] 产品  %s 生成订单 %s 已完成支付, 请及时联系达人 %s,联系电话  %s .";
    MessageSenderTask<Object> task = new MessageSenderTask<Object>(mobile, null, productOrder);
    FutureTask<Object> futureTask = new FutureTask<Object>(task);
    taskExecutor.execute(futureTask);
  }

  @Override
  public ServiceResult<String> viewProductOrder(Long luckerId, Integer userRole, Long productOrderId) {
    ServiceResult<String> result = new ServiceResult<String>();

    ProductOrder productOrder = null;
    if (userRole == 2) {
      productOrder = selectProductOrder(luckerId, null, productOrderId);
    } else {
      productOrder = selectProductOrder(null, null, productOrderId);
    }
    logger.info("viewProductOrder info:" + productOrder);

    List<TravelUser> orderUserList = mariaDao.selectList("travelUser.selectOrderUsersByOrderId", productOrderId);
    logger.debug("ProductOrderUserList:" + orderUserList);
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
    ProductOrder productOrder = selectProductOrder(null, userId, productOrderId);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(productOrder);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public ProductOrder selectProductOrder(Long luckerId, Long userId, Long orderId) {
    Map<String, Object> queryMap = new HashMap<String, Object>(3);
    queryMap.put(DataConstant.PRODUCT_ORDER_ID, orderId);
    if (luckerId != null) {
      queryMap.put(DataConstant.LUCKER_ID, luckerId);
    }
    if (userId != null) {
      queryMap.put(DataConstant.USER_ID, userId);
    }
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
    logger.debug("selectProductOrderList:" + productOrderList + ",orderCountList:" + orderCountList);

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

    if (status == 3 || status == 6 || status == 7) {
      ProductOrder order = selectProductOrder(null, null, productOrderId);
      if (order != null)
        updateProductInventory2TravelDate(order.getTripProductId(), order.getTripStartDate(), -1);
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put(DataConstant.PRODUCT_ORDER_ID, productOrderId);
    map.put(DataConstant.STATUS, status);
    String jsonResult = JSONConvertor.getJSONInstance().writeValueAsString(map);
    result.setBusinessResult(jsonResult);
    return result;
  }

  public void updateProductInventory2TravelDate(String tripProductId, Integer travelStartDate, Integer increment) {
    long temp = (long) travelStartDate * 1000;
    Timestamp ts = new Timestamp(temp);
    String time = SystemConstant.travelDateFormat.format(ts);
    String[] dateUnit = time.split("-");
    String yearMonth = dateUnit[0] + "-" + dateUnit[1];
    String dateStr = dateUnit[2];
    productInventoryService.updateProductOrderBuyQuantity(tripProductId, yearMonth, dateStr, increment);
  }

  public void setTripProductDao(TripProductDao tripProductDao) {
    this.tripProductDao = tripProductDao;
  }

  public void setMariaDao(MariaDao mariaDao) {
    this.mariaDao = mariaDao;
  }

  public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
    this.taskExecutor = taskExecutor;
  }

  // public void setCommonService(CommonService commonService) {
  // this.commonService = commonService;
  // }

  public void setProductInventoryService(ProductDateInventoryService productInventoryService) {
    this.productInventoryService = productInventoryService;
  }

}
