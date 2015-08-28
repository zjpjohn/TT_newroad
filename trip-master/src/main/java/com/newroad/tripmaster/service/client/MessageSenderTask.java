package com.newroad.tripmaster.service.client;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.dao.pojo.order.ProductOrder;
import com.newroad.tripmaster.service.openmessage.demo.MessageXSendDemo;

public class MessageSenderTask<V> implements Callable<V> {

  private static Logger logger = LoggerFactory.getLogger(MessageSenderTask.class);

  private String mobile;

  // private String message;

  private ProductOrder productOrder;

  public MessageSenderTask(String mobile, String message, ProductOrder productOrder) {
    this.mobile = mobile;
    // this.message = message;
    this.productOrder = productOrder;
  }

  @Override
  public V call() {
    try {
      sendSMSMessage();
    } catch (Exception e) {
      logger.error("Fail to send sms message because of erro:" + e);
    }
    return null;
  }

  public void sendSMSMessage() {
    if (productOrder == null) {
      logger.error("Fail to get product order for sms message!");
      return;
    }
    MessageXSendDemo.sendSystemSms(mobile, productOrder.getContactName(), productOrder.getOrderId().toString(),
        productOrder.getProductName(), productOrder.getPayAmount().toString(), productOrder.getPcs().toString(),
        productOrder.getContactPhone());
    if (productOrder.getContactPhone() != null)
      MessageXSendDemo.sendClientSms(productOrder.getContactPhone(), productOrder.getContactName(), productOrder.getOrderId().toString(),
          productOrder.getProductName(), productOrder.getPayAmount().toString(), productOrder.getPcs().toString());
  }

  // public void sendSMSMessage() {
  // Boolean result = false;
  // Map<String, Object> requestMap = new HashMap<String, Object>(2);
  // requestMap.put(UserConstant.MOBILE, mobile);
  // requestMap.put(UserConstant.MESSAGE, message);
  // String httpResult = null;
  // try {
  // StringBuffer sb = new StringBuffer();
  // String reqEntity =
  // JSONConvertor.getJSONInstance().writeValueAsString(requestMap);
  // sb.append(reqEntity);
  // StringBuffer httpsb = HttpUtil.httpCall(Method.post, url, null, sb,
  // null);
  // httpResult = httpsb.toString();
  // } catch (Exception e) {
  // logger.error("Fail to get user info", e);
  // }
  // if (httpResult != null && !"".equals(httpResult)) {
  // @SuppressWarnings("unchecked")
  // Map<String, Object> map =
  // JSONConvertor.getJSONInstance().readValue(httpResult, Map.class);
  // Integer code = (Integer) map.get("code");
  // if (code != null && code == 200) {
  // result = true;
  // }
  // }
  // logger.info("send sms message " + message + " result:" + result);
  // }
}
