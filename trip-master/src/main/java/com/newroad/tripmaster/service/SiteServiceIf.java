package com.newroad.tripmaster.service;

import com.newroad.tripmaster.dao.pojo.Coordinate;
import com.newroad.tripmaster.dao.pojo.Site;
import com.newroad.util.apiresult.ServiceResult;

public interface SiteServiceIf {
  
    public ServiceResult<String> saveSite(Site site);
    
    public ServiceResult<String> listSites(Coordinate point,Integer sitetype);
    
    public ServiceResult<String> getSite(String hashSiteId);
}
