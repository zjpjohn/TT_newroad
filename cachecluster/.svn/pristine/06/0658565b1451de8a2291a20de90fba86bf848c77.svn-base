package com.newroad.cache.common.memcached;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



import com.newroad.cache.common.Cache;
import com.whalin.MemCached.MemCachedClient;

public class MemcachedCache implements Cache<String, Serializable> {

	// private static Logger LOG =
	// LoggerFactory.getLogger(CouchbaseCache.class);

	private MemCachedClient cacheClient;

	public MemcachedCache(MemcachedProperties memProp) {
		this.cacheClient = MemcachedManager.getCacheClientInstance();
	}

	/**
	 * 缓存数据过期时间
	 * 
	 * @return
	 */
	public Date getExpiry(int seconds) {
		Calendar expiry = Calendar.getInstance();
		expiry.add(Calendar.SECOND, seconds);
		return expiry.getTime();
	}

	public long getExpiryLongFromNow(int seconds) {
		Calendar expiry = Calendar.getInstance();
		expiry.add(Calendar.SECOND, seconds);
		return expiry.getTimeInMillis();
	}

	/**
	 * 获得当前时间的long值
	 */
	public long getNowLong() {
		return System.currentTimeMillis();
	}

	@Override
	public boolean set(String k, Serializable v) {
		return cacheClient.set(k, v);
	}

	@Override
	public boolean set(String k, Serializable v, long time) {
		return cacheClient.set(k, v, new Date(time));
	}

	@Override
	public Serializable get(String k) {
		return (Serializable) cacheClient.get(k);
	}

