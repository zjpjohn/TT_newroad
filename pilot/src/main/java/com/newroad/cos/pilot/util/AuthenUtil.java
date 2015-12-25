package com.newroad.cos.pilot.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

// 用户验证网盘接口的工具类
public final class AuthenUtil {

  public static Charset utf8 = Charset.forName("utf-8");

  private AuthenUtil() {

  }

  public static final byte[] hmacSHA1(byte[] hMacKey, byte[] target) {
    String algorithem = "HmacSHA1";
    byte[] hmacResult = null;
    try {
      SecretKeySpec secret = new SecretKeySpec(hMacKey, algorithem);
      Mac mac = Mac.getInstance(algorithem);
      mac.init(secret);
      hmacResult = mac.doFinal(target);
    } catch (Exception e) {
      // logger.warn("HmacSHA1加密异常", e);
      throw new RuntimeException("HmacSHA1加密异常", e);
    }
    return hmacResult;
  }

  // @SuppressWarnings("restriction")
  public static String base64(byte[] target) {
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(target);
  }

  public static final String rc4(byte[] key, byte[] target) {
    SecretKeySpec skey = new SecretKeySpec(key, "RC4");
    Cipher cipher = null;
    String result = null;
    try {
      cipher = Cipher.getInstance("RC4");
      cipher.init(Cipher.ENCRYPT_MODE, skey);
      byte[] input = target;
      byte[] output = new byte[input.length];
      int len = cipher.update(input, 0, input.length, output, 0);
      cipher.doFinal(output, len);
      result = base64(output);
    } catch (Exception e) {
      throw new RuntimeException("RC4 Encoding Exception:", e);
    }
    return result;
  }

  public static final byte[] md5(byte[] src) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return md.digest(src);
  }
}
