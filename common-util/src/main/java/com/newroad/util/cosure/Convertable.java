package com.newroad.util.cosure;

/**
 * @info  : 可转换的  用于对象转换
 * @author: xiangping_yu
 * @data  : 2013-6-9
 * @since : 1.5
 */
public interface Convertable<Q, T> {
	
	/**
	 * @param q  被转换对象
	 * @return 转换结果
	 */
	T convert(Q q);
}
