package com.newroad.fileext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.newroad.fileext.service.api.FileExtendServiceIf;
import com.newroad.fileext.service.cloud.CloudManageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-mvc.xml"})
public class ExtendServiceImplTest {

  @Autowired
  private FileExtendServiceIf fileExtendService;

  @Autowired
  private CloudManageService cloudManageService;

  public void setFileExtendService(FileExtendServiceIf fileExtendService) {
    this.fileExtendService = fileExtendService;
  }

  public void setCloudManageService(CloudManageService cloudManageService) {
    this.cloudManageService = cloudManageService;
  }

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  @Ignore
  public void testGetObject() {
    String connector =
        cloudManageService
            .getConnector(
                "supernote.lenovo.com",
                "ZAgAAAAAAAGE9MTAwMjk5NTkyNjUmYj0xJmM9MSZkPTEwNjQyJmU9OEFBNjhEMDk3QjU5QTUzQUVFQjQ5RTA0RDk3MzM3RkUxJmg9MTQwOTQ5Mjk3NDk3NiZpPTQzMjAwJmo9MFvJSZOdIWT9z1NyU4uvS_k");
    System.out.println("Connector:" + connector);
    JSONObject session = new JSONObject();
    session.put("account", "10029959265");

    JSONObject params = new JSONObject();
    params.put("clientResourceID", "6fa233682ba84302aa87dc6ec9ea1635");
    params.put("fileName", "IMG_20140830_163323.jpg");
    try {
      fileExtendService.downloadData(session, params);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  @Ignore
  public void testConcurrentGetObject() {
    JSONObject params = new JSONObject();
    params.put("resourceKeyId", "00405bd8bb600400");
    params.put("resourceLink", "");
    params.put("fileName", "e2c006d2-8aa1-41f7-a910-e63137050ce9.kk");
    try {
      // fileExtendService.downloadData(null, params, false);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  @Ignore
  public void testDeletePublicLink() {
    JSONArray array = new JSONArray();
    JSONObject json = new JSONObject();
    json.put("resourceKeyId", "004048e3a49b0300");
    json.put("resourceLink", "https://iocos.lenovows.com/v2/object/00402aa02e2bf800:lenote/004048e3a49b0300");
    array.add(json);
    JSONObject json2 = new JSONObject();
    json2.put("resourceKeyId", "004048e3a2550400");
    json2.put("resourceLink", "https://iocos.lenovows.com/v2/object/00402aa02e2bf800:lenote/004048e3a2550400");
    array.add(json2);

    JSONObject param = new JSONObject();
    param.put("resources", array);
    try {
      fileExtendService.deletePublicLink(null, param);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Test
  @Ignore
  public void testBatchCreatePublicLink() {
    JSONArray array = new JSONArray();
    JSONObject json = new JSONObject();
    json.put("resourceKeyId", "004048e3a49b0300");
    json.put("resourceLink", "https://iocos.lenovows.com/v2/object/00402aa02e2bf800:lenote/004048e3a49b0300");
    array.add(json);
    JSONObject json2 = new JSONObject();
    json2.put("resourceKeyId", "004048e3a2550400");
    json2.put("resourceLink", "https://iocos.lenovows.com/v2/object/00402aa02e2bf800:lenote/004048e3a2550400");
    array.add(json2);

    JSONObject param = new JSONObject();
    param.put("resources", array);
    param.put("linkValidTime", 10000);
    try {
      fileExtendService.batchCreatePublicLink(null, param);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


}
