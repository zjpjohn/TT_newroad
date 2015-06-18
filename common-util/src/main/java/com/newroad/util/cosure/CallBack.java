package com.newroad.util.cosure;

/**
 * @info  : 回调接口 
 * @author: xiangping_yu
 * @data  : 2013-6-9
 * @since : 1.5
 */
public interface CallBack<T, V> {
	T callBack(V v);
}
