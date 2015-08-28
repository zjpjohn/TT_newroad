package com.newroad.tripmaster.info.crawler.cloud;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class QiniuUploadManager {

  public static Logger logger = LoggerFactory.getLogger(QiniuUploadManager.class);

  Auth auth = Auth.create(QiNiuCloudManager.ACCESS_KEY, QiNiuCloudManager.SECRET_KEY);

  private static String BUCKET_NAME = "xingzhe-bucket";

  private static long EXPIRE = 3600;

  private String cloudURL = null;

  private String privateCloudURL = null;

  UploadManager uploadManager = new UploadManager();

  private String getUpToken(String bucket, String key, long expires, StringMap policy, boolean strict) {
    return auth.uploadToken(bucket, key, expires, new StringMap().putNotEmpty("persistentOps", "").putNotEmpty("persistentNotifyUrl", "")
        .putNotEmpty("persistentPipeline", ""), strict);
  }

  public String uploadToken(String key) {
    StringMap stringMap =
        new StringMap().putNotEmpty("persistentOps", "").putNotEmpty("persistentNotifyUrl", "").putNotEmpty("persistentPipeline", "");
    return getUpToken(BUCKET_NAME, key, EXPIRE, stringMap, true);
  }

  public void upload(File file) {
    String key = QiNiuCloudManager.generateKeyId() + "_" + file.getName();
    String mimetype = "";
    try {
      Response res = uploadManager.put(file, key, uploadToken(key));
      MyRet ret = res.jsonToObject(MyRet.class);
      // logger.info(res.toString());
      // logger.info(res.bodyString());
      cloudURL = "http://" + QiNiuCloudManager.DOMAIN + "/" + key;
      logger.info("cloudURL:" + cloudURL);
      // privateCloudURL = auth.privateDownloadUrl(cloudURL);
    } catch (QiniuException e) {
      Response r = e.response;
      logger.error(r.toString());
      try {
        logger.error(r.bodyString());
      } catch (QiniuException e1) {
        // ignore
      }
    }
  }

  public String getFileCloudURL() {
    return cloudURL;
  }

  public class MyRet {

    public long fsize;
    public String key;
    public String hash;
    public int width;
    public int height;
  }
}
