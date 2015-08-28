package com.newroad.tripmaster.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newroad.tripmaster.constant.HttpConstant;
import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.dao.pojo.order.ProductOrder;
import com.newroad.tripmaster.filter.TokenAuthFilter;
import com.newroad.tripmaster.service.ProductOrderServiceIf;
import com.newroad.tripmaster.util.BeanDBObjectUtils;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;

@Controller
@RequestMapping("/v{apiVersion}/order")
public class ProductOrderController {

  private static Logger logger = LoggerFactory.getLogger(ProductOrderController.class);

  @Autowired
  private ProductOrderServiceIf productOrderService;


  /**
   * update designed trip product
   */
  @RequestMapping(value = "/new", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String createProductOrder(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when creating new order!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    ProductOrder productOrder = JSONConvertor.getJSONInstance().fromJson(reqParam, ProductOrder.class);
    productOrder.setUserId(user.getUserId());
    return productOrderService.createProductOrder(productOrder).toString();
  }


  /**
   * view product order
   */
  @RequestMapping(value = "/view/{productOrderId}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String viewProductOrder(HttpServletRequest request, @PathVariable("productOrderId") Long productOrderId, @PathVariable String apiVersion)
      throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (user.getUserRole() == 1 || user.getUserRole() == 2) {
      return productOrderService.viewProductOrder(user.getUserId(), user.getUserRole(), productOrderId).toString();
    } else if (user.getUserRole() == 3) {
      return productOrderService.viewUserProductOrder(user.getUserId(), productOrderId).toString();
    }
    return null;
  }



  /**
   * view product order list by userId
   */
  @RequestMapping(value = "/list/{start}/{size}", method = RequestMethod.GET, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listProductOrder(HttpServletRequest request, @PathVariable Integer start, @PathVariable Integer size,
      @PathVariable String apiVersion) throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (start == null || size == null) {
      logger.error("Fail to get request start or size parameters!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    Integer roleAuthority = user.getUserRole();
    return productOrderService.listProductOrders(user.getUserId(), roleAuthority, start, size).toString();
  }


  /**
   * update product order pay status
   */
  @RequestMapping(value = "/paystatus/{orderId}", method = RequestMethod.PUT, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String updateOrderPayStatus(HttpServletRequest request, @PathVariable("orderId") Long orderId,
      @PathVariable String apiVersion) throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when updating order pay status!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if (orderId == null || orderId == 0L) {
      logger.error("Fail to get productOrderId request parameters when updating order pay status!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    @SuppressWarnings("unchecked")
    Map<String, Object> map = JSONConvertor.getJSONInstance().readValue(reqParam, Map.class);
    Integer status = (Integer) map.get("status");
    String payStatus = (String) map.get("payInfo");
    return productOrderService.updatetOrderPayStatus(orderId, user.getUserId(), status, payStatus).toString();
  }

  /**
   * update product order by user
   */
  @RequestMapping(value = "/update/{orderId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String updateProductOrder(HttpServletRequest request, @PathVariable("orderId") Long orderId, @PathVariable String apiVersion)
      throws Exception {
    String reqParam = StringHelper.getRequestEntityString(request);
    if (reqParam.indexOf("error") >= 0) {
      logger.error("Fail to get request parameters when updating product order!");
      return ApiReturnObjectUtil.getReturn400().toString();
    }
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    Long userId = user.getUserId();
    Integer authority = user.getUserRole();

    ProductOrder productOrder = JSONConvertor.getJSONInstance().fromJson(reqParam, ProductOrder.class);
    @SuppressWarnings("unchecked")
    Map<String, Object> orderMap = BeanUtils.describe(productOrder);
    BeanDBObjectUtils.filterBeanMap(orderMap);
    String serviceResult = null;
    switch (authority) {
      case 1:
        serviceResult = productOrderService.systemUpdateProductOrder(orderId, orderMap).toString();
        break;
      case 2:
        serviceResult = productOrderService.updateProductOrderByLucker(orderId, userId, orderMap).toString();
        break;
      case 3:
        serviceResult = productOrderService.updateProductOrderByUser(orderId, userId, orderMap).toString();
        break;
    }
    return serviceResult;
  }

  /**
   * refund product order
   */
  @RequestMapping(value = "/refund/{orderId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String refundProductOrder(HttpServletRequest request, @PathVariable("orderId") Long productOrderId, @PathVariable String apiVersion)
      throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if(user.getUserRole()!=1&&user.getUserRole()!=2){
      logger.error("Fail to execute refund order task because of UNAUTHORIZED!");
      return ApiReturnObjectUtil.getReturn401().toString();
    }
    Integer status = 4;
    return productOrderService.updateProductOrderStatus(productOrderId, status).toString();
  }
  
  /**
   * cancel product order
   */
  @RequestMapping(value = "/cancel/{orderId}", method = RequestMethod.POST, produces = HttpConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String cancelProductOrder(HttpServletRequest request, @PathVariable("orderId") Long productOrderId, @PathVariable String apiVersion)
      throws Exception {
    SimpleUser user = TokenAuthFilter.getCurrentUser();
    if(user.getUserRole()!=1&&user.getUserRole()!=2){
      logger.error("Fail to execute cancel order task because of UNAUTHORIZED!");
      return ApiReturnObjectUtil.getReturn401().toString();
    }
    Integer status = 3;
    return productOrderService.updateProductOrderStatus(productOrderId, status).toString();
  }

}
