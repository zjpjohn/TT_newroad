package com.newroad.util;

import java.util.Calendar;

import org.apache.commons.lang.time.DateFormatUtils;

public class TimeHelper {
	
	private TimeHelper(){
	}
	/**
	 * 把距历元(1970/1/1 0:0:0)的毫秒数 转换为: 时:分格式
	 * @param l
	 * @return
	 */
	public static String parseTime(long l){
		return DateFormatUtils.format(l, "HH:mm");
	}
	
	/**
	 * 把距历元(1970/1/1 0:0:0)的毫秒数 转换为 :月-日格式
	 * @param l
	 * @return
	 */
	public static String parseDay(long l){
		return DateFormatUtils.format(l, "MM-dd");
	}
	
	/**
	 * 获得当天0时的毫秒数		
	 * @return
	 */
	public static long getZeroTimeOfToday() {
		Calendar zero = Calendar.getInstance();
		zero.set(Calendar.HOUR_OF_DAY, 0);
		zero.set(Calendar.MINUTE, 0);
		zero.set(Calendar.SECOND, 0);
		return zero.getTimeInMillis();
	}
	
	/**
	 * 获得当天23:59:59时的毫秒数		
	 * @return
	 */
	public static long getEndTimeOfToday(int extraHours) {
		Calendar zero = Calendar.getInstance();
		zero.set(Calendar.HOUR_OF_DAY, 23);
		zero.set(Calendar.MINUTE, 59);
		zero.set(Calendar.SECOND, 59);
		zero.add(Calendar.HOUR_OF_DAY, extraHours);
		return zero.getTimeInMillis();
	}
}
