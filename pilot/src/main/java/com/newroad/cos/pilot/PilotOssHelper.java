package com.newroad.cos.pilot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PilotOssHelper {

  private PilotOssHelper() {

  }

  public static byte[] inputStream2Byte(InputStream in, long length) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[2048];
      int len = -1;
      long readed = 0;
      while (length > readed) {
        if ((len = in.read(buffer)) > 0) {
          if ((readed + len) > length) {
            len = (int) (length - readed);
          }
          bos.write(buffer, 0, len);
        }
        readed += len;
      }
      return bos.toByteArray();
    } finally {
      bos.close();
    }
  }

  public static byte[] inputStream2Byte(InputStream is) throws IOException {
    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
    try {
      byte[] buffer = new byte[1024];// 1k bytes buffer
      int size = is.read(buffer, 0, 1024);

      while (size > 0) {
        bytestream.write(buffer, 0, size);
        size = is.read(buffer, 0, 1024);
      }
      return bytestream.toByteArray();
    } finally {
      is.close();
      bytestream.close();
    }
  }

  public static String inputStream2String(InputStream is) throws IOException {
    ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
    String imgdata = null;
    try {
      int ch;
      while ((ch = is.read()) != -1) {
        bytestream.write(ch);
      }
      imgdata = bytestream.toString();
    } finally {
      is.close();
      bytestream.close();
    }
    return imgdata;
  }

  public static String paramToString(Map<String, String> queryParam) throws IOException {
    String encodedStr = null;
    String encodedValue = null;
    Map.Entry<String, String> entry = null;
    String key = null;
    String value = null;
    StringBuilder bf = null;
    int index = 0;
    bf = new StringBuilder();

    // add by houwei
    if (queryParam == null)
      return null;

    Iterator<Entry<String, String>> iter = queryParam.entrySet().iterator();

    while (iter.hasNext()) {
      entry = (Map.Entry<String, String>) iter.next();
      key = entry.getKey();
      value = entry.getValue();
      encodedValue = URLEncoder.encode(value, "utf-8");
      bf.append(key);
      bf.append("=");
      bf.append(encodedValue);
      bf.append('&');
    }
    index = bf.length();
    if (index > 0) {
      bf.deleteCharAt(index - 1);
      encodedStr = bf.toString();
    }
    return encodedStr;
  }

  public static List<JSONObject> jsonParseArray(String json) {
    JSONArray jsonarray = null;
    // String url = null;
    List<JSONObject> oiditems = new ArrayList<JSONObject>();
    try {
      jsonarray = new JSONArray(json);
      int count = jsonarray.length();
      for (int i = 0; i < count; i++) {
        final JSONObject item = jsonarray.getJSONObject(i);
        // final String bucketname = item.getString("bucket_name");
        // final String key = item.getString("key");
        // url = buildObjUrl(bucketname, key, null);
        // if (url != null) {
        // oiditems.add(url);
        // }
        oiditems.add(item);
      }
    } catch (JSONException jse) {
      jse.printStackTrace();
    }
    return oiditems;
  }

  public static String insertContents(String content, String indexTag, String input) {
    StringBuilder bf = null;
    int index = 0;
    index = content.indexOf(indexTag);
    bf = new StringBuilder();
    bf.append(content.substring(0, index + indexTag.length()));
    bf.append(input);
    bf.append(content.substring(index + indexTag.length()));
    return bf.toString();
  }

  public static String buildBucketUrl(String bucketName) {
    return OssManagerBase.URL_BASE + "/bucket/" + bucketName;
  }

  public static String buildBucketUrl(String bucketName, String actionType) {
    String URL = null;
    if (actionType == null) {
      URL = OssManagerBase.URL_BASE + "/bucket/" + bucketName;
    } else {
      URL = OssManagerBase.URL_BASE + "/bucket/" + actionType + "/" + bucketName;
    }
    return URL;
  }

  public static String buildObjUrl(String bucketName, String actionType) {
    String URL = null;
    if (actionType == null) {
      URL = OssManagerBase.URL_BASE + "/object/" + bucketName;
    } else {
      URL = OssManagerBase.URL_BASE + "/object/" + actionType + "/" + bucketName;
    }
    return URL;
  }

  public static String buildObjUrl(String bucketName, String actionType, String keyName) {
    String URL = buildObjUrl(bucketName, actionType);
    return URL + "/" + keyName;
  }

  public static String buildObjIOUrl(String bucketName, String actionType) {
    String URL = null;
    if (actionType == null) {
      URL = OssManagerBase.URL_IO_BASE + "/object/" + bucketName;
    } else {
      URL = OssManagerBase.URL_IO_BASE + "/object/" + actionType + "/" + bucketName;
    }
    return URL;
  }

  public static String buildObjIOUrl(String bucketName, String actionType, String keyName) {
    String URL = buildObjIOUrl(bucketName, actionType);
    return URL + "/" + keyName;
  }

}
