package com.newroad.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
  
  private MD5Utils() {}
	
	private static final String HEX_DIGITS[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	public static String encodeAsNum(final String origin) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			return byteArrayToString(md.digest(origin.getBytes()), 10);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("MD5 encode as num error!");
	}

	public static String encodeAsHex(final String origin) {
		if (origin == null)
			return null;

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			return byteArrayToString(md.digest(origin.getBytes()), 16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("MD5 encode as hex error!");
	}

	private static String byteArrayToString(byte b[], int type) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			if (type == 16)
				resultSb.append(byteToHexString(b[i]));
			if (type == 10)
				resultSb.append(byteToNumString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToNumString(byte b) {
		int _b = b;
		if (_b < 0)
			_b += 256;
		return String.valueOf(_b);
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return (new StringBuilder(String.valueOf(HEX_DIGITS[d1]))).append(HEX_DIGITS[d2]).toString();
	}

	public static void main(String[] args) throws Exception {
//		System.out.println(MD5Utils.encodeAsHex("123456"));
	}

}
