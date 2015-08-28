package com.newroad.tripmaster.service;

import net.sf.json.JSONObject;

import com.newroad.util.apiresult.ServiceResult;


public interface RecommendServiceIf {

  public ServiceResult<JSONObject> select();
  
  public ServiceResult<JSONObject> userSelect(String userId);
  
  public ServiceResult<JSONObject> listScenic(String userId);
  
  public ServiceResult<JSONObject> listUserScenic(String userId);
}
