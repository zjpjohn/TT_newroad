package com.newroad.fileext.service.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

/**
 * @info : 消息发送
 * @author: tangzj1
 * @data : 2014-7-8
 * @since : 1.5
 */
@Service
public class RabbitMQSender {
  
  private AmqpTemplate fileMQTemplate;
  
  public void setFileMQTemplate(AmqpTemplate fileMQTemplate) {
    this.fileMQTemplate = fileMQTemplate;
  }

  public void sendMessage(Object message) {
    fileMQTemplate.convertAndSend(message);
  }

  
}
