package com.newroad.cos.pilot;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cos.pilot.HttpHelper.RequestAndResponse;
import com.newroad.cos.pilot.util.AuthenUtil;

/**
 * @info
 * @author tangzj1
 * @date Nov 4, 2013
 * @version
 */
public class OssManagerBase {

  private final Logger logger = LoggerFactory.getLogger(OssManagerBase.class);

  protected static Properties propertie;
  protected static String URL_BASE;
  protected static String URL_IO_BASE;
  protected static String CONNECTOR_URL;
  protected static String SESSION_URL;

  protected static final String NET_ERROR = "http repsonse is null!";

  protected String connectorId = null;
  protected String sessionId = null;

  protected String token = null;
  protected String developerKid = null;
  protected String developerKey = null;

  static {
    propertie = new Properties();
    try {
      String filePath = "cos.properties";
      propertie.load(OssManagerBase.class.getClassLoader().getResourceAsStream(filePath));
      URL_BASE = propertie.getProperty("URL_BASE");
      URL_IO_BASE = propertie.getProperty("URL_IO_BASE");
      CONNECTOR_URL = propertie.getProperty("CONNECTOR_URL");
      SESSION_URL = propertie.getProperty("SESSION_URL");
    } catch (FileNotFoundException ex) {
      LoggerFactory.getLogger(OssManagerBase.class).error("Get cos properties file FileNotFoundException:", ex);
    } catch (IOException ex) {
      LoggerFactory.getLogger(OssManagerBase.class).error("Get cos properties file IOException:", ex);
    }
  }

  public static void setBaseUrl(String baseUrl, String connectorUrl) {
    URL_BASE = baseUrl + "/v2";
    CONNECTOR_URL = connectorUrl + "/v1/app/connector";
    SESSION_URL = connectorUrl + "/v1/app/session";
  }

  // public void setRealms(String baseUrl, String baseIOUrl,
  // String connectorUrl, String sessionUrl) {
  // URL_BASE = baseUrl;
  // URL_IO_BASE = baseIOUrl;
  // CONNECTOR_URL = connectorUrl;
  // SESSION_URL = sessionUrl;
  // }

  public String loginByNormal(String userName, String passWord, String kid, String key, String appId, String spec, String workspace)
      throws IOException {
    return pilotByNormal(userName, passWord, kid, key, appId, spec, workspace);
  }

  public String loginByLenovoID(String realm, String lenovoST, String kid, String key, String appId, String spec, String workspace)
      throws IOException {
    return pilotByLenovoID(realm, lenovoST, kid, key, appId, spec, workspace);
  }

  public String loginByConnector(String devID, String devKey, String connector) throws IOException {
    String newsession = null;
    if (devID == null) {
      throw new PilotException("loginByConnector: devID is null!");
    }
    if (devKey == null) {
      throw new PilotException("loginByConnector: devKey is null!");
    }
    if (connector == null) {
      throw new PilotException("loginByConnector: connector is null!");
    }
    developerKid = devID;
    developerKey = devKey;
    synchronized (this) {
      connectorId = connector;
      newsession = getSession(developerKid, developerKey, connectorId);
      sessionId = newsession;
      logger.debug("COS loginByConnector connector:" + connectorId + ",sessionId:" + sessionId);
      return sessionId;
    }
  }

  public String generateToken(String appID, String devID, String devKey, String userSlug, long expiration) throws IOException {
    if (appID == null) {
      throw new IOException("generateToken: appID is null");
    }
    if (devID == null) {
      throw new IOException("generateToken: devID is null");
    }
    if (devKey == null) {
      throw new IOException("generateToken: devKey is null");
    }
    if (userSlug == null) {
      throw new IOException("generateToken: userSlug is null");
    }
    synchronized (this) {
      this.token = generateKeyToken(appID, devID, devKey, userSlug, expiration);
      return token;
    }
  }

  public void useToken(String token) throws PilotException {
    if (token == null) {
      throw new PilotException("useToken: token is null!");
    }
    synchronized (this) {
      this.token = token;
    }
  }

  protected String getAuthorization() {
    synchronized (this) {
      if (token != null) {
        return token;
      } else if (sessionId != null) {
        return sessionId;
      } else {
        throw new AuthorizationException("getAuthorization: fail to get authorization using token!");
      }
    }
  }

