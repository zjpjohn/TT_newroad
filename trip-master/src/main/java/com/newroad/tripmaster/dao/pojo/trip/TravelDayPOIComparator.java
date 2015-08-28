package com.newroad.tripmaster.dao.pojo.trip;

import java.util.Comparator;

public class TravelDayPOIComparator implements Comparator<POIRoute> {

  public int compare(POIRoute p1, POIRoute p2) {
    if (p1 == p2) {
      return 0;
    } else if (p1.getRouteDay().equals(p2.getRouteDay())
        && p1.getTravelPOI().getTravelPOIId().equals(p2.getTravelPOI().getTravelPOIId())) {
      return 0;
    }
    return -1;
  }

}
