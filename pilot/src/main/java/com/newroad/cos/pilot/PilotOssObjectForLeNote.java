package com.newroad.cos.pilot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cos.pilot.util.CallbackData;
import com.newroad.cos.pilot.util.PilotOssConstants;
import com.newroad.cos.pilot.util.PilotOssConstants.ThumbnailGenerateType;

public class PilotOssObjectForLeNote implements PilotOssObjectBaseEx, OssManagerListener {

  private final Logger logger = LoggerFactory.getLogger(PilotOssObjectForLeNote.class);

  protected OssManager ossMgr = null;
  protected String bucketName = null;
  protected String keyID = null;
  protected String[] keys = null;
  protected String url = null;

  protected Map<String, String> objectInfoMap = null;

  protected boolean isContinue = false;
  protected PilotOssListenerEx listener = null;
  // Max Stream Segment 5M
  protected final long MAX_STREAM_SEGMENT = 1024 * 1024 * 5;
  // Max Stream Sub Segment 1M
  protected final long MAX_STREAM_SUB_SEGMENT = 1024 * 1024;

  protected PilotOssObjectForLeNote(OssManager ossMgr, String bucketName, String... keyIDs) {
    this.bucketName = bucketName;
    if (keyIDs != null) {
      int keySize = keyIDs.length;
      if (keySize == 1) {
        this.keyID = keyIDs[0];
        this.keys = keyIDs;
        this.url = PilotOssHelper.buildObjIOUrl(bucketName, null, keyID);
      } else {
        this.keys = keyIDs;
        this.url = PilotOssHelper.buildObjIOUrl(bucketName, null);
      }
    }
    this.ossMgr = ossMgr;
  }

  protected PilotOssObjectForLeNote(OssManager ossMgr, String url) {
    this.url = url;
    this.ossMgr = ossMgr;
  }

  // protected PilotOssObjectForLeNote(String bucketName, String keyID,
  // String url, OssManager ossMgr) {
  // this.bucketName = bucketName;
  // this.keyID = keyID;
  // if (url != null) {
  // this.url = url;
  // } else {
  // this.url = PilotOssHelper.buildObjIOUrl(bucketName, null, keyID);
  // }
  // this.ossMgr = ossMgr;
  // }

  public String getBucketName() {
    return bucketName;
  }

  public String getKeyID() {
    return keyID;
  }

  public String getUrl() {
    return url;
  }

  public String[] getKeyIDs() {
    return keys;
  }

  @Override
  public Map<String, String> getObjectInfoMap() {
    return objectInfoMap;
  }

  public void registerListener(PilotOssListenerEx listener) {
    synchronized (this) {
      if (listener != null) {
        this.listener = listener;
      }
    }
  }

  public void unregisterListener(PilotOssListenerEx listener) {
    synchronized (this) {
      if (this.listener != null) {
        if (this.listener.equals(listener)) {
          this.listener = null;
        }
      }
    }
  }

  public boolean putObject(File uploadFile, String contentType, long objectOffset, long size, Object paramCallbackData, Object userData)
      throws PilotException {
    try {
      InputStream stream = new FileInputStream(uploadFile);
      return putObject(stream, contentType, objectOffset, size, paramCallbackData, userData);
    } catch (FileNotFoundException e) {
      //logger.error("putObject FileNotFoundException:", e);
      throw new PilotException("putObject FileNotFoundException!", e);
    }
  }

