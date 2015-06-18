package com.newroad.fileext;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newroad.fileext.service.cloud.CloudManageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-mvc.xml"})
public class CloudManageServiceTest {

  @Autowired
  private CloudManageService manageService;

  public void setManageService(CloudManageService manageService) {
    this.manageService = manageService;
  }

  @Before
  public void setUp() throws Exception {}
  
  @Ignore
  @Test
  public void testRectifyCloudResource() {
    manageService.rectifyCloudResource();
  }
}
