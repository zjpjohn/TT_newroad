package com.newroad.tripmaster.service.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.newroad.util.apiresult.ApiReturnObjectUtil;
import com.newroad.util.apiresult.ReturnCode;
import com.newroad.util.auth.TokenUtil;

public class TripMasterHttpClient {

  @Value("${USERSNS_AUTHTOKEN_URL}")
  private String accountURL;

  private RestTemplate restTemplate;

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public JSONObject checkAuth(String token, String app) {
    // Need to check auth based on app type
    Map<String, String> headerMap = new HashMap<String, String>();
    headerMap.put(TokenUtil.TOKEN, token);

    JSONObject jsonRequest = new JSONObject();
    jsonRequest.put("app", app);

    String response = performHttpRequest(accountURL, jsonRequest.toString(), headerMap);
    JSONObject jsonResponse = JSONObject.fromObject(response);
    Integer returnCode = (Integer) jsonResponse.get(ApiReturnObjectUtil.KEY_RETURN_CODE);
    if (returnCode == ReturnCode.OK.getValue()) {
      return jsonResponse;
    }
    return null;
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
