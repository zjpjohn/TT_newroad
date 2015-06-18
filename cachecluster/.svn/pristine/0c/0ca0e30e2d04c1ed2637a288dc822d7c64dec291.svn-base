package com.newroad.cache.common.memcached;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.newroad.cache.common.CacheManager;
import com.schooner.MemCached.SchoonerSockIOPool;
import com.whalin.MemCached.MemCachedClient;

/**
 * @info
 * @author tangzj1
 * @date Feb 12, 2014
 * @version
 */
public class MemcachedManager implements CacheManager {

	private static Logger LOG = LoggerFactory.getLogger(MemcachedManager.class);

	private static MemCachedClient memcachedClient;

	@PostConstruct
	public void init() {
		String[] serverlist = MemcachedProperties.getMEM_SERVER_LIST().split(
				",");

		// Integer[] weights = { 3 };
		SchoonerSockIOPool pool = SchoonerSockIOPool
				.getInstance(MemcachedProperties.getMEM_SERVER_NAME());
		pool.setServers(serverlist);
		// 开始时每个cache服务器的可用连接数
		pool.setInitConn(MemcachedProperties.getMEM_SERVER_INIT_CONN());
		pool.setMinConn(MemcachedProperties.getMEM_SERVER_MIN_CONN());
		pool.setMaxConn(MemcachedProperties.getMEM_SERVER_MAX_CONN());
		/*
		 * 设置连接池维护线程的睡眠时间 设置为0，维护线程不启动
		 * 维护线程主要通过log输出socket的运行状况，监测连接数目及空闲等待时间等参数以控制连接创建和关闭。
		 */
		pool.setMaintSleep(0);
		// 设置是否使用Nagle算法，因为我们的通讯数据量通常都比较大（相对TCP控制数据）而且要求响应及时，因此该值需要设置为false（默认是true）
		pool.setNagle(false);
		pool.initialize();

		if (!pool.isInitialized())
			LOG.error("************************************memcache 初始化失败 ***************************");

		memcachedClient = new MemCachedClient(
				MemcachedProperties.getMEM_SERVER_NAME());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lenovo.common.cache.CacheManager#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * 得到缓存连接
	 */
	public static MemCachedClient getCacheClientInstance() {
		if (memcachedClient == null) {
			new MemcachedManager().init();
		}
		return memcachedClient;
	}
}
