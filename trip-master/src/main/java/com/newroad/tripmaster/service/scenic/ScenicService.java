package com.newroad.tripmaster.service.scenic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.ScenicDao;
import com.newroad.tripmaster.dao.pojo.Scenic;
import com.newroad.tripmaster.service.ScenicServiceIf;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.apiresult.ServiceResult;

public class ScenicService implements ScenicServiceIf {

  private static Logger logger = LoggerFactory.getLogger(ScenicService.class);

  private ScenicDao scenicDao;

  public ServiceResult<String> summaryScenic(String siteId) {
    ServiceResult<String> result = new ServiceResult<String>();
    Scenic scenic = scenicDao.detailScenic(siteId);
    if(scenic==null){
      result.setReturnCode(ReturnCode.DATA_NOT_FOUND);
      result.setReturnMessage("No scenic detail info!");
      return result;
    }
    String json = JSONConvertor.transformScenicDetail2JSON(scenic);
    logger.info("Scenic Summary Info:" + json);
    result.setBusinessResult(json);
    return result;
  }

  public ServiceResult<String> detailScenic(String siteId) {
    ServiceResult<String> result = new ServiceResult<String>();
    Scenic scenic = scenicDao.detailScenic(siteId);
    if(scenic==null){
      result.setReturnCode(ReturnCode.DATA_NOT_FOUND);
      result.setReturnMessage("No scenic detail info!");
      return result;
    }
    String parentScenicId = scenic.getScenicId().toString();
    if (parentScenicId != null) {
      List<Scenic> childScenicList = scenicDao.listChildScenics(parentScenicId);
      if (childScenicList.size() > 0) {
        scenic.setChildScenic(childScenicList);
      }
    }
    String json = JSONConvertor.getJSONInstance().writeValueAsString(scenic);
    logger.info("Scenic detail Info:" + json);
    result.setBusinessResult(json);
    return result;
  }

  public ServiceResult<String> saveScenic(Scenic scenic) {
    ServiceResult<String> result = new ServiceResult<String>();
    Object idObj = scenicDao.save(scenic);
    logger.info("Scenic object id:" + idObj);
    result.setBusinessResult(idObj.toString());
    return result;
  }

  public void setScenicDao(ScenicDao scenicDao) {
    this.scenicDao = scenicDao;
  }


}
