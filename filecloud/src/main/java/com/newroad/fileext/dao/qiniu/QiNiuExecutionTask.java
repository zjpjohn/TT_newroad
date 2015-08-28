package com.newroad.fileext.dao.qiniu;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.fileext.dao.CloudFileExecutionCode;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.CommonFileData;
import com.newroad.fileext.utilities.JSONConvertor;
import com.newroad.fileext.utilities.SystemProperties;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.RecordKeyGenerator;
import com.qiniu.storage.Recorder;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class QiNiuExecutionTask implements Callable<Object> {

  private static Logger logger = LoggerFactory.getLogger(QiNiuExecutionTask.class);

  private CloudFileExecutionCode executionCode;
  private CloudFileData fileData;

  private static String ACCESS_KEY = "YVPIXWdG3mxK6TBBRIqmO0P3wBJdf-jDUC3_3AYr";
  private static String SECRET_KEY = "Sc-hpjZQWDuMWA47dXxJUYq-0YKTGzH3cJcSuh-F";
  private static String DOMAIN = "http://7xjqgn.com1.z0.glb.clouddn.com";
  private UploadManager uploadManager = null;
  private Auth auth = Auth.create(getAK(), getSK());

  public QiNiuExecutionTask(CloudFileExecutionCode executionCode, CloudFileData fileData) {
    this.executionCode = executionCode;
    this.fileData = fileData;
  }

  @Override
  public CloudFileData call() throws Exception {
    String bucket = SystemProperties.bucketName;
    switch (executionCode) {
      case PUT_OBJECT:
        long uploadExpire = 3600 * 1;
        String fileKey = fileData.getKey();
        String upToken = getUpToken(bucket, fileKey, uploadExpire, null, false);
        if (fileData.getFileByte() == null) {
          return null;
        }
        uploadFile(fileData.getFileByte(), fileKey, upToken);
        break;
      case GET_OBJECT:
        long downloadExpire = 3600 * 24;
        downloadFile(fileData.getLink(), downloadExpire);
        break;
      default:
        break;
    }
    return fileData;
  }


  private void uploadFile(byte[] data, String key, String upToken) {
    uploadManager = new UploadManager();
    try {
      Response res = uploadManager.put(data, key, upToken);
      if (res.isOK()) {
        CloudFileData cloudFileData = JSONConvertor.getJSONInstance().fromJson(res.bodyString(), CloudFileData.class);
        fileData.setKey(cloudFileData.getKey());
        fileData.setHash(cloudFileData.getHash());
        fileData.setWidth(cloudFileData.getWidth());
        fileData.setHeight(cloudFileData.getHeight());
        fileData.setLink(DOMAIN + "/" + fileData.getKey());
        fileData.setFileByte(null);
      }
    } catch (QiniuException e) {
      Response r = e.response;
      // 请求失败时简单状态信息
      logger.error("response:" + r.toString());
      try {
        // 响应的文本信息
        logger.error("responseBody:" + r.bodyString());
      } catch (QiniuException e1) {
        // ignore
      }
    }
  }

  @SuppressWarnings("unused")
  private void uploadFileRecorder(String filePath, byte[] data, String key, String upToken) {
    Recorder recorder = null;
    try {
      recorder = new FileRecorder(new File(filePath));
      RecordKeyGenerator recordGen = new RecordKeyGenerator() {
        @Override
        public String gen(String key, File file) {
          return key + "_._" + file.getAbsolutePath();
        }
      };
      uploadManager = new UploadManager(recorder, recordGen);
      Response res = uploadManager.put(data, key, upToken);
      if (res.isOK()) {
        CommonFileData ret = res.jsonToObject(CommonFileData.class);
        logger.info(res.toString());
        logger.info(res.bodyString());
      } else {

      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private String getUpToken(String bucket, String key, long expires, StringMap policy, boolean strict) {
    if (policy == null) {
      policy =
          new StringMap().putNotEmpty("returnBody",
              "{\"key\": $(key), \"hash\": $(etag), \"width\": $(imageInfo.width), \"height\": $(imageInfo.height)}");
    }
    return auth.uploadToken(bucket, key, expires, policy, strict);
  }


  private String downloadFile(String url, long expires) {
    return auth.privateDownloadUrl(url, expires);
  }

  private String getAK() {
    return ACCESS_KEY;

  }

  private String getSK() {
    return SECRET_KEY;

  }
}
