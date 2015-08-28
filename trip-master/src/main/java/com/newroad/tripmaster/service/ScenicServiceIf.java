package com.newroad.tripmaster.service;

import com.newroad.tripmaster.dao.pojo.Scenic;
import com.newroad.util.apiresult.ServiceResult;

public interface ScenicServiceIf {
  
  public ServiceResult<String> summaryScenic(String siteId);
  
  public ServiceResult<String> detailScenic(String siteId);
  
  public ServiceResult<String> saveScenic(Scenic scenic);
}
