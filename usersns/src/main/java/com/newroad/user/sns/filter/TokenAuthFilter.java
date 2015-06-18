package com.newroad.user.sns.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.newroad.user.sns.model.login.LoginUser;
import com.newroad.user.sns.service.SessionService;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.auth.TokenUtil;

/**
 * @info : 客户端请求时 进行token认证
 * @author: xiangping_yu
 * @data : 2013-6-20
 * @since : 1.5
 */
public class TokenAuthFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(TokenAuthFilter.class);
	/**
	 * 记录当前操作用户信息
	 */
	private static ThreadLocal<LoginUser> current = new ThreadLocal<LoginUser>();
	
	private SessionService service;
	/**
	 * 不需要登录拦截的url请求
	 */
	private static List<String> notUrlList = Collections.emptyList();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		// 不符合规则的请求url
		if (isIgnore(req.getRequestURI())) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = req.getHeader(TokenUtil.AUTH_TOKEN);
		if (StringUtils.isBlank(token)) {
			out(res, getReturn(ReturnCode.NO_TOKEN, "No AuthToken in Http head!"));
			log.warn("No AuthToken in Http head!");
			return;
		}
		
		LoginUser user = service.getLoginUser(token);
		if (user == null) {
			out(res,  getReturn(ReturnCode.ILLEGAL_TOKEN, "The AuthToken is fail or expired!"));
			log.warn("The AuthToken[" + token + "] is fail or expired!");
			return;
		}
		
		// 保存当前线程副本
		current.set(user);
		try {
			chain.doFilter(request, response);
		} finally {
			current.remove();
		}
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext context = filterConfig.getServletContext();
		ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(context);
		service = (SessionService) ac.getBean("sessionService");
		
		String notUrl = filterConfig.getInitParameter("notUrl");
		
		if(!StringUtils.isBlank(notUrl)) {
			String url[] = notUrl.split(",");
			notUrlList = new ArrayList<String>(url.length);
			notUrlList.addAll(Arrays.asList(url));
		}
		log.info("not filter user login request string :"+notUrl);
	}
	
	@Override
	public void destroy() {
		current.remove();
	}
	
	public static LoginUser getCurrent() {
		return current.get();
	}
	
	private void out(HttpServletResponse res, String json) {
		res.setContentType("application/json;charset=UTF-8");
		try {
			PrintWriter out = res.getWriter();
			out.write(json);
			out.close();
		} catch (Exception e) {
			log.error("token filter response print out error!", e);
		}
	}
	
	private String getReturn(ReturnCode code, String msg) {
		JSONObject json = new JSONObject();
		json.put("returnCode", code.getValue());
		json.put("returnMessage", msg);
		return json.toString();
	}
	
	private boolean isIgnore(String uri) {
		if (!CollectionUtils.isEmpty(notUrlList)) {
			if (notUrlList.contains(uri)) 
				return true;
			
			for (String notUrl : notUrlList) {
				if(uri.indexOf(notUrl) != -1)
					return true;
				
				if (uri.matches(notUrl))
					return true;
			}
		}
		return false;
	}
}
