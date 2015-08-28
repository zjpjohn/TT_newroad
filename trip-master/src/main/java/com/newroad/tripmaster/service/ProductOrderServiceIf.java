package com.newroad.tripmaster.service;

import java.util.Map;

import com.newroad.tripmaster.dao.pojo.order.ProductOrder;
import com.newroad.util.apiresult.ServiceResult;

public interface ProductOrderServiceIf {

  // System generation
  public ServiceResult<String> createProductOrder(ProductOrder productOrder);

  public ServiceResult<String> viewProductOrder(Long luckerId, Integer userRole, Long productOrderId);

  public ServiceResult<String> viewUserProductOrder(Long userId, Long productOrderId);

  public ServiceResult<String> listProductOrders(Long luckerId, Integer userRole, Integer start, Integer size);

  // System update
  public ServiceResult<String> systemUpdateProductOrder(Long productOrderId, Map<String, Object> updateActionMap);

  // Lucker update
  public ServiceResult<String> updateProductOrderByLucker(Long productOrderId, Long luckerId, Map<String, Object> updateActionMap);

  // User update
  public ServiceResult<String> updateProductOrderByUser(Long productOrderId, Long userId, Map<String, Object> updateActionMap);

  // System update
  public ServiceResult<String> updatetOrderPayStatus(Long productOrderId, Integer status, String payStatus);

  // User requirement
  public ServiceResult<String> updateProductOrderStatus(Long productOrderId, Integer status);


}
