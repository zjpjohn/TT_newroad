package com.newroad.fileext.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileDataUtils {
  
  public static String getCurrentDateInfo() {
    SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
    Date currentDate = new Date();
    String dateHeader = format.format(currentDate);
    return dateHeader;
  }
}