  private String generateKeyToken(String appID, String devID, String devKey, String userSlug, long expirationEpoch) {
    byte[] RC4_KEY = devKey.getBytes(AuthenUtil.utf8);
    String keyToken = "";
    String targetJson = null;
    JSONObject json = new JSONObject();
    try {
      json.put("user_slug", "lenovo:" + userSlug);
      json.put("app_id", appID);
      json.put("expiration", expirationEpoch);
      targetJson = json.toString();
      keyToken = URLEncoder.encode(AuthenUtil.rc4(RC4_KEY, targetJson.getBytes(AuthenUtil.utf8)), "utf-8");

      return "lws_token " + devID + ":" + keyToken;
    } catch (UnsupportedEncodingException e) {
      logger.error("generateKeyToken UnsupportedEncodingException", e);
    } catch (JSONException e) {
      logger.error("generateKeyToken JSONException", e);
    }
    return null;
  }

  public String getConnector() {
    return connectorId;
  }

  public String getDevKeyId() {
    return developerKid;
  }

  private String pilotByLenovoID(String realm, String lenovoST, String kid, String key, String appId, String spec, String workspace)
      throws IOException {
    String connector = null;
    String session;
    JSONObject object;
    String toString = null;
    String credential = null;
    String url = CONNECTOR_URL;

    Map<String, String> sendHeaders = new HashMap<String, String>();

    developerKid = kid;
    developerKey = key;
    credential = "lenovo_id " + realm + ":" + lenovoST;
    try {
      object = new JSONObject();
      object.put("developer_kid", kid);
      object.put("developer_key", key);
      object.put("app_id", appId);
      object.put("app_spec_shape", spec);
      object.put("user_credential", credential);
      object.put("workspace", workspace);
      toString = object.toString();

    } catch (JSONException jse) {
      logger.error("pilotByLenovoID LenovoID info JSONException", jse);
    } catch (Exception e){
      throw new PilotException("pilotByLenovoID LenovoID info Exception", e);
    }

    HttpHelper hhp = new HttpHelper();
    byte[] params = toString.getBytes();
    RequestAndResponse rr = null;
    rr = hhp.performPost(url, null, null, sendHeaders, null, new ByteArrayInputStream(params), params.length, null, null);
    byte[] body = handlerResponseBody(hhp, rr);
    if (body == null) {
      throw new IOException("pilotByLenovoID: http body is null!");
    }

    String json = new String(body);
    try {
      synchronized (this) {
        JSONObject jsonObject = new JSONObject(json);
        connector = jsonObject.getString("connector");
        connectorId = connector;
        session = jsonObject.getString("session");
        sessionId = session;
        logger.debug("COS pilotByLenovoID connector:" + connectorId + ",sessionId:" + sessionId);
        return connectorId;
      }
    } catch (JSONException jse) {
      logger.error("pilotByLenovoID getSession JSONException", jse);
    }
    return null;
  }

  private String pilotByNormal(String userName, String passWord, String kid, String key, String appId, String spec, String workspace)
      throws IOException {
    String connector = null;
    String session;
    JSONObject object;
    String toString = null;
    String credential = null;
    String url = CONNECTOR_URL;

    Map<String, String> sendHeaders = new HashMap<String, String>();

    developerKid = kid;
    developerKey = key;
    credential = null;
    credential = "basic " + userName + ":" + passWord;
    try {
      object = new JSONObject();
      object.put("developer_kid", kid);
      object.put("developer_key", key);
      // object.put("kid", kid);
      // object.put("key", key);
      object.put("app_id", appId);
      object.put("app_spec_shape", spec);
      object.put("user_credential", credential);
      object.put("workspace", workspace);
      toString = object.toString();

    } catch (JSONException jse) {
      logger.error("pilotByNormal LenovoID user info JSONException", jse);
    } catch (Exception e){
      throw new PilotException("pilotByNormal LenovoID user info Exception", e);
    }

    HttpHelper hhp = new HttpHelper();
    byte[] params = toString.getBytes();
    RequestAndResponse rr = null;
    rr = hhp.performPost(url, null, null, sendHeaders, null, new ByteArrayInputStream(params), params.length, null, null);
    byte[] body = handlerResponseBody(hhp, rr);
    if (body == null) {
      throw new IOException("pilotByNormal: http body is null!");
    }

    String json = new String(body);
    try {
      synchronized (this) {
        JSONObject jsonObject = new JSONObject(json);
        connector = jsonObject.getString("connector");
        connectorId = connector;
        session = jsonObject.getString("session");
        sessionId = session;
        logger.debug("COS pilotByNormal connector:" + connectorId + ",sessionId:" + sessionId);
        return connectorId;
      }
    } catch (JSONException jse) {
      logger.error("pilotByNormal getSession JSONException", jse);
    }
    return null;
  }

