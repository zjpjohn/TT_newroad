package com.newroad.tripmaster.info.crawler;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.newroad.tripmaster.info.crawler.dao.ScenicDao;

@Component
public class SpringContextUtil implements ApplicationContextAware {

  static {
    applicationContext =
        new ClassPathXmlApplicationContext(new String[] {
            "tripmaster/spring-service.xml"});
  }

  private static ApplicationContext applicationContext; // Spring应用上下文环境

  /*
   * 
   * 实现了ApplicationContextAware 接口，必须实现该方法；
   * 
   * 通过传递applicationContext参数初始化成员变量applicationContext
   */

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextUtil.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(String name) throws BeansException {
    return (T)applicationContext.getBean(name);
  }

  public static <T> T getBean(Class<T> classtype) throws BeansException {
    return (T)applicationContext.getBean(classtype);
  }

  public static ScenicDao getScenicDao() {
    return applicationContext.getBean(ScenicDao.class);
  }

}
