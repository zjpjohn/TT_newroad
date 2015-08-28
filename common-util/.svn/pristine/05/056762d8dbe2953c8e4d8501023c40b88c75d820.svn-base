package com.newroad.util.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

public class TokenUtil {

  /**
   * 用户认证TOKEN KEY
   */
  public static final String TOKEN = "token";

  private TokenUtil() {}

  public static String generate(String appId, String appKeyId, String appKey, String userSlug, long expirationEpoch) {
    String token = "";
    String targetJson = null;
    JSONObject json = new JSONObject();
    json.put("user_slug", userSlug);
    json.put("app_id", appId);
    json.put("expiration", expirationEpoch);
    targetJson = json.toString();
    try {
      token = URLEncoder.encode(AuthUtil.rc4(appKey.getBytes("utf-8"), targetJson.getBytes("utf-8")), "utf-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return token;
  }
}
