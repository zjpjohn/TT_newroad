package com.newroad.cos.pilot;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

/**
 * @author tangzj1
 * 
 */
@SuppressWarnings("deprecation")
public class HttpConnectionManager {
  /**
   * 连接池里的最大连接数
   */
  public static final int MAX_TOTAL_CONNECTIONS = 100;

  /**
   * 每个路由的默认最大连接数
   */
  public static final int MAX_ROUTE_CONNECTIONS = 50;

  /**
   * 连接超时时间
   */
  public static final int CONNECT_TIMEOUT = 60 * 1000;

  /**
   * 套接字超时时间
   */
  public static final int SOCKET_TIMEOUT = 60 * 1000;

  /**
   * 连接池中 连接请求执行被阻塞的超时时间
   */
  // public static final long CONN_MANAGER_TIMEOUT = 60 * 1000;

  public static final int RETRY_TIME = 3;

  /**
   * http连接相关参数
   */
  private static HttpParams parentParams;
  //
  // private static PoolingClientConnectionManager cm;
  //
  // private static DefaultHttpClient httpClient;
  /**
   * 默认目标主机
   */
  // private static final HttpHost DEFAULT_TARGETHOST = new
  // HttpHost("http://www.qq.com", 80);

  private static final String USER_AGENT = "lenovo.com.pilot";

  /**
   * 初始化http连接池，设置参数、http头等等信息
   */
  static {

    parentParams = new BasicHttpParams();
    parentParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

    parentParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

    // parentParams.setLongParameter(ClientPNames.CONN_MANAGER_TIMEOUT,
    // CONN_MANAGER_TIMEOUT);
    parentParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
    parentParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);

    parentParams.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
    parentParams.setParameter(ClientPNames.HANDLE_REDIRECTS, true);

    parentParams.setParameter(HttpProtocolParams.HTTP_CONTENT_CHARSET, "UTF-8");
    parentParams.setParameter(HttpProtocolParams.USER_AGENT, USER_AGENT);
    parentParams.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, true);
  }

  private HttpConnectionManager() {

  }

  public static DefaultHttpClient createHttpClient() {
    // 请求重试处理
    HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
      @Override
      public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        if (executionCount >= RETRY_TIME) {
          // 如果超过最大重试次数，那么就不要继续了
          return false;
        }
        if (exception instanceof NoHttpResponseException) {
          // 如果服务器丢掉了连接，那么就重试
          return true;
        }
        if (exception instanceof SSLHandshakeException) {
          // 不要重试SSL握手异常
          return false;
        }
        HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
          // 如果请求被认为是幂等的，那么就重试
          return true;
        }
        return false;
      }
    };
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
    schemeRegistry.register(new Scheme("https", 443, newSslSocketFactory()));

    // Create a new PoolingClientConnectionManager for one HttpClient
    PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry, CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
    cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
    // if (targetHost != null) {
    // cm.setMaxPerRoute(new HttpRoute(targetHost), 20); // 设置对目标主机的最大连接数
    // }

    DefaultHttpClient httpClient = new DefaultHttpClient(cm, parentParams);
    httpClient.setHttpRequestRetryHandler(httpRequestRetryHandler);
    return httpClient;
  }

  // public static void shutdown() {
  // if (cm != null) {
  // cm.shutdown();
  // }
  // }

  private static SSLSocketFactory newSslSocketFactory() {
    SSLSocketFactory sf = null;
    try {
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(null, new X509TrustManager[] {new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

        public X509Certificate[] getAcceptedIssuers() {
          return new X509Certificate[0];
        }
      }}, new SecureRandom());
      // Pass the keystore to the SSLSocketFactory. The factory is
      // responsible
      // for the verification of the server certificate.
      sf = new SSLSocketFactory(context, SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
      // Hostname verification from certificate
      // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
      // sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
      return sf;
    } catch (NoSuchAlgorithmException noale) {
    } catch (Exception e) {
      // throw new AssertionError(e);
    }
    return sf;
  }
}
