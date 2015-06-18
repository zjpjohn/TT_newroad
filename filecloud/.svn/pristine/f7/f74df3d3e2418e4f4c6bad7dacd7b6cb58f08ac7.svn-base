package com.newroad.fileext.service.cloud;

import java.util.concurrent.ThreadPoolExecutor;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.newroad.fileext.data.model.CommonFileData;
import com.newroad.fileext.service.api.FileCacheTaskQueueIf;
import com.newroad.fileext.service.mq.RabbitMQListener;

public class CacheCallbackTaskHandler extends RabbitMQListener {

  private static Logger logger = LoggerFactory.getLogger(CacheCallbackTaskHandler.class);

  private FileCacheTaskQueueIf cacheTaskQueue;

  private ThreadPoolTaskExecutor threadPoolTaskExecutor;

  @Override
  public void onMessage(Message message) {
    String messageBody = new String(message.getBody());
    JSONObject messageJSON;
    try {
      messageJSON = JSONObject.fromObject(messageBody);
      JSONObject sessionUser = (JSONObject) messageJSON.get("sessionUser");
      JSONObject fileResourcejson = (JSONObject) messageJSON.get("fileResource");
      CommonFileData fileResource = (CommonFileData) JSONObject.toBean(fileResourcejson, CommonFileData.class);
      Thread.sleep(5000);
      cacheCallbackResourceTask(sessionUser, fileResource);
    } catch (Exception e) {
      logger.error("CloudCallbackHandler Exception:", e);
    }
  }

  private void cacheCallbackResourceTask(JSONObject sessionUser, CommonFileData fileResource) {
    fileResource = cacheTaskQueue.cacheFileResource(sessionUser, fileResource);
    String cacheFilePath = fileResource.getCloudFileData().getCacheFileData().getFileCachePath();
    logger.info("cacheCallbackResourceTask file cache path is " + cacheFilePath);
//    for (ThumbnailType thumbnail : ThumbnailType.values()) {
//      JSONObject json = new JSONObject();
//      json.put("thumbnailFilePath", cacheFilePath + "_" + thumbnail.getWidth());
//      json.put("thumbnailKey", thumbnail.getWidth());
//      cacheTaskQueue.cacheThumbnail(cacheFilePath, thumbnail, false);
//    }
  }

  public void setCacheTaskQueue(FileCacheTaskQueue cacheTaskQueue) {
    this.cacheTaskQueue = cacheTaskQueue;
  }

  public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
    this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    this.threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
  }
}
