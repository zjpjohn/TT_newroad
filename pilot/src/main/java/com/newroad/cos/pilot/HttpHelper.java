package com.newroad.cos.pilot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cos.pilot.util.PilotOssConstants;

@SuppressWarnings("deprecation")
class HttpHelper {

  private final Logger logger = LoggerFactory.getLogger(PilotOssObjectForLeNote.class);
  // private static final String TAG = "HTTPHELPER";
  public static final int POST_METHOD = 1;
  public static final int GET_METHOD = 2;
  public static final int PUT_METHOD = 3;
  public static final int DELETE_METHOD = 4;
  public static final String NET_ERROR = "http repsonse is null!";

  private DefaultHttpClient client = null;

  public boolean isMultipart = false;

  final static int BUFFER_SIZE = 4096;

  public static final class RequestAndResponse {
    public final HttpRequestBase request;
    public final HttpResponse response;
    public boolean cancel;

    protected RequestAndResponse(HttpRequestBase request, HttpResponse response) {
      this.request = request;
      this.response = response;
    }

    protected RequestAndResponse(HttpRequestBase request, HttpResponse response, boolean flag) {
      this.request = request;
      this.response = response;
      this.cancel = flag;
    }
  }

  public HttpHelper() {}

  public void setMultipart(boolean isMultipart) {
    this.isMultipart = isMultipart;
  }

  public RequestAndResponse performGet(String url, String user, String pwd, Map<String, String> addtionalHeaders) throws HttpAbortException {
    return performRequest(GET_METHOD, url, user, pwd, addtionalHeaders, null, null, 0, null, null);
  }

  public RequestAndResponse performPost(String url, String user, String pwd, Map<String, String> addtionalHeaders,
      Map<String, String> params, InputStream is, long length, OssManagerListener listener, Object userdata) throws HttpAbortException {
    return performRequest(POST_METHOD, url, user, pwd, addtionalHeaders, params, is, length, listener, userdata);
  }

  public RequestAndResponse performPut(String url, String user, String pwd, Map<String, String> addtionalHeaders,
      Map<String, String> params, InputStream is, long length, OssManagerListener listener, Object userdata) throws HttpAbortException {
    return performRequest(PUT_METHOD, url, user, pwd, addtionalHeaders, params, is, length, listener, userdata);
  }

  public RequestAndResponse performDelete(String url, String user, String pwd, Map<String, String> addtionalHeaders,
      Map<String, String> params) throws HttpAbortException {
    return performRequest(DELETE_METHOD, url, user, pwd, addtionalHeaders, params, null, 0, null, null);
  }

  RequestAndResponse performRequest(int method, String url, String user, String pwd, Map<String, String> headers,
      Map<String, String> params, InputStream is, long length, OssManagerListener listener, Object userdata) throws HttpAbortException {

    client = HttpConnectionManager.createHttpClient();
    HttpRequestBase request = null;
    HttpResponse response = null;
    HttpEntity entity = null;
    RequestAndResponse rr = null;
    boolean cancelFlag = false;

    // insert all headers to sendheaders
    final Map<String, String> sendHeaders = new HashMap<String, String>();

    if (headers != null && !headers.isEmpty()) {
      sendHeaders.putAll(headers);
    }

    if (!sendHeaders.isEmpty()) {
      client.addRequestInterceptor(new HttpRequestInterceptor() {
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
          for (String key : sendHeaders.keySet()) {
            if (!request.containsHeader(key)) {
              String value = sendHeaders.get(key);
              request.addHeader(key, value);
            }
          }
        }
      });
    }

    try {
      if (is != null) {
        if (isMultipart) {
          // logger.trace("performRequest Multipart entity ");
          if (listener != null) {
            entity =
                createMultipartEntity(is, new ProgressMultipartHttpEntity(HttpMultipartMode.BROWSER_COMPATIBLE, listener, length, userdata));
          } else {
            entity = createMultipartEntity(is, new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE));
          }
        } else {
          // logger.trace("performRequest InputStream entity ");
          InputStreamEntity isEntity = new InputStreamEntity(is, length);
          entity = isEntity;

          if (listener != null) {
            entity = new ProgressHttpEntity(entity, length, listener, userdata);
          }
        }
      }

      if (POST_METHOD == method || PUT_METHOD == method) {
        // judge which request
        switch (method) {
          case POST_METHOD: {
            HttpPost post = new HttpPost(url);
            if (entity != null) {
              post.setEntity(entity);
            }
            request = post;
            response = client.execute(request);
          }
            break;
          case PUT_METHOD: {
            HttpPut put = new HttpPut(url);
            if (entity != null) {
              put.setEntity(entity);
            }
            request = put;
            // request = addHttpRequestHeader(sendHeaders,request);
            response = client.execute(request);
          }
            break;
          default: {
            logger.error("insert entity methos error!");
            break;
          }
        }
      }

      if (GET_METHOD == method) {
        HttpGet get = new HttpGet(url);
        request = get;
        response = client.execute(request);
      }

      if (DELETE_METHOD == method) {
        HttpDelete del = new HttpDelete(url);
        request = del;
        response = client.execute(request);
      }
      rr = new RequestAndResponse(request, response, cancelFlag);
    } catch (Exception e) {
      request.abort();
      if (entity instanceof ProgressHttpEntity) {
        ProgressHttpEntity et = (ProgressHttpEntity) entity;
        if (et.isAbort()) {
          cancelFlag = true;
          et.setAbort(false);
        }
      }
      if (entity instanceof ProgressMultipartHttpEntity) {
        ProgressMultipartHttpEntity et = (ProgressMultipartHttpEntity) entity;
        if (et.isAbort()) {
          cancelFlag = true;
          et.setAbort(false);
        }
      }
      logger.error("performRequest (" + url + ") user abort HttpAbortException:", e);
      throw new HttpAbortException("performRequest (" + url + ") user abort Exception:", e);
    } finally {
      try {
        if (is != null) {
          is.close();
        }
      } catch (IOException e) {
        logger.error("performRequest closing stream IOException:", e);
      }
    }
    return rr;
  }

  public void closeConnections() {
    client.getConnectionManager().closeIdleConnections(0, TimeUnit.MILLISECONDS);
  }

  private HttpEntity createMultipartEntity(InputStream input, MultipartEntity entity) throws IOException, JSONException {
    // Must use MultipartEntity not MultipartEntityBuilder because the
    // MultipartFormEntity in MultipartEntityBuilder couldn't be extend and
    // support progress.

    String objectListStr = PilotOssHelper.inputStream2String(input);
    JSONObject objectsJson = new JSONObject(objectListStr);
    JSONArray objectArray = objectsJson.getJSONArray(PilotOssConstants.FILE_LIST);

    int fileNum = objectArray.length();
    for (int i = 0; i < fileNum; i++) {
      JSONObject obj = (JSONObject) objectArray.get(i);
      File file = new File(obj.getString(PilotOssConstants.TEMP_FILE_PATH));
      String key = obj.getString(PilotOssConstants.OBJECT_KEY);
      String mimeType = obj.getString(PilotOssConstants.CONTENT_TYPE);
      // entity.addPart(
      // key,
      // new FileBody(((File) file), ContentType.create(mimeType),
      // "UTF-8"));
      entity.addPart(key, new FileBody(((File) file), file.getName(), mimeType, "UTF-8"));
    }
    return entity;
  }

}