	@Override
	public boolean delete(String key) {
		return cacheClient.delete(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lenovo.common.cache.Cache#getAllPresent(java.lang.Iterable)
	 */
	@Override
	public Map<String, Serializable> getAllPresent(Iterable<String> keys) {
		Map<String, Serializable> cacheMap = new ConcurrentHashMap<String, Serializable>();
		for (Iterator<String> itemIt = keys.iterator(); itemIt.hasNext();) {
			String itemKey = itemIt.next();
			cacheMap.put(itemKey, get(itemKey));
		}
		return cacheMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lenovo.common.cache.Cache#setAll(java.util.Map)
	 */
	@Override
	public int setAll(Map<? extends String, ? extends Serializable> m) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lenovo.common.cache.Cache#deleteAll(java.lang.Iterable)
	 */
	@Override
	public int deleteAll(Iterable<String> keys) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lenovo.common.cache.Cache#deleteAll()
	 */
	@Override
	public boolean deleteAll() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lenovo.common.cache.Cache#size()
	 */
	@Override
	public long size() {
		long size = 0;
		Map<String, Map<String, String>> items = cacheClient.statsItems();

		for (Iterator<Entry<String, Map<String, String>>> itemIt = items
				.entrySet().iterator(); itemIt.hasNext();) {
			Entry<String, Map<String, String>> entry = itemIt.next();
			Map<String, String> valueMap = entry.getValue();

			for (Iterator<Entry<String, String>> mapsIt = valueMap.entrySet()
					.iterator(); mapsIt.hasNext();) {
				Entry<String, String> mapEntry = mapsIt.next();
				String mapsValue = mapEntry.getValue();
				if (mapEntry.getKey().endsWith("number")) {// memcached key 类型
					// item_str:integer:number_str
					int keyNumber = Integer.valueOf(mapsValue.trim());
					size += keyNumber;
				}
			}
		}
		return size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lenovo.common.cache.Cache#asMap()
	 */
	@Override
	public ConcurrentMap<String, Serializable> asMap() {
		ConcurrentMap<String, Serializable> cacheMap = new ConcurrentHashMap<String, Serializable>();
		Map<String, Map<String, String>> items = cacheClient.statsItems();
		Map<Integer, Integer> itemKeys = getItemKeys(items);

		for (Iterator<Entry<Integer, Integer>> itemKeysIt = itemKeys.entrySet()
				.iterator(); itemKeysIt.hasNext();) {
			Entry<Integer, Integer> entry = itemKeysIt.next();
			Map<String, Map<String, String>> dumpMaps = cacheClient
					.statsCacheDump(entry.getKey(), entry.getValue());
			for (Iterator<Entry<String, Map<String, String>>> dumpIt = dumpMaps
					.entrySet().iterator(); dumpIt.hasNext();) {
				Entry<String, Map<String, String>> dumpEntry = dumpIt.next();
				Map<String, String> allMap = dumpMaps.get(dumpEntry.getKey());
				for (Iterator<Entry<String, String>> allIt = allMap.entrySet()
						.iterator(); allIt.hasNext();) {
					Entry<String, String> allEntry = allIt.next();
					String key = allEntry.getKey();
					// Couldn't get cache value via key using
					// statsCacheDump
					try {
						cacheMap.put(URLDecoder.decode(key, "UTF-8"), get(key));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		return cacheMap;
	}

	public List<String> keys() {
		// String[] serverlist = MEM_SERVER_LIST.split(",");
		List<String> list = new ArrayList<String>();
		Map<String, Map<String, String>> items = cacheClient.statsItems();
		Map<Integer, Integer> itemKeys = getItemKeys(items);

		for (Iterator<Entry<Integer, Integer>> itemKeysIt = itemKeys.entrySet()
				.iterator(); itemKeysIt.hasNext();) {
			Entry<Integer, Integer> entry = itemKeysIt.next();
			Map<String, Map<String, String>> dumpMaps = cacheClient
					.statsCacheDump(entry.getKey(), entry.getValue());
			for (Iterator<Entry<String, Map<String, String>>> dumpIt = dumpMaps
					.entrySet().iterator(); dumpIt.hasNext();) {
				Entry<String, Map<String, String>> dumpEntry = dumpIt.next();
				Map<String, String> allMap = dumpMaps.get(dumpEntry.getKey());
				for (Iterator<Entry<String, String>> allIt = allMap.entrySet()
						.iterator(); allIt.hasNext();) {
					Entry<String, String> allEntry = allIt.next();
					String allKey = allEntry.getKey();
					try {
						list.add(URLDecoder.decode(allKey, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}
		return list;
	}

	public Map<String, String> keysExpire() throws UnsupportedEncodingException {
		// String[] serverlist = MEM_SERVER_LIST.split(",");
		Map<String, String> keyMap = new HashMap<String, String>();
		Map<String, Map<String, String>> items = cacheClient.statsItems();
		Map<Integer, Integer> itemKeys = getItemKeys(items);

		for (Iterator<Entry<Integer, Integer>> itemKeysIt = itemKeys.entrySet()
				.iterator(); itemKeysIt.hasNext();) {
			Entry<Integer, Integer> entry = itemKeysIt.next();
			Map<String, Map<String, String>> dumpMaps = cacheClient
					.statsCacheDump(entry.getKey(), entry.getValue());
			for (Iterator<Entry<String, Map<String, String>>> dumpIt = dumpMaps
					.entrySet().iterator(); dumpIt.hasNext();) {
				Entry<String, Map<String, String>> dumpEntry = dumpIt.next();
				Map<String, String> allMap = dumpMaps.get(dumpEntry.getKey());
				for (Iterator<Entry<String, String>> allIt = allMap.entrySet()
						.iterator(); allIt.hasNext();) {
					Entry<String, String> allEntry = allIt.next();
					String value = allEntry.getValue();
					int start = value.indexOf(";");
					int end = value.indexOf("s");
					String expire = value.substring(start + 1, end).trim();
					keyMap.put(URLDecoder.decode(allEntry.getKey(), "UTF-8"),
							expire);
				}
			}
		}
		return keyMap;
	}

	private Map<Integer, Integer> getItemKeys(
			Map<String, Map<String, String>> items) {
		Map<Integer, Integer> itemMap = new HashMap<Integer, Integer>();
		for (Iterator<Entry<String, Map<String, String>>> itemIt = items
				.entrySet().iterator(); itemIt.hasNext();) {
			Entry<String, Map<String, String>> itemEntry = itemIt.next();
			Map<String, String> valueMaps = itemEntry.getValue();

			for (Iterator<Entry<String, String>> mapsIt = valueMaps.entrySet()
					.iterator(); mapsIt.hasNext();) {
				Entry<String, String> mapEntry = mapsIt.next();
				String mapsKey = mapEntry.getKey();
				String mapsValue = mapEntry.getValue();

				if (mapsKey.endsWith("number")) {// memcached key 类型
													// item_str:integer:number_str
					String[] arr = mapsKey.split(":");
					int slabNumber = Integer.valueOf(arr[1].trim());
					int limit = Integer.valueOf(mapsValue.trim());
					itemMap.put(slabNumber, limit);
				}
			}
		}
		return itemMap;
	}
}
