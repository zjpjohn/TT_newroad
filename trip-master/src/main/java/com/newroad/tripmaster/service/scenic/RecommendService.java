package com.newroad.tripmaster.service.scenic;

import java.util.List;

import net.sf.json.JSONObject;

import com.newroad.tripmaster.dao.NewsDao;
import com.newroad.tripmaster.dao.ScenicDao;
import com.newroad.tripmaster.dao.pojo.News;
import com.newroad.tripmaster.service.RecommendServiceIf;
import com.newroad.util.apiresult.ServiceResult;


public class RecommendService implements RecommendServiceIf {

  private NewsDao newsDao;
  
  private ScenicDao scenicDao;
  
  public ServiceResult<JSONObject> select() {
    ServiceResult<JSONObject> result = new ServiceResult<JSONObject>();
    List<News> newsList=newsDao.getNewsList();
    //JSONArray newsArray=JSONArray.fromObject(newsList);
    return null;
  }

  public ServiceResult<JSONObject> listScenic(String userId) {
    // TODO Auto-generated method stub
    return null;
  }

  
  public ServiceResult<JSONObject> userSelect(String userId) {
    // TODO Auto-generated method stub
    return null;
  }
  
  
  public ServiceResult<JSONObject> listUserScenic(String userId) {
    // TODO Auto-generated method stub
    return null;
  }

}