  public boolean putObject(InputStream stream, String contentType, long objectOffset, long size, Object paramCallbackData, Object userData)
      throws PilotException {
    if (size == 0) {
      throw new PilotException("The size of object " + keyID + " is zero");
    }
    ProgressData progress = new ProgressData(size, userData);
    synchronized (this) {
      isContinue = true;
    }

    CallbackData cbData = null;
    if (paramCallbackData != null) {
      cbData = (CallbackData) paramCallbackData;
    } else {
      cbData = new CallbackData();
    }
    try {
      if (cbData.getHeaderData() == null)
        cbData.setHeaderData(new HashMap<String, String>());
      if (objectOffset == 0 && size <= MAX_STREAM_SUB_SEGMENT) {
        byte[] buf = new byte[(int) size];
        readFromStream(buf, stream, (int) size);
        int loop = 3;
        // send start signal here
        notifyStart(userData);
        while (true) {
          try {
            ossMgr.writeObject(bucketName, keyID, contentType, new ByteArrayInputStream(buf, 0, (int) size), size, this, cbData, progress);
          } catch (IOException e) {
            String msg = e.getMessage();
            if (msg == null || msg.compareToIgnoreCase("user abort") != 0) {
              loop--;
              if (loop == 0)
                throw e;
              continue;
            }
            throw e;
          }
          break;
        }
        notifyClient(TaskStatus.TASK_COMPLETED, size, size, userData, 0);
      } else {
        long segmentCount = (size + MAX_STREAM_SEGMENT - 1) / MAX_STREAM_SEGMENT;
        long remain = size;

        Long partNumber;
        // send start signal here
        notifyStart(userData);
        for (long i = 0; i < segmentCount; i++) {
          long sendLen = remain > MAX_STREAM_SEGMENT ? MAX_STREAM_SEGMENT : remain;
          long offset = objectOffset + progress.completed;
          partNumber = i + 1;
          cbData.getHeaderData().put("partnumber", partNumber.toString());
          sendData(bucketName, keyID, contentType, stream, sendLen, offset, this, cbData, progress);
          // data.completed += sendLen;
          remain -= sendLen;
        }
        cbData.getHeaderData().remove("range");
        ossMgr.commitObjectByChunk(url, contentType, cbData);
        notifyClient(TaskStatus.TASK_COMPLETED, size, size, userData, 0);
      }
      return true;
    } catch (Exception e) {
      isContinue = false;
      notifyStop(progress.completed, progress.total, userData, 1);
      //logger.error("putObject Exception for object key " + keyID + ":", e);
      PilotException err = new PilotException("putObject Exception for object key " + keyID + ":", e);
      throw err;
    } finally {
      try {
        if (stream != null) {
          stream.close();
        }
      } catch (IOException e) {
        logger.error("putObject close stream IOException for object key " + keyID + ":", e);
      }
    }
  }

  public boolean getObject(OutputStream stream, long objectOffset, long size, Object userData) throws PilotException {
    long fileSize = 0l;
    try {
      if (objectInfoMap == null) {
        throw new PilotException("Couldn't get object info properly because objectInfoMap is null!");
      }
      fileSize = Long.parseLong(objectInfoMap.get("size"));
      if (fileSize == 0) {
        throw new PilotException("The size of object key " + keyID + " in objectInfoMap is zero!");
      } else if (fileSize != size) {
        logger.info("The cos file size of object key " + keyID + " (" + fileSize + ") is inconsistent with the input size (" + size + ")!");
      }
    } catch (Exception e) {
      //logger.error("Couldn't get Object size for object key " + keyID + " Exception:", e);
      throw new PilotException("Couldn't  get Object size for object key " + keyID + " Exception:", e);
    }

    synchronized (this) {
      isContinue = true;
    }

    ProgressData data = new ProgressData(fileSize, userData);
    try {
      // send start signal here
      notifyStart(userData);
      if (objectOffset == 0 && fileSize <= MAX_STREAM_SEGMENT) {
        ossMgr.readObject(url, stream, fileSize, null, this, data);
        notifyClient(TaskStatus.TASK_COMPLETED, fileSize, fileSize, userData, 0);
      } else {
        long segmentCount = (fileSize + MAX_STREAM_SEGMENT - 1) / MAX_STREAM_SEGMENT;
        long remain = fileSize;
        for (long i = 0; i < segmentCount; i++) {
          long readLen = remain > MAX_STREAM_SEGMENT ? MAX_STREAM_SEGMENT : remain;
          long offset = objectOffset + data.completed;
          ossMgr.readObjectByChunk(url, stream, offset, readLen, null, this, data);
          data.completed += readLen;
          remain -= readLen;
        }
        notifyClient(TaskStatus.TASK_COMPLETED, fileSize, fileSize, userData, 0);
      }
      return true;
    } catch (Exception e) {
      isContinue = false;
      notifyStop(data.completed, data.total, userData, 1);
      //logger.error("getObject Exception for object key " + keyID + ":", e);
      PilotException err = new PilotException("getObject Exception for object key " + keyID + ":", e);
      throw err;
    } finally {
      try {
        if (stream != null)
          stream.close();
      } catch (IOException e) {
        logger.error("getObject close stream IOException for object key " + keyID + ":", e);
      }
    }
  }

