package com.newroad.tripmaster.info.crawler;

import com.newroad.tripmaster.info.crawler.service.SitePictureManager;

public class SitePictureAction {
  

  public static void main(String[] args) {
    SitePictureManager pictureManager=new SitePictureManager();
    pictureManager.updateSitePictureInfo();
  }
}
