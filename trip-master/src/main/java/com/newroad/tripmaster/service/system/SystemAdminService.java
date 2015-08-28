package com.newroad.tripmaster.service.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.dao.maria.MariaDao;
import com.newroad.tripmaster.dao.pojo.Lucker;
import com.newroad.tripmaster.service.SystemAdminServiceIf;
import com.newroad.tripmaster.service.scenic.ProductDesignService;
import com.newroad.util.apiresult.ServiceResult;

public class SystemAdminService implements SystemAdminServiceIf {

  private static Logger logger = LoggerFactory.getLogger(SystemAdminService.class);

  private MariaDao mariaDao;

  @Override
  public ServiceResult<String> createNewLucker(Lucker lucker) {
    // TODO Auto-generated method stub
    return null;
  }



  public void setMariaDao(MariaDao mariaDao) {
    this.mariaDao = mariaDao;
  }

}
