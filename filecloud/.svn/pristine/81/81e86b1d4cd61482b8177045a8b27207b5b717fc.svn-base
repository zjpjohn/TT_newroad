package com.newroad.fileext.service.mq;


import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;


@Service
public class RabbitMQListener implements MessageListener {

  private static Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);

  @Override
  public void onMessage(Message message) {
    if (logger.isDebugEnabled()) {
      log(message);
    }
    //String messageBody = new String(message.getBody());
    //System.out.println("Listener received message----->" + messageBody);
  }

  private void log(Message message) {
    logger.debug("MQ receive message:" + Arrays.toString(message.getBody()));
  }


}