  public Map<String, String> deleteObject(Object param) throws PilotException {
    try {
      return ossMgr.deleteObject(url, (CallbackData) param);
    } catch (IOException e) {
      //logger.error("deleteObject IOException for object key " + keyID + ":", e);
      PilotException err = new PilotException("deleteObject IOException for object key " + keyID + ":", e);
      throw err;
    }
  }

  public Map<String, String> getObjectInfo() throws PilotException {
    try {
      Map<String, String> objectMap = ossMgr.getObjectInfo(url);
      this.objectInfoMap = objectMap;
      return objectInfoMap;
    } catch (IOException e) {
      //logger.error("getObjectInfo IOException for object key " + keyID + ":", e);
      PilotException err = new PilotException("getObjectInfo IOException for object key " + keyID + ":", e);
      throw err;
    }
  }

  public byte[] getObjectThumbnail(int width, int height, ThumbnailGenerateType thumbnailType, String displayName) throws PilotException {
    Map<String, String> param = new HashMap<String, String>();
    param = generateScale(width, height, param);
    if (thumbnailType != null) {
      param.put(PilotOssConstants.TYPE, thumbnailType.toString());
    }
    if (displayName != null) {
      param.put(PilotOssConstants.DISPLAY_NAME, displayName);
    }
    try {
      return ossMgr.getObjectThumbnail(url, param);
    } catch (IOException e) {
      //logger.error("getObjectThumbnail IOException for object key " + keyID + ":", e);
      PilotException err = new PilotException("getObjectThumbnail IOException for object key " + keyID + ":", e);
      throw err;
    }
  }

  public byte[] getObjectDocThumbnail(int width, int height, int PageNumber) throws PilotException {
    Map<String, String> param = new HashMap<String, String>();
    param = generateScale(width, height, param);
    param.put(PilotOssConstants.PAGE_NUMBER, String.valueOf(PageNumber));

    try {
      return ossMgr.getObjectDocThumbnail(url, param);
    } catch (IOException e) {
      //logger.error("getObjectDocThumbnail IOException for object key " + keyID + ":", e);
      PilotException err = new PilotException("getObjectDocThumbnail IOException for object key " + keyID + ":", e);
      throw err;
    }
  }

  public String createObjectPublicLink(int validTime) throws PilotException {
    Map<String, String> param = new HashMap<String, String>();
    param.put(PilotOssConstants.VALID_TIME, String.valueOf(validTime));
    try {
      return ossMgr.createObjectPublicLink(url, param, null);
    } catch (IOException e) {
      //logger.error("createObjectPublicLink IOException for object key " + keyID + ":", e);
      PilotException err = new PilotException("createObjectPublicLink IOException for object key " + keyID + ":", e);
      throw err;
    }
  }

  public String deleteObjectPublicLink(Object param) throws PilotException {
    CallbackData cbData = null;
    if (param != null) {
      cbData = (CallbackData) param;
    }
    try {
      return ossMgr.deleteObjectPublicLink(url, cbData);
    } catch (IOException e) {
      //logger.error("deleteObjectPublicLink IOException for object key " + keyID + ":", e);
      PilotException err = new PilotException("deleteObjectPublicLink IOException for object key " + keyID + ":", e);
      throw err;
    }
  }

  public void setThumbnailOrientation(int orientation) throws PilotException {
    try {
      Map<String, String> map = new HashMap<String, String>();
      map.put("bucket_name", bucketName);
      map.put("key", keyID);
      Integer tmp = orientation;
      map.put("orientation", tmp.toString());
      CallbackData data = new CallbackData();
      data.setData(map);
      ossMgr.setThumbnailOrientation(url, data);
    } catch (IOException e) {
      //logger.error("setThumbnailOrientation IOException for object key " + keyID + ":", e);
      PilotException err = new PilotException("setThumbnailOrientation IOException for object key " + keyID + ":", e);
      throw err;
    }
  }

