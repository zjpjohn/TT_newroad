package com.newroad.fileext.service.cloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.newroad.fileext.service.mq.RabbitMQSender;
import com.newroad.fileext.utilities.FileResourceException;

/**
 * @info used to monitor cloud sync service 
 * @author tangzj1
 * @version 2.0
 * @since May 8, 2014
 */
@Service
public class CloudSyncService {

  private static Logger logger = LoggerFactory.getLogger(CloudSyncService.class);

  private RabbitMQSender messageSender;

  public void syncCloudFile(String cloudMessage) throws FileResourceException {
    logger.info("Cloud Callback resource info before sending mq:" + cloudMessage);
    pushCallbackInfo(cloudMessage);
  }

  private void pushCallbackInfo(String message) {
    messageSender.sendMessage(message);
  }

  public void setMessageSender(RabbitMQSender messageSender) {
    this.messageSender = messageSender;
  }

}
