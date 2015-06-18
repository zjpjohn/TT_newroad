package com.newroad.util;

import java.text.NumberFormat;

public class MathsHelper {
	
	private MathsHelper(){
	}

	public static String getPercent(long a, long b) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		String result = numberFormat.format((float) a / (float) b * 100);
		return result;
	}
}