  protected String getSession(String kid, String key, String connector) throws IOException {
    String session = null;
    JSONObject object;
    String toString = null;
    String url = SESSION_URL;

    Map<String, String> sendHeaders = new HashMap<String, String>();
    try {
      object = new JSONObject();
      object.put("developer_kid", kid);
      object.put("developer_key", key);
      // object.put("kid", kid);
      // object.put("key", key);

      object.put("connector", connector);
      toString = object.toString();
    } catch (Exception e) {
      logger.error("getSession get connector info Exception for connector " + connector + ":", e);
      throw new PilotException("getSession get connector info Exception for connector " + connector + ":", e);
    }

    HttpHelper hhp = new HttpHelper();
    byte[] params = toString.getBytes();
    RequestAndResponse rr = null;
    rr = hhp.performPost(url, null, null, sendHeaders, null, new ByteArrayInputStream(params), params.length, null, null);
    try {
      checkResponseStatus(rr);
      Header header = rr.response.getFirstHeader("Location");
      if (header != null) {
        session = header.getValue();
      }
      logger.debug("COS getSession SessionID:" + session);
    } finally {
      hhp.closeConnections();
    }
    return session;
  }

  protected String assembleUrl(String objectUrl, Map<String, String> queryParam) throws IOException {
    String url = null;
    String queryString = null;
    if (queryParam != null && !queryParam.isEmpty()) {
      queryString = PilotOssHelper.paramToString(queryParam);
      url = objectUrl + "?" + queryString;
    } else {
      url = objectUrl;
    }
    return url;
  }

  protected RequestAndResponse checkResponseStatus(RequestAndResponse rr) throws IOException {
    if (rr == null || rr.response == null) {
      throw new IOException(NET_ERROR);
    }
    StatusLine status = rr.response.getStatusLine();
    if (status.getStatusCode() / 100 != 2) {
      throw new IOException("checkResponseStatus http status:" + Integer.toString(status.getStatusCode()));
    }
    return rr;
  }

  protected RequestAndResponse retryCheckResponseStatus(int method, HttpHelper hhp, RequestAndResponse rr, String url,
      Map<String, String> sendHeaders, InputStream input, long size, OssManagerListener listener, Object progressData) throws IOException {
    if (rr == null || rr.response == null) {
      throw new IOException(NET_ERROR);
    }

    StatusLine status = rr.response.getStatusLine();
    // Couldn't make sure the connectorId is correct in multi-thread env.
    // if (status.getStatusCode() == 401) {
    // String newsession = getSession(developerKid, developerKey,
    // connectorId);
    // if (newsession == null) {
    // throw new IOException(Integer.toString(status.getStatusCode()));
    // }
    // // throw new SessionEXPException("putObjectByChunk");
    // sendHeaders.put("X-Lenovows-Authorization", newsession);
    // rr = hhp.performRequest(method, url, null, null, sendHeaders, null,
    // input, size, listener, new ProgressData(size, null));
    // }
    if (status.getStatusCode() == 301 || status.getStatusCode() == 302) {
      Header header = rr.response.getFirstHeader("Location");
      if (header == null) {
        throw new IOException("retryCheckResponseStatus http status:" + Integer.toString(status.getStatusCode()));
      }
      String locationheader = header.getValue();
      rr = hhp.performRequest(method, locationheader, null, null, sendHeaders, null, input, size, listener, new ProgressData(size, null));
    }

    if (rr == null || rr.response == null) {
      throw new IOException("retryCheckResponseStatus: " + NET_ERROR);
    }
    status = rr.response.getStatusLine();
    if ((status.getStatusCode() / 100) != 2) {
      throw new IOException("retryCheckResponseStatus http status:" + Integer.toString(status.getStatusCode()));
    }
    return rr;
  }

