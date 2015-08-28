package com.newroad.tripmaster.service.scenic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.dao.TravelDateUnitDao;
import com.newroad.tripmaster.dao.pojo.trip.DateInfo;
import com.newroad.tripmaster.dao.pojo.trip.TravelDateUnit;

public class ProductDateInventoryService {

  private static Logger logger = LoggerFactory.getLogger(ProductDateInventoryService.class);

  private TravelDateUnitDao travelDateUnitDao;

  public void createProductDateInventory(String tripProductId, List<TravelDateUnit> travelDateList) {
    for (TravelDateUnit travelDateUnit : travelDateList) {
      travelDateUnit.setTripProductId(tripProductId);
      Object idObj2 = travelDateUnitDao.saveTravelDateUnit(travelDateUnit);
      if (idObj2 == null) {
        logger.error("Fail to create travel date unit when yearMonth is " + travelDateUnit.getYearMonth() + "!");
      }
    }
  }

  public Integer updateProductOrderBuyQuantity(String tripProductId, String yearMonth, String date, Integer increment) {
    TravelDateUnit travelDateUnit = travelDateUnitDao.getTravelDateUnit(tripProductId, yearMonth, 1);
    Integer updateCount = 0;
    if (travelDateUnit != null) {
      List<DateInfo> dateInfoList = travelDateUnit.getDateList();
      for (DateInfo dateInfo : dateInfoList) {
        if (date.equals(dateInfo.getDate())) {
          Integer buyQuantity = dateInfo.getBuyQuantity() + increment;
          dateInfo.setBuyQuantity(buyQuantity);
          break;
        }
      }
      updateCount = travelDateUnitDao.updateTravelDateUnit(tripProductId, yearMonth, dateInfoList, 1, false);
    }
    return updateCount;
  }

  /**
   * @info update the specific product date inventory
   * @param tripProductId
   * @param yearMonth
   * @param date
   * @param inventory
   * @param availible
   * @return
   */
  public Integer updateProductDateInventory(String tripProductId, String yearMonth, String date, Integer inventory, Integer buyQuantity) {
    TravelDateUnit travelDateUnit = travelDateUnitDao.getTravelDateUnit(tripProductId, yearMonth, 1);
    List<DateInfo> dateInfoList = travelDateUnit.getDateList();
    for (DateInfo dateInfo : dateInfoList) {
      if (date.equals(dateInfo.getDate())) {
        if (inventory != null) {
          dateInfo.setMaxInventory(inventory);
        }
        if (buyQuantity != null) {
          dateInfo.setBuyQuantity(buyQuantity);
        }
        break;
      }
    }
    Integer updateCount = travelDateUnitDao.updateTravelDateUnit(tripProductId, yearMonth, dateInfoList, 1, false);
    return updateCount;
  }

  public Integer updateProductDateUnit(String tripProductId, List<TravelDateUnit> travelDateList) {
    Integer updateStatusCount = travelDateUnitDao.updateTravelDateUnitStatus(tripProductId, null, 0);
    logger.info("Ignore old TravelDateUnit count:" + updateStatusCount);
    Integer updateCount = 0;
    for (TravelDateUnit travelDate : travelDateList) {
      updateCount += travelDateUnitDao.updateTravelDateUnit(tripProductId, travelDate.getYearMonth(), travelDate.getDateList(), 1, true);
    }
    return updateCount;
  }

  public List<TravelDateUnit> listProductDateInventory(String tripProductId) {
    List<TravelDateUnit> travelDateList = travelDateUnitDao.listTravelDateUnit(tripProductId, null, 1);
    return travelDateList;
  }

  public void setTravelDateUnitDao(TravelDateUnitDao travelDateUnitDao) {
    this.travelDateUnitDao = travelDateUnitDao;
  }


}
