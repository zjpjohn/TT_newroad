package com.newroad.fileext.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.newroad.util.apiresult.ApiReturnObjectUtil;
import com.newroad.util.apiresult.ReturnCode;

/**
 * @info Rest Client
 * @author tangzj1
 * @date Sep 13, 2013
 * @version
 */
@Component
public class FileExtendRestClient {

  private static Logger logger = LoggerFactory.getLogger(FileExtendRestClient.class);

//  @Value("${SPACE_PROXY_URL}")
//  private String spaceURL;

  @Value("${ACCOUNT_PROXY_URL}")
  private String accountURL;

  private RestTemplate restTemplate;

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public JSONObject checkAuth(Map<String, String> headers, String app) {
    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("app", app);

    String response = performHttpRequest(accountURL, jsonRequest.toString(), headers);
    JSONObject jsonResponse = JSONObject.fromObject(response);
    Integer returnCode = (Integer) jsonResponse.get(ApiReturnObjectUtil.KEY_RETURN_CODE);
    if (returnCode == ReturnCode.OK.getValue()) {
      return jsonResponse;
    }
    return null;
  }

  public static Map<String, String> getHttpHeaders(JSONObject session) {
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("AuthToken", session.getString("token"));
    return headers;
  }

  private String performHttpRequest(String url, String body, Map<String, String> headerMap) {
    HttpHeaders headers = new HttpHeaders();
    if (headerMap != null) {
      Set<Map.Entry<String, String>> headerSet = headerMap.entrySet();
      Iterator<Entry<String, String>> iter = headerSet.iterator();
      while (iter.hasNext()) {
        Entry<String, String> entry = iter.next();
        headers.set(entry.getKey(), entry.getValue());
      }
    }

    HttpEntity<String> request = new HttpEntity<String>(body, headers);
    String response = restTemplate.postForObject(url, request, String.class);
    return response;
  }

}
