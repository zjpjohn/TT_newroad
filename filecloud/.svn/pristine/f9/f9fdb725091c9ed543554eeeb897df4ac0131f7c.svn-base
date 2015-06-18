package com.newroad.fileext;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:mqtest.xml"})
public class RabbitMQClientTest {

  @Autowired
  private AmqpAdmin admin;
  @Autowired
  private AmqpTemplate template;

  @Test
  public void simpleProducerConsumerTest() {
    try {
      String sent = "Catch the rabbit! " + new Date();
      admin.declareQueue(new Queue("test.queue"));

      // write message
      template.convertAndSend(sent);
      // read message
      String received = (String) template.receiveAndConvert();

      System.out.println("Msg: " + received);
      Assert.assertEquals(sent, received);

    } catch (AmqpException e) {
      Assert.fail("Test failed: " + e.getLocalizedMessage());
    }
  }
}
