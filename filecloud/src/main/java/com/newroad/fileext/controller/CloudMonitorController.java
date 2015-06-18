package com.newroad.fileext.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newroad.fileext.filter.TokenAuthFilter;
import com.newroad.fileext.service.cloud.CloudSyncService;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;

/**
 * @author tangzj1
 * @version 2.0
 * @since May 19, 2014
 */
@Controller
@RequestMapping("/v{apiVersion}/cloud")
public class CloudMonitorController {

  private static Logger logger = LoggerFactory.getLogger(CloudMonitorController.class);

  @Autowired
  private CloudSyncService cloudService;

  @RequestMapping(value = "/syncCloudFile", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String syncCloudFile(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    String requestStr = StringHelper.getRequestEntityString(request);
    logger.debug("syncCloudFile start!");
    JSONObject session = TokenAuthFilter.getCurrent();
    if(session==null){
      return ApiReturnObjectUtil.getReturnObjFromException(new Exception("Session is null")).toString();
    }
    try {
      cloudService.syncCloudFile(assemblyResourceInfo(session, requestStr).toString());
    } catch (Exception e) {
      logger.error("syncCloudFile Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
    logger.debug("syncCloudFile completed!");
    return null;
  }

  private JSONObject assemblyResourceInfo(JSONObject sessionUser, String requestStr) {
    if (requestStr == null || "".equals(requestStr)) {
      throw new FileResourceException("Cloud callback resource is null!");
    }
    JSONArray resourceArray = new JSONArray();
    int startIndex = requestStr.indexOf("[{");
    int endIndex = requestStr.lastIndexOf("}]");
    if (startIndex >= 0 && endIndex >= 0) {
      JSONArray param = JSONArray.fromObject(requestStr);
      int arraySize = param.size();
      for (int i = 0; i < arraySize; i++) {
        JSONObject object = (JSONObject) param.get(i);
        JSONObject newObj = new JSONObject();
        newObj.put("key", (String) object.get("key"));
        newObj.put("link", (String) object.get("location"));
        newObj.put("size", (Integer) object.get("size"));
        Integer createTime = (Integer) object.get("time_created");
        Integer lastUpdateTime = (Integer) object.get("last_modified");
        if (createTime != null)
          newObj.put("createTime", createTime * 1000);
        if (lastUpdateTime != null)
          newObj.put("lastUpdateTime", lastUpdateTime * 1000);
        resourceArray.add(newObj);
      }
    } else {
      JSONObject param = JSONObject.fromObject(requestStr);
      JSONObject newObj = new JSONObject();
      newObj.put("key", (String) param.get("key"));
      newObj.put("link", (String) param.get("location"));
      newObj.put("size", (Integer) param.get("size"));
      Integer createTime = (Integer) param.get("time_created");
      Integer lastUpdateTime = (Integer) param.get("last_modified");
      if (createTime != null)
        newObj.put("createTime", createTime * 1000);
      if (lastUpdateTime != null)
        newObj.put("lastUpdateTime", lastUpdateTime * 1000);
      resourceArray.add(newObj);
    }
    JSONObject resource = new JSONObject();
    resource.put("resources", resourceArray);
    resource.put("sessionUser", sessionUser);
    return resource;
  }
}
