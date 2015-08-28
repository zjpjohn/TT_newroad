package com.newroad.tripmaster.info.crawler.cloud;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;

public class QiuDownloadManager {

  
  Auth auth = Auth.create(QiNiuCloudManager.ACCESS_KEY, QiNiuCloudManager.SECRET_KEY);
  
  private BucketManager bucketManager = new BucketManager(auth);

  
  public void download(String url){
    try {
      String[] buckets = bucketManager.buckets();
      //bucketManager.fetch(url, bucket, key);
    } catch (QiniuException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