  protected RequestAndResponse retryCancelResponseStatus(int method, HttpHelper hhp, RequestAndResponse rr, String url,
      Map<String, String> sendHeaders, InputStream input, long size, OssManagerListener listener, Object progressData) throws IOException {
    if (rr == null || rr.response == null) {
      throw new IOException(NET_ERROR);
    }
    boolean cancelFlag = rr.cancel;
    if (cancelFlag) {
      throw new IOException("retryCancelResponseStatus: user abort, cancelFlag" + cancelFlag);
    }

    StatusLine status = rr.response.getStatusLine();
    // Couldn't make sure the connectorId is correct in multi-thread env.
    // if (status.getStatusCode() == 401) {
    // String newsession = getSession(developerKid, developerKey,
    // connectorId);
    // if (newsession == null) {
    // throw new IOException(Integer.toString(status.getStatusCode()));
    // }
    // // throw new SessionEXPException("putObjectByChunk");
    // input.reset();
    // assert (input.markSupported());
    // input.mark((int) size);
    // sendHeaders.put("X-Lenovows-Authorization", newsession);
    // rr = hhp.performRequest(method, url, null, null, sendHeaders, null,
    // input, size, listener, new ProgressData(size, null));
    // }
    if (status.getStatusCode() == 301 || status.getStatusCode() == 302) {
      Header header = rr.response.getFirstHeader("Location");
      if (header == null) {
        throw new IOException("retryCancelResponseStatus http status:" + Integer.toString(status.getStatusCode()));
      }
      String locationheader = header.getValue();
      input.reset();
      rr = hhp.performRequest(method, locationheader, null, null, sendHeaders, null, input, size, listener, new ProgressData(size, null));
    }

    if (rr == null || rr.response == null) {
      throw new IOException(NET_ERROR);
    }
    cancelFlag = rr.cancel;
    if (cancelFlag) {
      throw new IOException("retryCancelResponseStatus: user abort, cancelFlag" + cancelFlag);
    }

    status = rr.response.getStatusLine();
    if ((status.getStatusCode() / 100) != 2) {
      throw new IOException("retryCancelResponseStatus http status:" + Integer.toString(status.getStatusCode()));
    }
    return rr;
  }

  protected byte[] handlerResponseBody(HttpHelper hhp, RequestAndResponse rr) throws IOException {
    byte[] body = null;
    HttpEntity entity = null;
    InputStream is = null;
    try {
      rr = checkResponseStatus(rr);
      entity = rr.response.getEntity();
      if (entity != null) {
        is = entity.getContent();
        body = PilotOssHelper.inputStream2Byte(is);
      }
      // need to release connection in web env.
      rr.request.releaseConnection();
    } catch (IOException ioe) {
      if (rr != null)
        rr.request.abort();
      throw ioe;
    } finally {
      if (entity != null) {
        EntityUtils.consume(entity);
      }
      if (is != null) {
        is.close();
      }
      hhp.closeConnections();
    }
    return body;
  }

  protected byte[] handlerResponseBody(HttpHelper hhp, RequestAndResponse rr, int method, String url, Map<String, String> sendHeaders,
      InputStream input, long size, OssManagerListener listener, Object progressData) throws IOException {
    byte[] body = null;
    HttpEntity entity = null;
    InputStream is = null;
    try {
      rr = retryCheckResponseStatus(method, hhp, rr, url, sendHeaders, input, size, listener, progressData);
      entity = rr.response.getEntity();
      if (entity != null) {
        is = entity.getContent();
        body = PilotOssHelper.inputStream2Byte(is);
      }
      // need to release connection in web env.
      rr.request.releaseConnection();
    } catch (IOException ioe) {
      if (rr != null)
        rr.request.abort();
      throw ioe;
    } finally {
      if (entity != null) {
        EntityUtils.consume(entity);
      }
      if (is != null) {
        is.close();
      }
      hhp.closeConnections();
    }
    return body;
  }

}
