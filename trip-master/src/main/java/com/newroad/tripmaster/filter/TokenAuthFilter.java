package com.newroad.tripmaster.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.newroad.tripmaster.constant.JSONConvertor;
import com.newroad.tripmaster.dao.pojo.SimpleUser;
import com.newroad.tripmaster.service.client.TripMasterHttpClient;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.auth.TokenUtil;

/**
 * @info :
 * @author: tangzj1
 * @data : 2013-9-11
 * @since : 1.5
 */
public class TokenAuthFilter implements Filter {

  private final static Logger logger = LoggerFactory.getLogger(TokenAuthFilter.class);
  /**
   * ThreadLocal points to the current thread
   */
  private static ThreadLocal<JSONObject> current = new ThreadLocal<JSONObject>();

  private TripMasterHttpClient restClient;

  /**
   * URL
   */
  // private String urlRegx;

  private List<String> checkUrlList;

  private String orderPayStatus = "/order/paystatus";


  public void init(FilterConfig filterConfig) throws ServletException {
    // urlRegx = filterConfig.getInitParameter("urlRegx");

    ServletContext context = filterConfig.getServletContext();
    ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(context);
    restClient = (TripMasterHttpClient) ac.getBean("restClient");

    String checkUrl = filterConfig.getInitParameter("url");

    if (!StringUtils.isBlank(checkUrl)) {
      String url[] = checkUrl.split(",");
      checkUrlList = new ArrayList<String>(url.length);
      checkUrlList.addAll(Arrays.asList(url));
    }
    logger.info("not filter user login request string :" + checkUrl);
  }

  private boolean isLocalIP(String ip) {

    boolean result = false;
    if (ip.indexOf("localhost") >= 0) {
      result = true;
    } else if (ip.indexOf("127.0.0.1") >= 0) {
      result = true;
    }
    return result;
  }

  private boolean ignorePayOrderCheck(String url, String ip) {
    if (url.indexOf(orderPayStatus) >= 0 && isLocalIP(ip)) {
      logger.info("Order pay status uri " + url + " is approved to access!");
      return true;
    }
    return false;
  }

  private boolean needCheck(String uri) {
    if (!CollectionUtils.isEmpty(checkUrlList)) {
      // Temp ignore order pay status request check
      if (checkUrlList.contains(uri)) {
        return true;
      }

      for (String checkUrl : checkUrlList) {
        if (uri.indexOf(checkUrl) != -1)
          return true;

        if (uri.matches(checkUrl))
          return true;
      }
    }
    return false;
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String url = req.getRequestURI();
    String remoteIP = req.getRemoteAddr();

    String token = req.getHeader(TokenUtil.TOKEN);
    // String uid = req.getHeader(FullTextSearchConstants.USERID);
    if (StringUtils.isBlank(token))
      token = req.getParameter(TokenUtil.TOKEN);

    if (StringUtils.isBlank(token)) {
      Cookie[] cookies = ((HttpServletRequest) request).getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if ("token".equals(cookie.getName())) {
            token = URLDecoder.decode(cookie.getValue(), "UTF-8");
            token = token.replace("\"", "");
          }
        }
      }
    }

    // Need to change app type according to client request.
    String app = "trip-master";
    boolean tokenRequired = true;
    if (!needCheck(url) || ignorePayOrderCheck(url, remoteIP)) {
      tokenRequired = false;
    }
    logger.info("app=" + app + ",token=" + token + ",tokenRequired=" + tokenRequired);

    if (tokenRequired && StringUtils.isBlank(token)) {
      out(res, getReturn(ReturnCode.UNAUTHORIZED, "No AuthToken in Http head!"));
      logger.warn("No AuthToken in Http head!");
      return;
    }

    JSONObject session = restClient.checkAuth(token, app);
    JSONObject sessionData=null;
    if (session != null) {
      sessionData = session.getJSONObject("data");
      current.set(sessionData);
      //check the user whether is allowed to access the specific url.
      filterUserAuthority(sessionData, url, res);
    } else if (tokenRequired && session == null) {
      out(res, getReturn(ReturnCode.UNAUTHORIZED, "The AuthToken is fail or expired!"));
      logger.warn("The AuthToken[" + token + "] is fail or expired!");
      return;
    }
   
    try {
      chain.doFilter(request, response);
    } finally {
      current.remove();
    }
  }

  void out(HttpServletResponse res, String json) {
    res.setContentType("application/json;charset=UTF-8");
    try {
      PrintWriter out = res.getWriter();
      out.write(json);
      out.close();
    } catch (Exception e) {
      logger.error("token filter response print out error!", e);
    }
  }

  public void destroy() {}

  public static JSONObject getCurrent() {
    JSONObject session = current.get();
    if (session == null)
      return null;
    return session;
  }

  private String getReturn(ReturnCode code, String msg) {
    JSONObject json = new JSONObject();
    json.put("code", code.getValue());
    json.put("message", msg);
    return json.toString();
  }

  public static SimpleUser getCurrentUser() {
    // Long userId = null;
    JSONObject session = getCurrent();
    SimpleUser user = null;
    if (session != null) {
      user = JSONConvertor.getJSONInstance().fromJson(session.toString(), SimpleUser.class);
    }
    return user;
  }

  public void filterUserAuthority(JSONObject sessionData, String url, HttpServletResponse res) {
    Integer userRole = (Integer) sessionData.get("userRole");
    String token = (String) sessionData.get("token");
    if (!checkUserAuthority(url, userRole)) {
      out(res, getReturn(ReturnCode.UNAUTHORIZED, "The User token hasn't access authority!"));
      logger.error("The User token[" + token + "] hasn't access authority!");
      return;
    }
  }

  private Boolean checkUserAuthority(String uri, Integer userRole) {
    if (uri.contains("/design")) {
      if (userRole != 1 && userRole != 2) {
        logger.error("Fail to access because of unauthorization!");
        return false;
      }
    } else if (uri.contains("/admin")) {
      if (userRole != 1) {
        logger.error("Fail to access because of unauthorization!");
        return false;
      }
    }
    return true;
  }
}
