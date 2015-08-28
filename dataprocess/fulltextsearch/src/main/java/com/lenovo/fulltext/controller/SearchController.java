package com.lenovo.fulltext.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lenovo.fulltext.filter.TokenAuthFilter;
import com.lenovo.fulltext.service.SearchServiceIf;
import com.lenovo.fulltext.util.FullTextSearchConstants;
import com.lenovo.fulltext.util.FullTextSearchException;
import com.lenovo.tool.StringHelper;
import com.lenovo.tool.apiresult.ApiReturnObjectUtil;


/**
 * @info Search Rest Controller
 * @author tangzj1
 * @date Aug 26, 2013
 * @version
 */
@Controller
@RequestMapping("/v{apiVersion}")
public class SearchController {

  private static Logger logger = LoggerFactory.getLogger(SearchController.class);

  @Autowired
  private SearchServiceIf searchService;

  /**
   * Search
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/search")
  public @ResponseBody
  String search(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    // Must set the correct CharacterEncoding before getting request parameters.
    request.setCharacterEncoding("UTF-8");
    try {
      String query = request.getParameter(FullTextSearchConstants.QUERY);
      if (query == null) {
        throw new FullTextSearchException("Search query keyword is null!");
      }
      // Need to set Encoding in tomcat config file manually.
      String encoding = StringHelper.getEncoding(query);
      logger.debug("search info encoding:" + encoding);
      if (!"UTF-8".equals(encoding)) {
        query = StringHelper.parse2UTF(query);
      }
      logger.info("Search info:" + query);

      String userId = "";
      JSONObject sessionUser = TokenAuthFilter.getCurrent() == null ? null : TokenAuthFilter.getCurrent();
      if (sessionUser != null) {
        userId = (String) sessionUser.get("uid");
      } else {
        throw new FullTextSearchException("Search user info is null!");
      }

      JSONObject param = StringHelper.getRequestEntity(request);
      Integer startIndex = (Integer) param.get("start");
      Integer size = (Integer) param.get("size");
      return searchService.search(userId, query, startIndex, size).toString();
    } catch (Exception e) {
      logger.error("Search Exception:", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }
}
