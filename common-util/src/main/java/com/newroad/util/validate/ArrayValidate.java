package com.newroad.util.validate;

import java.util.Arrays;
import java.util.List;

public class ArrayValidate {
	
	private ArrayValidate(){
	}

	/**
	 * 验证指定的数组是否包含指定的元素
	 * 
	 * @param array
	 * @param element
	 * @return
	 */
	public static boolean contain(Object[] array, Object element) {
		if (array == null) {
			throw new RuntimeException("array is null.");
		}
		List<Object> arrayList = Arrays.asList(array);
		if (element != null && arrayList.contains(element)) {
			return true;
		}
		return false;
	}
}