  private void readFromStream(byte[] buffer, InputStream stream, int len) throws Exception {
    int offset = 0;
    while (offset < len) {
      int readLen = stream.read(buffer, offset, len - offset);
      if (readLen == -1 || readLen == 0) {
        if (offset != len)
          throw new IllegalArgumentException();
      }
      offset += readLen;
    }
  }

  private void sendData(String bucketName, String keyID, String contentType, InputStream input, long size, long offset,
      OssManagerListener listener, CallbackData callbackData, Object userdata) throws IOException {
    byte[] buf = new byte[(int) MAX_STREAM_SUB_SEGMENT];
    long segmentCount = (size + MAX_STREAM_SUB_SEGMENT - 1) / MAX_STREAM_SUB_SEGMENT;
    long remain = size;
    long sended = 0;
    for (long i = 0; i < segmentCount; i++) {
      long sendCount = remain < MAX_STREAM_SUB_SEGMENT ? remain : MAX_STREAM_SUB_SEGMENT;
      input.read(buf, 0, (int) sendCount);
      Long start = i * MAX_STREAM_SUB_SEGMENT;
      Long end = i * MAX_STREAM_SUB_SEGMENT + sendCount - 1;
      String strRange = start.toString();
      strRange += "-";
      strRange += end.toString();
      callbackData.getHeaderData().put("range", strRange);
      // Retry time 3
      int loop = 3;
      while (true) {
        try {
          ossMgr.writeObjectByChunk(bucketName, keyID, contentType, new ByteArrayInputStream(buf), sendCount, offset + sended, listener,
              callbackData, userdata);
        } catch (IOException e) {
          String msg = e.getMessage();
          if (msg == null || msg.compareToIgnoreCase("user abort") != 0) {
            loop--;
            if (loop == 0)
              throw e;
            continue;
          }
          throw e;
        }
        break;
      }
      remain -= sendCount;
      sended += sendCount;
      ProgressData data = (ProgressData) userdata;
      data.completed += sendCount;
    }

  }

  protected Map<String, String> generateScale(int width, int height, Map<String, String> param) {
    if (width == 0 && height == 0)
      throw new IllegalArgumentException();
    if (width == 0)
      param.put(PilotOssConstants.SCALE, String.format("x%d", height));
    else if (height == 0)
      param.put(PilotOssConstants.SCALE, String.format("%d", width));
    else
      param.put(PilotOssConstants.SCALE, String.format("%dx%d", width, height));

    return param;
  }

  protected void notifyStart(Object userData) {
    synchronized (this) {
      if (listener != null) {
        listener.onStart(userData);
      }
    }
  }

  protected void notifyStop(long completed, long total, Object userData, int errCode) {
    synchronized (this) {
      if (listener != null) {
        listener.onStop(completed, total, userData, errCode);
      }
    }
  }

  protected void notifyFinished(long completed, long total, Object userData) {
    synchronized (this) {
      if (listener != null) {
        listener.onFinished(completed, total, userData);
      }
    }
  }

  protected boolean notifyClient(TaskStatus status, long completed, long total, Object userData, int errCode) {
    boolean ret = false;
    synchronized (this) {
      if (listener != null) {
        ret = listener.onProgress(status, completed, total, userData, errCode);
        if (status == TaskStatus.TASK_COMPLETED) {
          notifyFinished(completed, total, userData);
        }
      } else if (completed == total) {
        // complete is equal to total so that the result is true
        ret = true;
      }
    }
    return ret;
  }

  public boolean stopTransfer() throws PilotException {
    synchronized (this) {
      isContinue = false;
    }
    return isContinue;
  }

  public boolean onProgress(long completed, long length, Object userData) {
    ProgressData data = (ProgressData) userData;
    long tmp = (data.completed + completed) * 100 / data.total;
    if (tmp < 100 && completed == length) {
      // If the current progress is on going and the completed data is the
      // same to the data length, the partial progress is end.
      return false;
    }
    if (tmp > data.currentProgress) {
      boolean ret = true;
      ret = notifyClient(TaskStatus.TASK_RUNNING, data.completed + completed, data.total, data.userData, 0);
      data.currentProgress = (tmp == 100) ? 99 : tmp;
      synchronized (this) {
        if (isContinue)
          isContinue = !ret;
      }
    }
    return isContinue;
  }

  public long getProgressInterval() {
    return 10;
  }

}
