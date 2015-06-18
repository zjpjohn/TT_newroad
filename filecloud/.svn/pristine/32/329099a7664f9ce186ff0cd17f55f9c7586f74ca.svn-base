package com.newroad.fileext.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.newroad.fileext.service.FileExtendRestClient;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.util.auth.TokenUtil;


/**
 * @info : 客户端请求时 fileext进行token认证
 * @author: tangzj1
 * @data : 2013-8-12
 * @since : 1.5
 */
public class TokenAuthFilter implements Filter {

  private final Logger logger = LoggerFactory.getLogger(TokenAuthFilter.class);
  /**
   * ThreadLocal points to the current thread
   */
  private static ThreadLocal<JSONObject> current = new ThreadLocal<JSONObject>();

  private FileExtendRestClient restClient;

  /**
   * URL 匹配
   */
  private String urlRegx;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    urlRegx = filterConfig.getInitParameter("urlRegx");

    ServletContext context = filterConfig.getServletContext();
    ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(context);
    // service = (SessionTokenService) ac.getBean("tokenService");
    restClient = (FileExtendRestClient) ac.getBean("restClient");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String url = req.getRequestURI();

    // SPLogger.setCurrentRequest(new RequestInfo(req.getServerPort(),
    // req.getRequestURL().toString()));

    // 不符合规则的请求url
    if (!url.matches(urlRegx)) {
      chain.doFilter(request, response);
      return;
    }

    String token = checkAuthTokenExist(req);
    if (StringUtils.isBlank(token)) {
      out(res, "{code:401, message:'No AuthToken in Http head!'}");
      logger.error("No AuthToken in Http (URI:" + req.getRequestURI() + ") head!");
      return;
    }

    // Need to change app type according to client request.
    JSONObject session = checkAuthByToken(token);
    if (session == null) {
      out(res, "{code:403, message:'The AuthToken is fail or expired!'}");
      logger.error("The AuthToken[" + token + "] is fail or expired!");
      return;
    }
    logger.info("Session User:" + session);

    current.set(session);
    try {
      chain.doFilter(request, response);
    } finally {
      current.remove();
    }
  }

  private String checkAuthTokenExist(HttpServletRequest req) {
    String token = req.getHeader(FileDataConstant.AUTH_TOKEN);

    // check authtoken in request header from COS callback request
    if (StringUtils.isBlank(token)) {
      token = req.getHeader(FileDataConstant.AUTH_TOKEN.toLowerCase());
    }

    // send authToken in Cookie in order to download picture using link.
    if (StringUtils.isBlank(token)) {
      Cookie[] cookies = req.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if ("token".equals(cookie.getName())) {
            try {
              token = URLDecoder.decode(cookie.getValue(), "UTF-8");
              token = token.replace("\"", "");
            } catch (UnsupportedEncodingException e) {
              logger.error("get token from cookie UnsupportedEncodingException:", e);
            }
          }
        }
      }
    }

    // send authToken in http get parameter in order to play flash audio online.
    if (StringUtils.isBlank(token)) {
      token = req.getParameter(FileDataConstant.AUTH_TOKEN);
    }
    return token;
  }

  private JSONObject checkAuthByToken(String token) {
    String app = "filecloud";
    Map<String, String> headers = new HashMap<String, String>(1);
    headers.put(TokenUtil.AUTH_TOKEN, token);
    return restClient.checkAuth(headers, app);
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

  @Override
  public void destroy() {}

  public static JSONObject getCurrent() {
    JSONObject session = current.get();
    if (session == null)
      return null;
    return session;
  }
}
