package com.newroad.cache.common.memcached;

/**
 * @info
 * @author tangzj1
 * @date  Feb 12, 2014 
 * @version 
 */
public class MemcachedProperties {
	// memcached server list : memcached.properties
	private static String MEM_SERVER_LIST;

	// memcached server name
	private static String MEM_SERVER_NAME;

	// 开始时cache pool的可用连接数
	private static int MEM_SERVER_INIT_CONN;

	// cache pool最大可用连接数
	private static int MEM_SERVER_MAX_CONN;

	// cache pool最少可用连接数
	private static int MEM_SERVER_MIN_CONN;
	
	
	/**
	 * @param mEM_SERVER_LIST
	 * @param mEM_SERVER_NAME
	 * @param mEM_SERVER_INIT_CONN
	 * @param mEM_SERVER_MAX_CONN
	 * @param mEM_SERVER_MIN_CONN
	 */
	public MemcachedProperties(String mEM_SERVER_LIST, String mEM_SERVER_NAME,
			int mEM_SERVER_INIT_CONN, int mEM_SERVER_MAX_CONN,
			int mEM_SERVER_MIN_CONN) {
		super();
		MEM_SERVER_LIST = mEM_SERVER_LIST;
		MEM_SERVER_NAME = mEM_SERVER_NAME;
		MEM_SERVER_INIT_CONN = mEM_SERVER_INIT_CONN;
		MEM_SERVER_MAX_CONN = mEM_SERVER_MAX_CONN;
		MEM_SERVER_MIN_CONN = mEM_SERVER_MIN_CONN;
	}

	public static String getMEM_SERVER_LIST() {
		return MEM_SERVER_LIST;
	}
	public static String getMEM_SERVER_NAME() {
		return MEM_SERVER_NAME;
	}
	public static int getMEM_SERVER_INIT_CONN() {
		return MEM_SERVER_INIT_CONN;
	}
	public static int getMEM_SERVER_MAX_CONN() {
		return MEM_SERVER_MAX_CONN;
	}
	public static int getMEM_SERVER_MIN_CONN() {
		return MEM_SERVER_MIN_CONN;
	}
}
