package com.newroad.util.auth;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

//用户验证网盘接口的工具类
public final class AuthUtil {
	
	private AuthUtil(){}
	
	public static final byte[] hmacSHA1(byte[] hMacKey,byte[] target){
		String algorithem = "HmacSHA1";
		byte[] hmacResult = null;
		try {
			SecretKeySpec secret  = new SecretKeySpec(hMacKey,algorithem);
			Mac mac               = Mac.getInstance(algorithem);
			mac.init(secret);
			hmacResult     = mac.doFinal(target);
		} catch (Exception e) {
			//logger.warn("HmacSHA1加密异常", e);
			throw new RuntimeException("HmacSHA1加密异常",e);
		}
		return hmacResult;
	}
	
	public static final String base64(byte[] target){
        Base64 encoder = new Base64();
        String result         = new String(encoder.encode(target));
        return result;
    }
	
	
	public static final String rc4(byte[] key,byte[] target){
		SecretKeySpec skey       = new SecretKeySpec(key,"RC4");
		Cipher cipher            = null;
		String result            = null;
		try {
			cipher               = Cipher.getInstance("RC4");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			byte[] input         = target;
			byte[] output        = new byte[input.length];
			int len              = cipher.update(input,0, input.length, output,0);
			cipher.doFinal(output,len);
			result               = base64(output);
		} catch (Exception e) {
			//logger.warn("RC4加密异常",e);
			e.printStackTrace();
			throw new RuntimeException("RC4加密异常",e);
		}
		return result;
	}
	
	public static final byte[] md5(byte[] src){
	    MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return md.digest(src);
	}
}
