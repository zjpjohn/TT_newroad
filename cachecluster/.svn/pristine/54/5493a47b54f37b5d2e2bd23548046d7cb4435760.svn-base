package com.newroad.cache.common;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

/**
 * @info Cache base on google common cache
 * @author tangzj1
 * @date Jan 13, 2014
 * @version
 */
public interface Cache<K, V> {

	/**
	 * @param key
	 * @return V
	 */
	V get(K key);

	/**
	 * Returns a map of the values associated with {@code keys} in this cache.
	 * The returned map will only contain entries which are already present in
	 * the cache.
	 * 
	 * @since 11.0
	 */
	Map<K, V> getAllPresent(Iterable<K> keys);

	/**
	 * Associates {@code value} with {@code key} in this cache. If the cache
	 * previously contained a value associated with {@code key}, the old value
	 * is replaced by {@code value}.
	 * 
	 * <p>
	 * Prefer {@link #get(Object, Callable)} when using the conventional "if
	 * cached, return; otherwise create, cache and return" pattern.
	 * 
	 * @throws Exception
	 * 
	 * @since 11.0
	 */ 
	boolean set(K key, V value);

	boolean set(K key, V value, long expire);

	/**
	 * Copies all of the mappings from the specified map to the cache. The
	 * effect of this call is equivalent to that of calling {@code put(k, v)} on
	 * this map once for each mapping from key {@code k} to value {@code v} in
	 * the specified map. The behavior of this operation is undefined if the
	 * specified map is modified while the operation is in progress.
	 * 
	 * @since 12.0
	 */
	int setAll(Map<? extends K, ? extends V> m);

	/**
	 * Discards any cached value for key {@code key}.
	 */
	boolean delete(K key);

	/**
	 * Discards any cached values for keys {@code keys}.
	 * 
	 * @since 11.0
	 */
	int deleteAll(Iterable<K> keys);

	/**
	 * Discards all entries in the cache.
	 */
	boolean deleteAll();

	/**
	 * Returns the approximate number of entries in this cache.
	 */
	long size();

	/**
	 * Returns a view of the entries stored in this cache as a thread-safe map.
	 * Modifications made to the map directly affect the cache.
	 */
	ConcurrentMap<K, V> asMap();
	
	List<K> keys();

}
