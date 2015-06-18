package com.newroad.util;

/**
 * info  : 应用帮助类
 * autor : xiangping_yu
 * data  : 2013-5-27
 * since : 1.5
 */
public class AppHelper {
	
	private AppHelper(){
	}

	/**
	 * 运行环境 key  jvm启动时指定 (-Dcom.lenovo.runModel=dev)
	 * 默认为生产环境  'dev' 开发, 'test' 测试 ,  'release' 生产
	 */
	private static final String RUN_MODEL = "com.lenovo.runModel";

	public static boolean isDebug(){
		String model = System.getProperty(RUN_MODEL); 
		return "dev".equals(model) ;
	}
}
