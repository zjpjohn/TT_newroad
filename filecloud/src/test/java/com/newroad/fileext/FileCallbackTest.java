package com.newroad.fileext;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newroad.fileext.service.cloud.CloudSyncService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-mvc.xml"})
public class FileCallbackTest {

  @Autowired
  private CloudSyncService cloudService;

  public void setCloudService(CloudSyncService cloudService) {
    this.cloudService = cloudService;
  }

  @Before
  public void setUp() throws Exception {}
  
  @Ignore
  @Test
  public void testFileCallback() {
    JSONObject json = new JSONObject();
    json.put("token", "test");
    cloudService.syncCloudFile(json.toString());
  }
}
